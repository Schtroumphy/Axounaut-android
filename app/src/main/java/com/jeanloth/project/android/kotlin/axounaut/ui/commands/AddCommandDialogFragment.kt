package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.AppLogger.logD
import com.jeanloth.project.android.kotlin.axounaut.Constants.SHORT_DATE_FORMAT_DATE_FIRST
import com.jeanloth.project.android.kotlin.axounaut.Constants.SHORT_DATE_FORMAT_YEAR_FIRST
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.AddClientDialogBinding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddCommandDialogBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.*
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddCommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.alertdialog.appcompat.negativeButton
import splitties.alertdialog.appcompat.positiveButton
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.imageDrawable
import splitties.views.onClick
import java.time.LocalDate
import java.util.*


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddCommandDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class AddCommandDialogFragment (
    private val currentCommand: Command?= null
): BottomSheetDialogFragment() {

    // Varaiables
    private var isEditMode = true
    private var isEditCommandMode = false

    private var selectedClient : AppClient? = null

    private lateinit var datePickerDialog : DatePickerDialog
    lateinit var bottomSheetDialog: BottomSheetDialog

    // Adapters
    private lateinit var articleAdapter: ArticleAdapter

    private lateinit var binding: FragmentAddCommandDialogBinding

    // View models
    private val clientVM : ClientVM by sharedViewModel()
    private val articleVM : ArticleVM by viewModel()
    private val addCommandVM : AddCommandVM by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomSheetDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return bottomSheetDialog.fullScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCommandDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupHeaders()

        javaClass.logD("Clients : ${clientVM.allClientsMutableLiveData.value}", "Current command $currentCommand")

        val articles : List<Article> = articleVM.getAllArticles()
        addCommandVM.setAllArticlesLiveData(createWrapperList(articles))

        /** In case of edit mode **/
        if(currentCommand != null) {
            addCommandVM.setDeliveryDate(currentCommand.deliveryDate)
            addCommandVM.setClientLiveData(currentCommand.client)
            addCommandVM.setArticlesLiveData(currentCommand.articleWrappers)
            isEditCommandMode = true
            setupElements()
        }

        articleAdapter = ArticleAdapter(emptyList(), true, requireContext()).apply {
            onAddMinusClick = {
                javaClass.logD("Articles list : $it")
                addCommandVM.setArticlesLiveData(it)
            }
            displayNoArticlesError = {
                binding.tvErrorNoArticles.visibility = if(it) VISIBLE else GONE
            }
        }

        // Update article list
        addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SWEET.code }
            ?.let { articleAdapter.setItems(it, true) }

        binding.rvArticles.adapter = articleAdapter

        // Get all clients
        clientVM.allClientsLiveData().observe(viewLifecycleOwner){
            javaClass.logD("Client observed map : ${it.map { it.toNameString() }}")
            val clientAdapter: ArrayAdapter<AppClient> = ArrayAdapter<AppClient>(
                requireContext(),
                android.R.layout.simple_list_item_1, it
            )
            clientAdapter.setNotifyOnChange(true)
            binding.etClient.threshold = 1
            binding.etClient.setAdapter(clientAdapter)
        }

        lifecycleScope.launchWhenStarted {
            addCommandVM.canResumeStateFlow.collectLatest {
                javaClass.logD("[AddCommandDialogFrament] Can resume ? $it")
                binding.btNext.isEnabled = it
            }
        }

        setupListenerForAutoCompleteTvClient()
        setupDateEditText()

        // Filter articles
        binding.btSweet.onClick {
            updateDisplayByCategory(ArticleCategory.SWEET.code)
            articleAdapter.setItems(addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SWEET.code } ?: emptyList(), true)
        }

        binding.btSalt.onClick {
            updateDisplayByCategory(ArticleCategory.SALTED.code)
            articleAdapter.setItems(addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SALTED.code } ?: emptyList(), true)
        }

        // Add data (client, article)
        binding.ibAddClient.onClick {
            showAddClientDialog()
        }
        binding.btAddArticle.onClick {
            dismiss()

            // Redirect to add article fragment
            // If opened from Command Details --> CommandDetailFragmentDirections
            // If opened from Command list fragment --> CommandListFragmentDirections
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(if(currentCommand == null) CommandListFragmentDirections.actionNavCommandListToNavArticle() else CommandDetailFragmentDirections.actionNavCommandDetailToNavArticle())
        }

        if(currentCommand != null) fillElements()

        // Next step
        binding.btNext.onClick {
            // Display previous button
            if(isEditMode)
                changeEditModeDisplay()
            else {
                // Save command
                saveCommand(currentCommand)
            }
        }
    }

    private fun fillElements() {
        binding.etClient.setText(currentCommand?.client?.firstname)
        binding.etDeliveryDate.setText(currentCommand?.deliveryDate)
    }

    private fun updateDisplayByCategory(code: Int) {
        when(code){
            ArticleCategory.SWEET.code -> {
                binding.btSweet.setTextColor(getColor(requireContext(), R.color.orange_001))
                binding.sweetDivider.visibility = VISIBLE

                binding.btSalt.setTextColor(getColor(requireContext(), R.color.marron_light_1))
                binding.saltDivider.visibility = GONE
            }
            ArticleCategory.SALTED.code -> {
                binding.btSweet.setTextColor(getColor(requireContext(), R.color.marron_light_1))
                binding.sweetDivider.visibility = GONE

                binding.btSalt.setTextColor(getColor(requireContext(), R.color.orange_001))
                binding.saltDivider.visibility = VISIBLE
            }
        }
    }

    private fun setupDateEditText() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.etDeliveryDate.setOnClickListener {
                datePickerDialog = DatePickerDialog( requireContext(),{ _, year, monthOfYear, dayOfMonth ->
                    val dateSelected = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    javaClass.logD("[DATE SELECTED] $dateSelected")
                    javaClass.logD("[DATE SELECTED] ${dateSelected.formatDateToOtherFormat(SHORT_DATE_FORMAT_YEAR_FIRST, SHORT_DATE_FORMAT_DATE_FIRST)}")

                    binding.etDeliveryDate.setText("${dateSelected.formatDateToOtherFormat(SHORT_DATE_FORMAT_YEAR_FIRST, SHORT_DATE_FORMAT_DATE_FIRST)}")
                    addCommandVM.setDeliveryDate(dateSelected.formatDateToOtherFormat(SHORT_DATE_FORMAT_YEAR_FIRST, SHORT_DATE_FORMAT_DATE_FIRST))
                    binding.etDeliveryDate.clearFocus()
                }, year, month, day)
            datePickerDialog.show()
            }
    }

    private fun setupListenerForAutoCompleteTvClient() {
        // IMPORTANT : If not implemented, the autocompletion won't work
        binding.etClient.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etClient.setOnItemClickListener { parent, _, position, _ ->
            val item: Any = parent.getItemAtPosition(position)
            if (item is AppClient) {
                Log.d("[Client Fragment]", "Client selected : $item")
                selectedClient = item
                addCommandVM.setClientLiveData(item)
            }
            this.hideKeyboard()
            binding.etClient.clearFocus()
        }
    }

    private fun changeEditModeDisplay() {
        isEditMode = !isEditMode
        binding.btAddArticle.visibility = if(isEditMode) VISIBLE else GONE
        binding.llBtCategory.visibility = if(isEditMode) VISIBLE else GONE
        binding.ibAddClient.visibility = if(isEditMode) VISIBLE else GONE
        setupPreviousCloseButton()
        articleAdapter.setItems(addCommandVM.allArticlesLiveData.value ?: emptyList(), isEditMode)
        setupElements()
        bottomSheetDialog.fullScreen()
    }

    private fun setupElements() {
        binding.tvAddCommandTitle.text = getString(if (isEditMode) R.string.add_command_title else R.string.recap)
        if(isEditCommandMode){
            binding.tvAddCommandTitle.text = "Modifier la commande"
            binding.btNext.text = "Mettre à jour "
        }
        binding.etClient.isEnabled = isEditMode
        binding.etDeliveryDate.isEnabled = isEditMode
        binding.ibAddClient.isEnabled = isEditMode

        binding.tvTotalPrice.visibility = if(isEditMode) GONE else VISIBLE
        if(!isEditMode)  binding.tvTotalPrice.text = getString(R.string.total_price,
            addCommandVM.allArticlesLiveData.value?.filter { it.count > 0 }?.map { it.count * it.article.price }?.sum().toString()
        )
    }

    private fun setupPreviousCloseButton() {
        binding.btPreviousOrClose.imageDrawable = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_close else R.drawable.ic_left_arrow
        )
    }

    private fun setupHeaders() {
        binding.btPreviousOrClose.setOnClickListener {
            if (isEditMode) dismiss() else changeEditModeDisplay()
        }
    }

    private fun saveCommand(command: Command? = null){
        val commandToSave = command?.apply {
            deliveryDate = binding.etDeliveryDate.text.toString()
            if(selectedClient != null) client = selectedClient
            articleWrappers = addCommandVM.allArticlesLiveData.value?.filter { it.count > 0 } ?: emptyList()
        } ?: Command(
            deliveryDate = binding.etDeliveryDate.text.toString(),
            client = selectedClient,
            articleWrappers = addCommandVM.allArticlesLiveData.value?.filter { it.count > 0 } ?: emptyList()
        )
        javaClass.logD("Command to save $commandToSave")

        // Call VM to save Command
        addCommandVM.saveCommand(commandToSave, true)

        dialog?.dismiss()
    }

    private fun showAddClientDialog(){
        val dialogAlertCommonBinding = AddClientDialogBinding.inflate(LayoutInflater.from(context))
        requireContext().materialAlertDialog {
            setView(dialogAlertCommonBinding.root)
            positiveButton(R.string.validate) {
                javaClass.logD("Clic sur ok")
                addClient(dialogAlertCommonBinding)
            }
            negativeButton(R.string.cancel){
                dismiss()
            }
        }.show()
    }

    private fun addClient(binding : AddClientDialogBinding) {
        val clientName = binding.etClientFirstname.text.toString()
        val clientPhoneNumber = binding.etClientPhone.text.toString()

        if( clientName.isEmpty() && !clientPhoneNumber.isPhoneValid()) {
            Snackbar.make(requireView(), "Veuillez saisir des valeurs valides.",
                Snackbar.LENGTH_LONG).show()
        } else {
            val clientToAdd = AppClient(
                firstname = clientName,
                lastname = "",
                phoneNumber = clientPhoneNumber.toInt(),
            )
            javaClass.logD("Client to add : $clientToAdd")

            clientVM.saveClient(clientToAdd)
            Snackbar.make(requireView(), "Client ajouté avec succès.",
                Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(command : Command?= null) = AddCommandDialogFragment(command)
    }
}
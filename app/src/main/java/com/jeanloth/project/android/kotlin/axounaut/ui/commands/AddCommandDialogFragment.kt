package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.*
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddCommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import kotlinx.android.synthetic.main.add_client_dialog.view.*
import kotlinx.android.synthetic.main.fragment_add_command_dialog.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.alertdialog.appcompat.*
import java.time.LocalDate
import java.util.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.imageDrawable
import splitties.views.imageResource
import splitties.views.onClick


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
    val currentCommand: Command?= null
): BottomSheetDialogFragment() {

    var isEditMode = true
    private lateinit var articleAdapter: ArticleAdapter
    private val clientVM : ClientVM by sharedViewModel()
    private val articleVM : ArticleVM by viewModel()
    private val addCommandVM : AddCommandVM by viewModel()
    private val commandVM : CommandVM by viewModel{
        parametersOf(
            0L
        )
    }
    private var selectedClient : AppClient? = null
    private lateinit var datePickerDialog : DatePickerDialog

    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return bottomSheetDialog.fullScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_command_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupHeaders()

        Log.d("[Add command fragment]", "Clients : ${clientVM.allClientsMutableLiveData.value}")
        Log.d("[Current command to edit]", "$currentCommand")

        val articles : List<Article> = articleVM.getAllArticles()
        addCommandVM.setAllArticlesLiveData(createWrapperList(articles))

        if(currentCommand != null) addCommandVM.setArticlesLiveData(currentCommand.articleWrappers)

        tv_error_no_articles.visibility = if(addCommandVM.allArticlesLiveData.value?.isEmpty() == true) VISIBLE else GONE
        articleAdapter = ArticleAdapter(addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SWEET.code } ?: emptyList(), true, requireContext()).apply {
            onAddMinusClick = {
                Log.d("ADD COMMAND", "  articles list : $it")
                addCommandVM.setArticlesLiveData(it)
            }
            displayNoArticlesError = {
                //tv_error_no_articles.visibility = if(it) VISIBLE else GONE
            }
        }
        rv_articles.adapter = articleAdapter

        // Get all clients
        clientVM.allClientsLiveData().observe(viewLifecycleOwner){
            Log.d("[Client Fragment", "Client observed map : ${it.map { it.toNameString() }}")
            val clientAdapter: ArrayAdapter<AppClient> = ArrayAdapter<AppClient>(
                requireContext(),
                android.R.layout.simple_list_item_1, it
            )
            clientAdapter.setNotifyOnChange(true)
            et_client.threshold = 1
            et_client.setAdapter(clientAdapter)
        }

        addCommandVM.canResumeLiveData.observe(viewLifecycleOwner){
            Log.d("[AddCommandDialog]", "Can resume ? $it")
            bt_next.isEnabled = it
        }

        setupListenerForAutoCompleteTvClient()
        setupDateEditText()

        // Filter articles
        bt_sweet.onClick {
            updateDisplayByCategory(ArticleCategory.SWEET.code)
            articleAdapter.setItems(addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SWEET.code } ?: emptyList(), true)
        }

        bt_salt.onClick {
            updateDisplayByCategory(ArticleCategory.SALTED.code)
            articleAdapter.setItems(addCommandVM.allArticlesLiveData.value?.filter { it.article.category == ArticleCategory.SALTED.code } ?: emptyList(), true)
        }

        // Add data (client, article)
        ib_add_client.onClick {
            showAddClientDialog()
        }
        bt_add_article.onClick {
            dismiss()
            // Redirect to add article fragment
            findNavController().navigate(CommandListFragmentDirections.actionNavCommandListToNavArticle())
        }

        if(currentCommand != null) fillElements()

        // Next step
        bt_next.onClick {
            // Display previous button
            if(isEditMode)
                changeEditModeDisplay()
            else {
                // Save command
                saveCommand()
                Log.d("ADD COMMAND", "Save command ")
            }
        }
    }

    private fun fillElements() {
        et_client.setText(currentCommand?.client?.firstname)
        et_delivery_date.setText(currentCommand?.deliveryDate)
    }

    private fun updateDisplayByCategory(code: Int) {
        when(code){
            ArticleCategory.SWEET.code -> {
                bt_sweet.setTextColor(getColor(requireContext(), R.color.orange_001))
                sweet_divider.visibility = VISIBLE

                bt_salt.setTextColor(getColor(requireContext(), R.color.marron_light_1))
                salt_divider.visibility = GONE
            }
            ArticleCategory.SALTED.code -> {
                bt_sweet.setTextColor(getColor(requireContext(), R.color.marron_light_1))
                sweet_divider.visibility = GONE

                bt_salt.setTextColor(getColor(requireContext(), R.color.orange_001))
                salt_divider.visibility = VISIBLE
            }
            ArticleCategory.OTHER.code -> {}
        }
    }

    private fun setupDateEditText() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        et_delivery_date.setOnClickListener {
                datePickerDialog = DatePickerDialog( requireContext(),{ view, year, monthOfYear, dayOfMonth ->
                    val dateSelected = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    Log.d("[DATE SELECTED]", "$dateSelected")
                    Log.d("[DATE SELECTED]", "${dateSelected.formatDateToOtherFormat("yyyy-MM-dd", "dd-MM-yyyy")}")

                    et_delivery_date.setText("${dateSelected.formatDateToOtherFormat("yyyy-MM-dd", "dd-MM-yyyy")}")
                    addCommandVM.setDeliveryDate(dateSelected.formatDateToOtherFormat("yyyy-MM-dd", "dd-MM-yyyy"))
                    et_delivery_date.clearFocus()
                }, year, month, day)
            datePickerDialog.show()
            }
    }

    private fun setupListenerForAutoCompleteTvClient() {
        // IMPORTANT : If not implemented, the autocompletion won't work
        et_client.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        et_client.setOnItemClickListener { parent, view, position, id ->
            val item: Any = parent.getItemAtPosition(position)
            if (item is AppClient) {
                Log.d("[Client Fragment]", "Client selected : $item")
                selectedClient = item
                addCommandVM.setClientLiveData(item)
            }
            this.hideKeyboard()
            et_client.clearFocus()
        }
    }

    private fun changeEditModeDisplay() {
        isEditMode = !isEditMode
        bt_add_article.visibility = if(isEditMode) VISIBLE else GONE
        ll_bt_category.visibility = if(isEditMode) VISIBLE else GONE
        ib_add_client.visibility = if(isEditMode) VISIBLE else GONE
        setupPreviousCloseButton()
        articleAdapter.setItems(addCommandVM.allArticlesLiveData.value ?: emptyList(), isEditMode)
        setupElements()
        bottomSheetDialog.fullScreen()
    }

    private fun setupElements() {
        tv_add_command_title.text = getString(if (isEditMode) R.string.add_command_title else R.string.recap)
        et_client.isEnabled = isEditMode
        et_delivery_date.isEnabled = isEditMode
        ib_add_client.isEnabled = isEditMode // TODO Color selector enable

        tv_total_price.visibility = if(isEditMode) GONE else VISIBLE
        if(!isEditMode)  tv_total_price.text = getString(R.string.total_price,
            addCommandVM.allArticlesLiveData.value?.filter { it.count > 0 }?.map { it.count * it.article.price }?.sum()?.formatDouble()
        )
    }

    private fun setupPreviousCloseButton() {
        bt_previous_or_close.imageDrawable = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_close else R.drawable.ic_left_arrow
        )
    }

    private fun setupHeaders() {
        if(currentCommand != null) {
            tv_add_command_title.text = "Modifier la commande"
            bt_next.text = "Mettre à jour "
        }
        bt_previous_or_close.setOnClickListener {
            if (isEditMode) {
                dismiss()
            } else
                changeEditModeDisplay()
        }
    }

    private fun saveCommand(){
        val command = Command(
            deliveryDate = et_delivery_date.text.toString(),
            client = selectedClient,
            articleWrappers = addCommandVM.allArticlesLiveData.value?.filter { it.count > 0 } ?: emptyList()
        )
        Log.d("[AddCommand Fragment]", "Command to save $command")

        // Call VM to save Command
        commandVM.saveCommand(command)
        dialog?.dismiss()
    }

    private fun showAddClientDialog(){
        val dialogView = layoutInflater.inflate(R.layout.add_client_dialog, null)
        requireContext().materialAlertDialog {
            setView(dialogView)
            positiveButton(R.string.validate) {
                Log.d("[ADD COMMAND]", "Clic sur ok")
                addClient(dialogView)
            }
            negativeButton(R.string.cancel){
                dismiss()
            }
        }.show()
    }

    private fun addClient(view : View) {
        val clientName = view.et_client_firstname.text.toString()
        val clientPhoneNumber = view.et_client_phone.text.toString()

        if( clientName.isEmpty() && !clientPhoneNumber.isPhoneValid()) {
            Snackbar.make(requireView(), "Veuillez saisir des valeurs valides.",
                Snackbar.LENGTH_LONG).show()
        } else {
            val clientToAdd = AppClient(
                firstname = clientName,
                lastname = "",
                phoneNumber = clientPhoneNumber.toInt(),
            )
            Log.d("[Article Details Fragment", "Client to add : $clientToAdd")
            clientVM.saveClient(clientToAdd)
            Snackbar.make(requireView(), "Client ajouté avec succès.",
                Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(command : Command?= null) = AddCommandDialogFragment(command)
    }
}
package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.app.DatePickerDialog
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.*
import com.jeanloth.project.android.kotlin.axounaut.mock.DataMock
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import kotlinx.android.synthetic.main.add_client_dialog.*
import kotlinx.android.synthetic.main.add_client_dialog.view.*
import kotlinx.android.synthetic.main.fragment_add_command_dialog.*
import kotlinx.android.synthetic.main.fragment_client_details.*
import kotlinx.android.synthetic.main.fragment_client_details.et_client_firstname
import kotlinx.android.synthetic.main.fragment_client_details.et_client_lastname
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.alertdialog.appcompat.*
import splitties.alertdialog.appcompat.messageResource
import java.time.LocalDate
import java.util.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.inflate
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
class AddCommandDialogFragment : BottomSheetDialogFragment() {

    var articlesActualized = listOf<ArticleWrapper>()
    var isEditMode = true
    private lateinit var adapter: ArticleAdapter
    private val clientVM : ClientVM by sharedViewModel()
    private val articleVM : ArticleVM by viewModel()
    private val commandVM : CommandVM by viewModel{
        parametersOf(
            0L
        )
    }
    private var selectedClient : AppClient? = null
    private lateinit var datePickerDialog : DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_command_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupHeaders()

        Log.d("[Add command fragment]", "Clients : ${clientVM.allClientsMutableLiveData.value}")

        val articles = articleVM.getAllArticles()
        tv_error_no_articles.visibility = if(articles.isEmpty()) VISIBLE else GONE
        adapter = ArticleAdapter(createWrapperList(articles), true, requireContext()).apply {
            onAddMinusClick = {
                Log.d("ADD COMMAND", "  articles list : $it")
                bt_next.visibility  = if(it.count { it.count > 0 } > 0 && et_client.text.toString().isNotEmpty() && et_delivery_date.text.toString().isNotEmpty()) VISIBLE else INVISIBLE
                articlesActualized = it
            }
        }
        rv_articles.adapter = adapter

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

        /*et_client.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                et_client.clearFocus()
            }
            false
        }*/

        lifecycleScope.launchWhenStarted {
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
        }

        setupListenerForAutoCompleteTvClient()

        setupDateEditText()

        ib_add_client.onClick {
            showAddClientDialog()
        }
    }

    private fun setupDateEditText() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        et_delivery_date.setOnClickListener {
                this.hideKeyboard()
                datePickerDialog = DatePickerDialog( requireContext(),{ view, year, monthOfYear, dayOfMonth ->
                    val dateSelected = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    Log.d("[DATE SELECTED]", "$dateSelected")
                    Log.d("[DATE SELECTED]", "${dateSelected.formatDateToOtherFormat("yyyy-MM-dd", "dd-MM-yyyy")}")

                    et_delivery_date.setText("${dateSelected.formatDateToOtherFormat("yyyy-MM-dd", "dd-MM-yyyy")}")
                    et_delivery_date.clearFocus()
                    if(et_client.text.toString().isEmpty())
                        et_client.requestFocus()

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
            }
            this.hideKeyboard()
            et_client.clearFocus()
        }
    }

    private fun changeEditModeDisplay() {
        isEditMode = !isEditMode
        setupPreviousCloseButton()
        setupNextButton()
        adapter.setItems(articlesActualized, isEditMode)
        setupElements()
    }

    private fun setupElements() {
        tv_add_command_title.text = getString(if (isEditMode) R.string.add_command_title else R.string.recap)
        et_client.isEnabled = isEditMode
        et_delivery_date.isEnabled = isEditMode
        ib_add_client.isEnabled = isEditMode // TODO Color selector enable

        Log.d(
            "TAG",
            "${
                articlesActualized.filter { it.count > 0 }
                    .map { it.count * it.totalArticleWrapperPrice!! }.sum()
            }"
        )

        tv_total_price.visibility = if(isEditMode) GONE else VISIBLE
        if(!isEditMode)  tv_total_price.text = getString(R.string.total_price,
            articlesActualized.filter { it.count > 0 }.map { it.count * it.article.price }.sum()
                .formatDouble()
        )
    }

    private fun setupPreviousCloseButton() {
        bt_previous_or_close.background = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_close else R.drawable.ic_left_arrow
        )
    }

    private fun setupNextButton() {
        bt_next.background = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_right_arrow else R.drawable.ic_check
        )
    }

    private fun setupSelectedItems(article: Article) {
        if (articlesActualized.map { it.article.name }.contains(article.name)) {
            /*if (article > 0) {
                //articlesActualized[articlesActualized.map { it.name }.indexOf(article.name)] = article
            }
        } else {
            //articlesActualized.add(article)
        }*/
            //articlesActualized.removeIf { it.count == 0 }
            Log.d("ADD COMMAND", "Selected items : $articlesActualized")
        }
    }

    private fun setupHeaders() {
        bt_previous_or_close.setOnClickListener {
            if (isEditMode) {
                articlesActualized = mutableListOf()
                dismiss()
            } else
                changeEditModeDisplay()
        }
    }

    private fun saveCommand(){
        val command = Command(
            deliveryDate = et_delivery_date.text.toString(),
            client = selectedClient,
            totalPrice = articlesActualized.sumByDouble { it.totalArticleWrapperPrice ?: 0.0 },
            articleWrappers = articlesActualized.filter { it.count > 0 }
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
            cancelButton()
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

    companion object{
        fun newInstance() = AddCommandDialogFragment()
    }
}
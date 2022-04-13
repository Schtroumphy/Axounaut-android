package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxListAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentCommandDetailBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.SwipeToCancelCallback
import com.jeanloth.project.android.kotlin.axounaut.extensions.displayDialog
import com.jeanloth.project.android.kotlin.axounaut.extensions.notCanceled
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandDetailedVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType.Companion.getCommandStatusByCode
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.views.onClick

/**
 * Fragment to show details of a command (deliver it, save payment, delete it...)
 */
class CommandDetailFragment : Fragment() {

    private val mainVM: MainVM by sharedViewModel()

    private val COMMAND_STATUS_ENABLE_CHECKBOX = listOf(CommandStatusType.TO_DO.code, CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)

    val args: CommandDetailFragmentArgs by navArgs()
    private lateinit var checkboxTextViewAdapter: CheckboxListAdapter
    private val commandDetailedVM: CommandDetailedVM by viewModel{
        parametersOf(
            args.commandToDetailId
        )
    }

    private lateinit var binding : FragmentCommandDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommandDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentCommandId = args.commandToDetailId
        var currentCommand : Command? = null

        checkboxTextViewAdapter = CheckboxListAdapter(mutableListOf()).apply {
            onCheckedItem = { item, isChecked ->
                item.statusCode = if (isChecked) ArticleWrapperStatusType.DONE.code else ArticleWrapperStatusType.TO_DO.code
                commandDetailedVM.saveArticleWrapper(item)
            }

            onSwipeItem = { aw, position ->
                displayCancelOrDeleteArticle(aw, position)
            }
        }

        // Set the recyclerView
        binding.rvCommandArticles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = checkboxTextViewAdapter
        }

        // Swipe to cancel an article
        setupSwipeToDelete()

        /** --------------- Observers --------------- **/

        // Observe current command to detail
        commandDetailedVM.currentCommandLiveData().observe(viewLifecycleOwner) {
            Log.d("[Command details]","current commend observed for id $currentCommandId : $it"
            )
            if (it == null) { // Has been deleted
                goBack()
                return@observe
            }
            if(currentCommand == null) setupHeader()
            currentCommand = it

            Log.d("[Command details]", "current command AWs observed for command id $currentCommandId : ${it.articleWrappers}")
            checkboxTextViewAdapter.setItems(it.articleWrappers, it.statusCode in COMMAND_STATUS_ENABLE_CHECKBOX)

            // Update status
            binding.tvCommandStatus.text = getCommandStatusByCode(it.statusCode).label.uppercase()

            // Update total price
            binding.tvTotalPrice.text = getString(R.string.total_price, it.totalPrice.toString())

            Log.d("[Command details]", "${it.statusCode}")
            when (it.statusCode) {
                CommandStatusType.TO_DO.code -> { }
                CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code -> {
                    binding.btDelivered.visibility = VISIBLE
                    binding.btDelivered.isEnabled = true
                    binding.btCompletePayment.visibility = GONE
                    binding.tvError.visibility = GONE
                }
                CommandStatusType.DELIVERED.code -> {
                    binding.btDelivered.visibility = GONE
                    binding.btPay.visibility = VISIBLE
                    binding.btEditCommand.visibility = GONE
                    binding.btCompletePayment.visibility = GONE
                    binding.tvError.visibility = GONE
                }
                CommandStatusType.PAYED.code, CommandStatusType.CANCELED.code -> {
                    binding.btDelivered.visibility = GONE
                    binding.btPay.visibility = GONE
                    binding.btEditCommand.visibility = GONE
                    binding.btCompletePayment.visibility = GONE
                    binding.tvError.visibility = GONE
                }
                CommandStatusType.INCOMPLETE_PAYMENT.code -> {
                    binding.btDelivered.visibility = GONE
                    binding.btPay.visibility = GONE
                    binding.btEditCommand.visibility = GONE
                    binding.btCompletePayment.visibility = VISIBLE
                    binding.tvError.visibility = VISIBLE
                    binding.tvError.text = getString(R.string.uncomplete_payment_error, it.dueAmount)
                }
            }

            // Fill payment received tv if payed or incomplete
            if(it.statusCode == CommandStatusType.PAYED.code || it.statusCode == CommandStatusType.INCOMPLETE_PAYMENT.code) fillPaymentInfo(it)
        }

        /** --------------- On click listeners --------------- **/

        binding.btDeleteCommand.onClick {
            displayDeleteDialog()
        }

        binding.btEditCommand.onClick {
            displayAddCommandFragment(commandDetailedVM.currentCommandMutableLiveData.value)
        }

        binding.btDelivered.onClick {
            val articlesNotCanceled = commandDetailedVM.currentCommandMutableLiveData.value!!.articleWrappers.notCanceled()
            if(!articlesNotCanceled.map { it.statusCode }.any { it == ArticleWrapperStatusType.DONE.code}) {
                displayErrorDialog("")
            } else if (articlesNotCanceled.any { it.statusCode != ArticleWrapperStatusType.DONE.code }) {
                confirmUncompleteDeliveryDialog()
            } else {
                commandDetailedVM.updateStatusCommand(CommandStatusType.DELIVERED)
            }
        }

        binding.btPay.onClick { displayPayCommandFragment() }
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : SwipeToCancelCallback(requireContext()) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return true }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                checkboxTextViewAdapter.updateArticleStatus(viewHolder.absoluteAdapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvCommandArticles)
    }

    private fun fillPaymentInfo(it: Command) {
        binding.tvPaymentReceived.visibility = VISIBLE
        binding.tvPaymentReceived.text = getString(R.string.received_payment_amount, it.paymentAmount)

        if(it.reduction != 0) {
            binding.tvReduction.visibility = VISIBLE
            binding.tvReduction.text = getString(R.string.applied_reduction, it.reduction?.toInt())
        }
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle(
            getString(
                R.string.command_number_label,
                args.commandToDetailId
            ), getString(R.string.command_delivery_date_label, commandDetailedVM.currentCommandMutableLiveData.value!!.deliveryDate)
        )
        val mainActivity = requireActivity() as MainActivity
        mainActivity.replaceHeaderLogoByBackButton(true)
        binding.tvCommandClient.text = commandDetailedVM.currentCommandMutableLiveData.value!!.client?.toNameString()
    }

    private fun displayErrorDialog(message : String){
        displayDialog(
            context = requireContext(),
            titleRef = R.string.delivery_dialog_title,
            contentMessage = "Si aucun item n'a été réalisé, merci de supprimer la commande ou de la modifier pour la mettre à jour.",
            negativeButtonLabelRef = R.string.cancel,
            negativeAction = {}
        )
    }

    private fun confirmUncompleteDeliveryDialog() {
        displayDialog(
            context = requireContext(),
            titleRef = R.string.delivery_incomplete_command_title,
            contentRef = R.string.delivery_dialog_content,
            positiveButtonLabelRef = R.string.delivery,
            positiveAction = {
                commandDetailedVM.updateStatusCommand(CommandStatusType.DELIVERED)
                Snackbar.make(
                    requireView(), "La commande est bien passée au statut 'Livrée'.",
                    Snackbar.LENGTH_LONG
                ).show()
            },
            negativeButtonLabelRef = R.string.cancel,
            negativeAction = {}
        )
    }

    private fun displayDeleteDialog() {
        displayDialog(
            context = requireContext(),
            titleRef = R.string.delete_dialog_title,
            contentRef = R.string.delete_dialog_content,
            positiveButtonLabelRef = R.string.delete,
            positiveAction = {
                commandDetailedVM.removeCommand()
                Snackbar.make(
                    requireView(), "La commande a bien été supprimée.",
                    Snackbar.LENGTH_LONG
                ).show()
                goBack()
            },
            negativeButtonLabelRef = R.string.cancel,
            negativeAction = {}
        )
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun displayPayCommandFragment() {
        val mainActivity = activity as MainActivity
        mainActivity.displayPayCommandFragment(commandDetailedVM.currentCommandMutableLiveData.value!!.idCommand)
    }

    private fun displayCancelOrDeleteArticle(articleWrapper: ArticleWrapper, position: Int){
        displayDialog(
            context = requireContext(),
            titleRef = R.string.cancel_dialog_title,
            contentRef = R.string.cancel_dialog_content,
            negativeButtonLabelRef = R.string.cancel_article,
            negativeAction =  {
               checkboxTextViewAdapter.refreshRecyclerView(position)
            },
            positiveButtonLabelRef = R.string.delete_article,
            positiveAction = {
                commandDetailedVM.deleteArticleWrapperFromCurrentCommand(articleWrapper)
                //checkboxTextViewAdapter.onItemDelete(position)
            })
    }

    fun displayAddCommandFragment(currentCommand: Command?) {
        AddCommandDialogFragment.newInstance(currentCommand).show(requireActivity().supportFragmentManager, "dialog")
    }


}
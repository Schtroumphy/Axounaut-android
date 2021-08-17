package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.extensions.fullScreen
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.PaymentType
import com.jeanloth.project.android.kotlin.domain_models.entities.toNameString
import kotlinx.android.synthetic.main.fragment_pay_command_dialog.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import splitties.alertdialog.appcompat.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.onClick
import splitties.views.textColorResource


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddCommandDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class PayCommandDialogFragment(
    val commandId: Long
) : BottomSheetDialogFragment() {

    private val commandVM : CommandVM by sharedViewModel()

    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return bottomSheetDialog.fullScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pay_command_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        commandVM.currentCommandId = commandId
        Log.d("[Pay command fragment]", "Current command id : ${commandVM.currentCommandId}")


        commandVM.observeCurrentCommand()

        commandVM.currentCommandMutableLiveData.observe(viewLifecycleOwner){
            commandVM.currentCommand = it

            // Setup header
            tv_client_command_number.text = getString(
                R.string.command_details_client_number,
                it!!.client!!.toNameString(),
                it.idCommand.toString()
            )
            tv_total_command_price.text = getString(
                R.string.total_price_command_number_label,
                it.totalPrice.toString()
            )

            // Payment received
            cb_complete_payment.setOnCheckedChangeListener { _, isChecked ->
                et_payment_received.setText(if (isChecked) it.totalPrice.toString() else "")
            }
        }

        et_payment_received.doOnTextChanged { _, _, _, count ->
            bt_proceed_payment.isEnabled = count > 0
        }

        // Setup spinner
        val adapter: ArrayAdapter<PaymentType> = ArrayAdapter<PaymentType>(
            requireContext(), android.R.layout.simple_spinner_item, PaymentType.values()
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_payment_type.adapter = adapter
        spinner_payment_type.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                commandVM.currentCommand?.paymentTypeCode = adapter.getItem(position)?.code
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }

        // Close button
        bt_pay_close.onClick{
            bottomSheetDialog.dismiss()
        }

        bt_proceed_payment.onClick {
            commandVM.currentCommand?.apply {
                paymentAmount = et_payment_received.text.toString().toDouble()
                statusCode = if(et_payment_received.text.toString().toDouble() == commandVM.currentCommand!!.totalPrice) CommandStatusType.PAYED.code else CommandStatusType.INCOMPLETE_PAYMENT.code
            }

            if(et_payment_received.text.toString().toDouble() == commandVM.currentCommand!!.totalPrice) {
                commandVM.saveCommand(commandVM.currentCommand!!)
                bottomSheetDialog.dismiss()
                Snackbar.make(requireView(), "Paiement réussi", Snackbar.LENGTH_SHORT).show()
            } else {
                displayIncompletePaymentDialog(commandVM.currentCommand!!.totalPrice!!)
            }
        }

    }

    private fun displayIncompletePaymentDialog(incompletePaymentAmount : Double) {
        requireContext().materialAlertDialog {
            title = getString(R.string.dialog_incomplete_payment_title)
            message = getString(R.string.dialog_incomplete_payment_content, incompletePaymentAmount)
            positiveButton(R.string.confirm) {
                commandVM.saveCommand(commandVM.currentCommand!!)
                bottomSheetDialog.dismiss()
            }
            negativeButton(R.string.cancel) { dialog ->
                dialog.dismiss()
            }
        }.onShow {
            positiveButton.textColorResource = R.color.see_green
            negativeButton.textColorResource = R.color.gray_2
        }.show()
    }

    companion object {
        fun newInstance(commandId: Long) = PayCommandDialogFragment(commandId)
    }

}
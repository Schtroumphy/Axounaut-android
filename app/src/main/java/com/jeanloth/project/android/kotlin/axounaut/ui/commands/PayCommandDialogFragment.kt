package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
class PayCommandDialogFragment(
    val commandId: Long
) : BottomSheetDialogFragment() {

    private val commandVM : CommandVM by sharedViewModel()

    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return bottomSheetDialog.fullScreen()
    }
    var totalPrice : Double = 0.0

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
            //totalPrice = it.totalPrice ?: 0.0
        }

        commandVM.paymentReceivedLiveData().observe(viewLifecycleOwner){

            if(et_reduction.text!!.isNotEmpty())
                tv_total_price_with_reduction.visibility = if (et_reduction.text.toString().toInt() > 0) VISIBLE else INVISIBLE
            else tv_total_price_with_reduction.visibility = INVISIBLE

            tv_total_price_with_reduction.text = getString(R.string.total_price_command_after_reduction_label, it.toString())

            et_payment_received.setText(it.toString())
        }

        et_payment_received.doOnTextChanged { text, _, _, _ ->
            bt_proceed_payment.isEnabled = text!!.isNotEmpty() && text.toString().toDouble() > 0

            if(text.isNotEmpty()) {
                when(text.toString().toDouble()){
                    in 0.1 ..commandVM.paymentReceivedMutableLiveData.value!! - 0.1 -> {
                        cb_complete_payment.isChecked = false
                        et_reduction.isEnabled = true
                        tv_incomplete_payment.visibility = VISIBLE
                    }
                    commandVM.paymentReceivedMutableLiveData.value!! -> {
                        cb_complete_payment.isChecked = true
                        et_reduction.isEnabled = true
                        tv_incomplete_payment.visibility = INVISIBLE
                    }
                    else -> {
                        //cb_complete_payment.isChecked = false
                        tv_incomplete_payment.visibility = VISIBLE
                        // TODO Display other error
                    }
                }
               } else {
                et_reduction.isEnabled = false
                tv_incomplete_payment.visibility = VISIBLE
            }
        }


        // Payment received
        cb_complete_payment.setOnCheckedChangeListener { _, isChecked ->
            et_payment_received.setText(if (isChecked) commandVM.paymentReceivedMutableLiveData.value!!.toString() else "0.0")
            if (!isChecked) {
                tv_total_price_with_reduction.visibility = INVISIBLE
                et_reduction.setText("")
            }
            et_payment_received.setSelection(et_payment_received.text.toString().length)
        }

        // Setup spinner
        setupSpinner()

        et_reduction.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                updateTotalPriceWithReduction()
            }
            true
        }

        // Close button
        bt_pay_close.onClick{
            bottomSheetDialog.dismiss()
        }

        //Confirm payment
        bt_proceed_payment.onClick {
            commandVM.currentCommand?.apply {
                paymentAmount = commandVM.paymentReceivedLiveData().value
                reduction = if(et_reduction.text.toString().isNotEmpty()) et_reduction.text.toString().toDouble() else 0.0
                statusCode = if(
                    commandVM.paymentReceivedLiveData().value == commandVM.currentCommand!!.totalPrice ||
                    commandVM.paymentReceivedLiveData().value == commandVM.currentCommand!!.totalPrice.minus(et_reduction.text.toString().toDouble()))
                            CommandStatusType.PAYED.code else CommandStatusType.INCOMPLETE_PAYMENT.code
            }

            commandVM.saveCommand(commandVM.currentCommand!!)
            bottomSheetDialog.dismiss()

            // TODO Send parameter to previous fragment to display this snackbar
            Snackbar.make(requireView(), "Paiement pris en compte.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
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
                et_payment_received.clearFocus()
                et_reduction.clearFocus()
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                et_payment_received.clearFocus()
                et_reduction.clearFocus()
            }
        }
    }

    private fun updateTotalPriceWithReduction() {
        if(et_reduction.text.toString().toInt() != 0){
            commandVM.setPaymentReceived(computeWithReduction(totalPrice))
        }
    }

    private fun computeWithReduction(price : Double): Double = price - et_reduction.text.toString().toInt()

    companion object {
        fun newInstance(commandId: Long) = PayCommandDialogFragment(commandId)
    }

}
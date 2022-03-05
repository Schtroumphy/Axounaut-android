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
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentPayCommandDialogBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.fullScreen
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.PaymentType
import com.jeanloth.project.android.kotlin.domain_models.entities.toNameString
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
    private lateinit var binding: FragmentPayCommandDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayCommandDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        commandVM.currentCommandId = commandId
        Log.d("[Pay command fragment]", "Current command id : ${commandVM.currentCommandId}")

        //commandVM.observeCurrentCommand()

        commandVM.currentCommandMutableLiveData.observe(viewLifecycleOwner){
            // Setup header
            binding.tvClientCommandNumber.text = getString(
                R.string.command_details_client_number,
                it!!.client!!.toNameString(),
                it.idCommand.toString()
            )
            binding.tvTotalCommandPrice.text = getString(
                R.string.total_price_command_number_label,
                it.totalPrice.toString()
            )
            //totalPrice = it.totalPrice ?: 0.0
        }

        commandVM.paymentReceivedLiveData().observe(viewLifecycleOwner){

            if(binding.etReduction.text!!.isNotEmpty())
                binding.tvTotalPriceWithReduction.visibility = if (binding.etReduction.text.toString().toInt() > 0) VISIBLE else INVISIBLE
            else binding.tvTotalPriceWithReduction.visibility = INVISIBLE

            binding.tvTotalPriceWithReduction.text = getString(R.string.total_price_command_after_reduction_label, it.toString())

            binding.etPaymentReceived.setText(it.toString())
        }

        binding.etPaymentReceived.doOnTextChanged { text, _, _, _ ->
            binding.btProceedPayment.isEnabled = text!!.isNotEmpty() && text.toString().toDouble() > 0

            if(text.isNotEmpty()) {
                when(text.toString().toDouble()){
                    in 0.1 ..commandVM.paymentReceivedMutableLiveData.value!! - 0.1 -> {
                        binding.cbCompletePayment.isChecked = false
                        binding.etReduction.isEnabled = true
                        binding.tvIncompletePayment.visibility = VISIBLE
                    }
                    commandVM.paymentReceivedMutableLiveData.value!! -> {
                        binding.cbCompletePayment.isChecked = true
                        binding.etReduction.isEnabled = true
                        binding.tvIncompletePayment.visibility = INVISIBLE
                    }
                    else -> {
                        //cb_complete_payment.isChecked = false
                        binding.tvIncompletePayment.visibility = VISIBLE
                        // TODO Display other error
                    }
                }
               } else {
                binding.etReduction.isEnabled = false
                binding.tvIncompletePayment.visibility = VISIBLE
            }
        }

        // Payment received
        binding.cbCompletePayment.setOnCheckedChangeListener { _, isChecked ->
            binding.etPaymentReceived.setText(if (isChecked) commandVM.paymentReceivedMutableLiveData.value!!.toString() else "0.0")
            if (!isChecked) {
                binding.tvTotalPriceWithReduction.visibility = INVISIBLE
                binding.etReduction.setText("")
            }
            binding.etPaymentReceived.setSelection(binding.etPaymentReceived.text.toString().length)
        }

        // Setup spinner
        setupSpinner()

        binding.etReduction.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                updateTotalPriceWithReduction()
            }
            true
        }

        // Close button
        binding.btPayClose.onClick{
            bottomSheetDialog.dismiss()
        }

        //Confirm payment
        binding.btProceedPayment.onClick {
            commandVM.currentCommand?.apply {
                paymentAmount = commandVM.paymentReceivedLiveData().value
                reduction = if(binding.etReduction.text.toString().isNotEmpty()) binding.etReduction.text.toString().toDouble() else 0.0
                statusCode = if(
                    commandVM.paymentReceivedLiveData().value == commandVM.currentCommand!!.totalPrice ||
                    commandVM.paymentReceivedLiveData().value == commandVM.currentCommand!!.totalPrice.minus(binding.etReduction.text.toString().toDouble()))
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
        binding.spinnerPaymentType.adapter = adapter
        binding.spinnerPaymentType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                commandVM.currentCommand?.paymentTypeCode = adapter.getItem(position)?.code
                binding.etPaymentReceived.clearFocus()
                binding.etReduction.clearFocus()
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                binding.etPaymentReceived.clearFocus()
                binding.etReduction.clearFocus()
            }
        }
    }

    private fun updateTotalPriceWithReduction() {
        if(binding.etReduction.text.toString().toInt() != 0){
            commandVM.setPaymentReceived(computeWithReduction(totalPrice))
        }
    }

    private fun computeWithReduction(price : Double): Double = price - binding.etReduction.text.toString().toInt()

    companion object {
        fun newInstance(commandId: Long) = PayCommandDialogFragment(commandId)
    }

}
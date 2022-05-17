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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentPayCommandDialogBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.fullScreen
import com.jeanloth.project.android.kotlin.axounaut.extensions.hideKeyboard
import com.jeanloth.project.android.kotlin.axounaut.viewModels.PayCommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.PaymentType
import com.jeanloth.project.android.kotlin.domain_models.entities.toNameString
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
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
    private val commandId: Long
) : BottomSheetDialogFragment() {

    private val payCommandVM : PayCommandVM by viewModel{
        parametersOf(
            commandId
        )
    }

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var binding: FragmentPayCommandDialogBinding
    private var paymentIsComplete : Boolean = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return bottomSheetDialog.fullScreen()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayCommandDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // TODO Monitor error display by enum type
    enum class ErrorType(message: String){ // Todo convert to int : reference to strings.xml
        NO_REDUCTION_ENTERED("Vous n'avez saisi aucune réduction"),
        NO_PAYMENT_RECEIVED("Vous n'avez saisi aucun payment reçu"),
        INCOMPLETE_PAYMENT("Le paiement est incomplet. En procédant...")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d("[Pay command fragment]", "Current command id : ${payCommandVM.commandId}")

        payCommandVM.currentCommandLiveData().observe(viewLifecycleOwner){
            binding.tvClientCommandNumber.text = getString(
                R.string.command_details_client_number,
                it?.client?.toNameString(),
                it?.idCommand.toString()
            )
            binding.tvTotalCommandPrice.text = getString(
                R.string.total_price_command_number_label,
                it?.totalPrice.toString()
            )
            payCommandVM.setCurrentTotalPrice(it?.totalPrice ?: 0)
        }

        binding.rgReduction.setOnCheckedChangeListener { _, i ->
            binding.tilReduction.visibility = if(i == binding.rbReductionNo.id) GONE else VISIBLE
            if(i == binding.rbReductionNo.id){
                payCommandVM.setReduction(0)
                binding.etReduction.setText("")
            }
        }

        binding.rgPayment.setOnCheckedChangeListener { _, i ->
            binding.tilPaymentReceived.visibility = if(i == binding.rbPaymentYes.id) GONE else VISIBLE
            if(i == binding.rbPaymentYes.id){
                payCommandVM.setPaymentComplete()
                paymentIsComplete = true
                binding.etPaymentReceived.setText("")
                binding.tvIncompletePayment.visibility = GONE
            }
        }

        // Listen to reduction enter
        binding.etReduction.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                hideKeyboard()
                binding.etReduction.clearFocus()
                updateTotalPriceWithReduction()
            }
            true
        }

        // Listen to total price with reduction
        payCommandVM.totalPriceWithReductionLiveData().observe(viewLifecycleOwner){
            if(it != 0 && it != payCommandVM.totalPrice){
                binding.rbPaymentYes.isChecked = true // Check complete payment to force update of payment received to total price
                binding.tvTotalPriceWithReduction.visibility = VISIBLE
                binding.tvTotalPriceWithReduction.text = getString(R.string.total_price_command_after_reduction_label, it.toString())
            } else {
                binding.tvTotalPriceWithReduction.visibility = GONE
            }
        }

        payCommandVM.statusAndPaymentReceivedLiveData().observe(viewLifecycleOwner){
            if(it.first != PayCommandVM.PaymentStatus.UNKONWN && binding.etPaymentReceived.text?.isNotBlank() == true) binding.tvIncompletePayment.visibility =  VISIBLE
            when(it.first){
                PayCommandVM.PaymentStatus.COMPLETE -> {
                    binding.tvIncompletePayment.text = "Le paiement est complet, le choix a été modifié en conséquence."
                    binding.btProceedPayment.isEnabled = true
                    binding.rbPaymentYes.isChecked = true
                }
                PayCommandVM.PaymentStatus.UNCOMPLETE -> {
                    binding.tvIncompletePayment.text = getString(R.string.incomplete_payment_error)
                    binding.btProceedPayment.isEnabled = true
                }
                PayCommandVM.PaymentStatus.SUPERIOR -> {
                    binding.tvIncompletePayment.text = "Le paiement est supérieur à celui attendu après réduction."
                    binding.btProceedPayment.isEnabled = false
                }
            }
        }

        // Payment received listener
        binding.etPaymentReceived.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                hideKeyboard()
                binding.etPaymentReceived.clearFocus()
                updatePaymentReceived()
            }
            true
        }

        // Setup spinner
        setupSpinner()

        // Close button
        binding.btPayClose.onClick{
            bottomSheetDialog.dismiss()
        }

        //Confirm payment
        binding.btProceedPayment.onClick {
            payCommandVM.saveCommand()
            bottomSheetDialog.dismiss()

            // TODO Send parameter to previous fragment to display this snack bar
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
                payCommandVM.currentCommand?.paymentTypeCode = adapter.getItem(position)?.code ?: PaymentType.CASH.code
                binding.etPaymentReceived.clearFocus()
                binding.etReduction.clearFocus()
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                binding.etPaymentReceived.clearFocus()
                binding.etReduction.clearFocus()
            }
        }
    }

    private fun updatePaymentReceived() {
        if(binding.etPaymentReceived.text.toString().toInt() <= 0 ) {
            // TODO Display an error : NO_PAYMENT_RECEIVED
            return
        }
        payCommandVM.setPaymentReceived(binding.etPaymentReceived.text.toString().toInt())
    }

    private fun updateTotalPriceWithReduction() {
        if(binding.etReduction.text.toString().toInt() <= 0 ) {
            // TODO Display an error : NO_REDUCTION_ENTERED
            return
        }
        payCommandVM.setReduction(binding.etReduction.text.toString().toInt())
    }

    companion object {
        fun newInstance(commandId: Long) = PayCommandDialogFragment(commandId)
    }

}
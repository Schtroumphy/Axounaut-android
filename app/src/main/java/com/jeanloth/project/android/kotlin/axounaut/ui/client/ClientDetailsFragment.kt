package com.jeanloth.project.android.kotlin.axounaut.ui.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.extensions.isPhoneValid
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import kotlinx.android.synthetic.main.fragment_client_details.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ClientDetailsFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val clientVM : ClientVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Ajouter un client")

        bt_add_client.setOnClickListener {
            addClient()
        }
    }

    private fun addClient() {
        val clientName = et_client_firstname.text.toString()
        val clientLastname = et_client_lastname.text.toString()
        val clientPhoneNumber = et_client_phone_number.text.toString()
        val clientFidelity = et_client_fidelity.text.toString()

        if( clientName.isEmpty() || !clientPhoneNumber.isPhoneValid()) {
            Snackbar.make(requireView(), "Veuillez saisir des valeurs valides.",
                Snackbar.LENGTH_LONG).show()
        } else {
            val clientToAdd = AppClient(
                firstname = clientName,
                lastname = clientLastname,
                phoneNumber = clientPhoneNumber.toInt(),
                fidelityPoint = if(clientFidelity.isEmpty()) 0 else clientFidelity.toInt()
            )
            Log.d("[Article Details Fragment", "Client to add : $clientToAdd")
            clientVM.saveClient(clientToAdd)
            Snackbar.make(requireView(), "Client ajouté avec succès.",
                Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

}
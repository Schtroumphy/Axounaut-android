package com.jeanloth.project.android.kotlin.axounaut.ui.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentClientDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.isPhoneValid
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ClientDetailsFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val clientVM : ClientVM by viewModel()

    private lateinit var binding: FragmentClientDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Ajouter un client")

        binding.btAddClient.setOnClickListener {
            addClient()
        }
    }

    private fun addClient() {
        val clientName = binding.etClientFirstname.text.toString()
        val clientLastname = binding.etClientLastname.text.toString()
        val clientPhoneNumber = binding.etClientPhoneNumber.text.toString()
        val clientFidelity = binding.etClientFidelity.text.toString()

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
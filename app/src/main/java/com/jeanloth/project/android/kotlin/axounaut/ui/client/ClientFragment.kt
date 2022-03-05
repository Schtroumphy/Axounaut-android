package com.jeanloth.project.android.kotlin.axounaut.ui.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ClientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentClientBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick

/**
 * A simple [Fragment] subclass.
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val clientVM : ClientVM by viewModel()
    private lateinit var clientAdapter: ClientAdapter

    private var clientListSelected : MutableList<AppClient> = mutableListOf()

    private lateinit var binding: FragmentClientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Clients")

        clientAdapter = ClientAdapter(emptyList(), requireContext()).apply {
            onCheckboxClick = { client, isSelected ->
                // Update selected clients list
                updateSelectedClientList(client, isSelected)
            }

            onFavoriteClick = {
                it.isFavorite = !it.isFavorite
                clientVM.saveClient(it)
            }

            onPhoneClick = {
                callClient(it)
            }

        }
        binding.rvClients.adapter = clientAdapter

        lifecycleScope.launchWhenStarted {
            clientVM.allClientsLiveData().observe(viewLifecycleOwner){
                Log.d("[Client Fragment", "Client observed : $it")
                clientAdapter.setItems(it)

                binding.tvNoClientError.visibility = if(it.isEmpty()) VISIBLE else GONE
            }
        }

        binding.clAddClientBtn.setOnClickListener {
            // Go to ClientDetailFragment
            goToClientDetails()
        }

        binding.btnDeleteClient.onClick {
            updateActionButtonDisplay(false)
            clientVM.deleteClients(clientListSelected)
            Snackbar.make(
                requireView(), resources.getQuantityString(
                    R.plurals.adding_client_toast,
                    clientListSelected.size,
                    clientListSelected.size
                ),
                Snackbar.LENGTH_LONG
            ).show()
            clientListSelected.clear()
        }
    }

    private fun callClient(phoneNumber: Int?) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phoneNumber!!.toString(), null)))
    }

    private fun updateActionButtonDisplay(isEditMode: Boolean) {
        binding.btnDeleteClient.visibility =  if(!isEditMode) VISIBLE else GONE
        binding.tvBtnAddClient.visibility =  if(!isEditMode) GONE else VISIBLE
    }

    private fun updateSelectedClientList(client: AppClient, selected: Boolean) {
        if(selected){
            if(!clientListSelected.contains(client)){
                clientListSelected.add(client)
            }
        } else {
            if(clientListSelected.contains(client)){
                clientListSelected.remove(client)
            }
        }
        Log.d("[Client Fragment]", "Clients selected : $clientListSelected")

        updateActionButtonDisplay(clientListSelected.isEmpty())

    }

    private fun goToClientDetails() {
        findNavController().navigate(ClientFragmentDirections.actionNavClientsToNavClientsDetails())
    }

}
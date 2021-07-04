package com.jeanloth.project.android.kotlin.axounaut.ui.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ClientAdapter
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.layout_header.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val clientVM : ClientVM by viewModel()
    private lateinit var clientAdapter: ClientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Clients")

        clientAdapter = ClientAdapter(emptyList(), requireContext()).apply {
            onClick = {
                // Go to client details
            }

            onFavoriteClick = {
                it.isFavorite = !it.isFavorite
                clientVM.saveClient(it)
            }

        }
        rv_clients.adapter = clientAdapter

        lifecycleScope.launchWhenStarted {
            clientVM.allClientsLiveData().observe(viewLifecycleOwner){
                Log.d("[Client Fragment", "Client observed : $it")
                clientAdapter.setItems(it)
            }
        }

        setupHeader()

        cl_add_client_btn.setOnClickListener {
            // Go to ClientDetailFragment
            goToClientDetails()
        }
    }

    private fun goToClientDetails() {
        findNavController().navigate(ClientFragmentDirections.actionNavClientsToNavClientsDetails())
    }

    private fun setupHeader() {
        tv_title.text = "Mes clients"
        tv_subtitle.visibility = View.GONE

        bt_previous_or_close.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
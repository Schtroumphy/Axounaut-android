package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentCommandListBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandDisplayMode
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.views.onClick

/**
 * A fragment representing a list of Command.
 */
class CommandListFragment : Fragment() {

    private val mainVM: MainVM by sharedViewModel()
    private val commandVM: CommandVM by viewModel { parametersOf(0L) }
    private lateinit var commandAdapter: CommandAdapter

    private lateinit var binding : FragmentCommandListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commandVM.retrieveSaveMode()

        // Set the adapter + recyclerView
        commandAdapter = CommandAdapter(emptyList(), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommandListBinding.inflate(layoutInflater, container, false)
        setupHeader()

        // Observe live data
        commandVM.commandToDisplayMediatorLiveData.observe(viewLifecycleOwner) {
            Log.d("[Command list Fragment", "Command observed : ${it.size}")
            commandAdapter.setItems(it)
            binding.tvErrorNoCommands.visibility = if (it.isEmpty()) VISIBLE else GONE
            if(it.isEmpty()){
                binding.tvErrorNoCommands.text = getString( when(commandVM.displayModeMutableLiveData.value) {
                    CommandDisplayMode.IN_PROGRESS -> R.string.no_loading_command_error
                    CommandDisplayMode.TO_COME -> R.string.no_to_come_command_error
                    CommandDisplayMode.PAST -> R.string.no_command_error
                    else -> R.string.no_loading_command_error
                })
            }
            commandVM.displayModeMutableLiveData.value?.let { mode ->
                when(mode) {
                    CommandDisplayMode.IN_PROGRESS -> binding.fabLoading.changeFabColors()
                    CommandDisplayMode.TO_COME -> binding.fabComing.changeFabColors()
                    CommandDisplayMode.PAST -> binding.fabAll.changeFabColors()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCommandList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commandAdapter.apply {
                onClick = {
                    // Command to detail
                    goToCommandDetails(it)
                }
            }
        }

        binding.fabAll.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.PAST)
                changeFabColors()
            }
        }

        binding.fabComing.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.TO_COME)
                changeFabColors()
            }
        }

        binding.fabLoading.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.IN_PROGRESS)
                changeFabColors()
            }
        }
    }

    private fun FloatingActionButton.changeFabColors() {
        // Make all gray before
        binding.fabLoading.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))
        binding.fabComing.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))
        binding.fabAll.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))

        // Change color for the activated one
        this.imageTintList= ColorStateList.valueOf(getColor(context, R.color.ginger))
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Commandes")
        val mainActivity = requireActivity() as MainActivity
        mainActivity.replaceHeaderLogoByBackButton(false)
    }

    private fun goToCommandDetails(commandId: Long) {
        findNavController().navigate(
            CommandListFragmentDirections.actionNavCommandListToNavCommandDetails(
                commandToDetailId = commandId
            )
        )
    }
}
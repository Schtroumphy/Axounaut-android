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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentCommandListBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
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

    enum class CommandDisplayMode(val statusCode: List<Int>) {
        IN_PROGRESS(listOf(CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)),
        TO_COME(listOf(CommandStatusType.TO_DO.code)),
        PAST(listOf(
                CommandStatusType.DONE.code,
                CommandStatusType.PAYED.code,
                CommandStatusType.DELIVERED.code,
                CommandStatusType.INCOMPLETE_PAYMENT.code,
            )
        ),
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommandListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        // TODO Save in prefs the last filter on commands list and of no value,
        // set display mode to past by default
        commandVM.setDisplayMode(CommandDisplayMode.PAST)

        // Set the adapter
        binding.rvCommandList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commandAdapter.apply {
                onClick = {
                    // Command to detail
                    goToCommandDetails(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            commandVM.commandToDisplayMediatorLiveData.observe(viewLifecycleOwner) {
                Log.d("[Command list Fragment", "Command observed : ${it.size}")
                commandAdapter.setItems(it)
                binding.tvErrorNoCommands.visibility = if (it.isEmpty()) VISIBLE else GONE
                if (it.isEmpty()) binding.tvErrorNoCommands.text = getString(
                    when (commandVM.displayModeMutableLiveData.value) {
                        CommandDisplayMode.IN_PROGRESS -> R.string.no_loading_command_error
                        CommandDisplayMode.TO_COME -> R.string.no_to_come_command_error
                        CommandDisplayMode.PAST -> R.string.no_command_error
                        else -> R.string.no_loading_command_error
                    }
                )
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
        commandAdapter = CommandAdapter(emptyList(), requireContext())
    }

    private fun goToCommandDetails(commandId: Long) {
        findNavController().navigate(
            CommandListFragmentDirections.actionNavCommandListToNavCommandDetails(
                commandToDetailId = commandId
            )
        )
    }
}
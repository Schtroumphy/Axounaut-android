package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.android.synthetic.main.fragment_command_list.*
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

    enum class CommandDisplayMode(val statusCode: List<Int>) {
        IN_PROGRESS(listOf(CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)),
        TO_COME(listOf(CommandStatusType.TO_DO.code)),
        PAST(
            listOf(
                CommandStatusType.DONE.code,
                CommandStatusType.PAYED.code,
                CommandStatusType.CANCELED.code,
                CommandStatusType.DELIVERED.code,
                CommandStatusType.INCOMPLETE_PAYMENT.code,
            )
        ),
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_command_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        // TODO Save in prefs the last filter on commands list

        // Set the adapter
        rv_command_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commandAdapter.apply {
                onClick = {
                    // Command to detail
                    goToCommandDetails(it)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            commandVM.commandToDisplayMediatorLiveData.observe(viewLifecycleOwner) {
                Log.d("[Article Fragment", "Article observed : $it")

                commandAdapter.setItems(it)
                tv_error_no_commands.visibility = if (it.isEmpty()) VISIBLE else GONE
                tv_error_no_commands.text = getString(
                    when (commandVM.displayModeMutableLiveData.value) {
                        CommandDisplayMode.IN_PROGRESS -> R.string.no_loading_command_error
                        CommandDisplayMode.TO_COME -> R.string.no_to_come_command_error
                        CommandDisplayMode.PAST -> R.string.no_command_error
                        else -> R.string.no_loading_command_error
                    }
                )
            }
        }

        fab_all.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.PAST)
                changeFabColors()
            }
        }

        fab_coming.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.TO_COME)
                changeFabColors()
            }
        }

        fab_loading.apply {
            onClick {
                commandVM.setDisplayMode(CommandDisplayMode.IN_PROGRESS)
                changeFabColors()
            }
        }
    }

    private fun FloatingActionButton.changeFabColors() {
        // Make all gray before
        fab_loading.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))
        fab_coming.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))
        fab_all.imageTintList= ColorStateList.valueOf(getColor(context, R.color.gray_1))

        // Change color for the activated one
        this.imageTintList= ColorStateList.valueOf(getColor(context, R.color.ginger))
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Commandes")
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideOrShowMenuButton(true)
        mainActivity.replaceHeaderLogoByBackButton(false)
        commandAdapter = CommandAdapter(emptyList(), requireContext())
    }

    private fun goToCommandDetails(command: Command) {
        Log.d("TAG", "Command to details : $command")
        findNavController().navigate(
            CommandListFragmentDirections.actionNavCommandListToNavCommandDetails(
                commandToDetail = command
            )
        )
    }
}
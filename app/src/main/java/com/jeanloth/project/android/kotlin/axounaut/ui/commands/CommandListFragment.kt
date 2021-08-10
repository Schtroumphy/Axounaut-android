package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.convertToLocalDate
import com.jeanloth.project.android.kotlin.axounaut.mock.DataMock
import com.jeanloth.project.android.kotlin.axounaut.ui.home.HomeFragmentDirections
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.android.synthetic.main.fragment_command_list.*
import kotlinx.android.synthetic.main.fragment_command_list.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.views.onClick
import java.time.LocalDate

/**
 * A fragment representing a list of Command.
 */
class CommandListFragment(
    var displayMode: CommandDisplayMode
) : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val commandVM : CommandVM by viewModel{
        parametersOf(
            0L
        )
    }
    private lateinit var commandAdapter: CommandAdapter

    enum class CommandDisplayMode(val statusCode : List<Int>){
        IN_PROGRESS(listOf(CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)),
        TO_COME(listOf(CommandStatusType.TO_DO.code)),
        PAST(listOf( CommandStatusType.DONE.code, CommandStatusType.PAYED.code, CommandStatusType.CANCELED.code, CommandStatusType.DELIVERED.code)),
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_command_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Commandes")
        (activity as MainActivity).hideOrShowMenuButton(true)
        commandAdapter = CommandAdapter(emptyList(), requireContext())

        // Set the adapter
        rv_command_list.apply{
                layoutManager = LinearLayoutManager(context)
                adapter = commandAdapter.apply {
                    onClick = {
                        // Command to detail
                        goToCommandDetails(it)
                    }
                }
            }

        lifecycleScope.launchWhenStarted {
            commandVM.allCommandsLiveData().observe(viewLifecycleOwner){
                Log.d("[Article Fragment", "Article observed : $it")
                var commandToDisplay = it.filter {
                    it.statusCode in displayMode.statusCode
                }.sortedBy { it.deliveryDate?.convertToLocalDate()}

                if(displayMode == CommandDisplayMode.IN_PROGRESS ){
                    commandToDisplay = commandToDisplay.filter{ it.deliveryDate?.convertToLocalDate() == LocalDate.now()}
                }

                commandAdapter.setItems( commandToDisplay)

                tv_error_no_commands.visibility = if(commandToDisplay.isEmpty()) VISIBLE else GONE

                tv_error_no_commands.text = getString(
                    when(displayMode){
                    CommandDisplayMode.IN_PROGRESS -> R.string.no_loading_command_error
                    CommandDisplayMode.TO_COME -> R.string.no_to_come_command_error
                    CommandDisplayMode.PAST -> R.string.no_command_error
                }
                )
            }
        }

        tv_error_no_commands.onClick {
            Snackbar.make(requireView(), "Test snackbar.",
                Snackbar.LENGTH_SHORT).show()
        }

        fab_add_command.onClick {
            (activity as MainActivity).displayAddCommandFragment()
        }
    }

    private fun goToCommandDetails(command: Command) {
        Log.d("TAG", "Command to details : $command")
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCommandDetails(
            commandToDetail = command
        ))
    }
}
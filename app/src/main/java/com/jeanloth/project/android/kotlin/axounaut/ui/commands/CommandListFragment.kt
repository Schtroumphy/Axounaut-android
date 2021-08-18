package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.toLocalDate
import com.jeanloth.project.android.kotlin.axounaut.ui.home.HomeFragmentDirections
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType.Companion.getCommandStatusByLabel
import com.jeanloth.project.android.kotlin.domain_models.entities.PeriodType
import com.jeanloth.project.android.kotlin.domain_models.entities.PeriodType.Companion.getPeriodTypeFromLabel
import kotlinx.android.synthetic.main.fragment_command_list.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.views.onClick
import java.time.DayOfWeek
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
    private lateinit var allPastCommands: List<Command>
    private lateinit var allPastCommandsFilteredByPeriod: List<Command>

    enum class CommandDisplayMode(val statusCode: List<Int>){
        IN_PROGRESS(listOf(CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)),
        TO_COME(listOf(CommandStatusType.TO_DO.code)),
        PAST(
            listOf(
                CommandStatusType.DONE.code,
                CommandStatusType.PAYED.code,
                CommandStatusType.CANCELED.code,
                CommandStatusType.DELIVERED.code
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

                var commandToDisplay = emptyList<Command>()

                if(displayMode == CommandDisplayMode.IN_PROGRESS ){
                    commandToDisplay = it.filter{ it.statusCode in displayMode.statusCode || it.deliveryDate?.toLocalDate()!! == LocalDate.now()}
                } else if(displayMode == CommandDisplayMode.TO_COME){
                    commandToDisplay = it.filter{ it.statusCode in displayMode.statusCode && it.deliveryDate?.toLocalDate()!!.isAfter(
                        LocalDate.now()
                    )}
                } else {
                    commandToDisplay =  it.filter {
                        it.statusCode in displayMode.statusCode
                    }.sortedBy { it.deliveryDate?.toLocalDate()}
                    allPastCommands = commandToDisplay
                    allPastCommandsFilteredByPeriod = commandToDisplay
                }
                commandAdapter.setItems(commandToDisplay)

                tv_error_no_commands.visibility = if(commandToDisplay.isEmpty()) VISIBLE else GONE

                tv_error_no_commands.text = getString(
                    when (displayMode) {
                        CommandDisplayMode.IN_PROGRESS -> R.string.no_loading_command_error
                        CommandDisplayMode.TO_COME -> R.string.no_to_come_command_error
                        CommandDisplayMode.PAST -> R.string.no_command_error
                    }
                )
            }
        }

        tv_error_no_commands.onClick {
            Snackbar.make(
                requireView(), "Test snackbar.",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        fab_add_command.onClick {
            (activity as MainActivity).displayAddCommandFragment()
        }

        setupFilters()
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Commandes")
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideOrShowMenuButton(true)
        mainActivity.replaceHeaderLogoByBackButton(false)
        commandAdapter = CommandAdapter(emptyList(), requireContext())
        cl_command_filters.visibility =
            if (displayMode == CommandDisplayMode.PAST) VISIBLE else GONE
    }

    private fun setupFilters() {
        val periodAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            PeriodType.values().map { it.label }
        )

        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_command_period.adapter = periodAdapter
        spinner_command_period.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinner_command_status.setSelection(0)
                allPastCommandsFilteredByPeriod = filterByPeriod(allPastCommandsFilteredByPeriod, periodAdapter.getItem(position)!!.getPeriodTypeFromLabel())
                commandAdapter.setItems(allPastCommandsFilteredByPeriod)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val statusAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CommandStatusType.values().map { it.label }
        )

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_command_status.adapter = statusAdapter
        spinner_command_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val commandsFilteredByStatus: List<Command>
                if(getCommandStatusByLabel(statusAdapter.getItem(position)!!) != CommandStatusType.ALL){
                    commandsFilteredByStatus = allPastCommandsFilteredByPeriod.filter {
                        it.statusCode == getCommandStatusByLabel(statusAdapter.getItem(position)!!).code
                    }
                } else {
                    commandsFilteredByStatus = allPastCommands
                }
                commandAdapter.setItems(commandsFilteredByStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun filterByPeriod(commands : List<Command>, periodType: PeriodType): List<Command> {

        val now = LocalDate.now()

        val firstDay = when(periodType.label){
            PeriodType.THIS_WEEK.label -> now.with(DayOfWeek.MONDAY)
            PeriodType.LAST_WEEK.label -> now.with(DayOfWeek.MONDAY).minusDays(7)
            PeriodType.THIS_MONTH.label -> now.withDayOfMonth(1)
            PeriodType.LAST_MONTH.label -> now.withDayOfMonth(1).minusDays(now.lengthOfMonth().toLong())
            PeriodType.THIS_YEAR.label -> now.withDayOfYear(1)
            PeriodType.LAST_YEAR.label -> now.withDayOfYear(1).minusDays(now.lengthOfYear().toLong())
            else -> null
        }

        val lastDay = when(periodType.label){
            PeriodType.THIS_WEEK.label -> now.with(DayOfWeek.SUNDAY)
            PeriodType.LAST_WEEK.label -> now.with(DayOfWeek.SUNDAY).minusDays(7)
            PeriodType.THIS_MONTH.label -> now.withDayOfMonth(now.lengthOfMonth())
            PeriodType.LAST_MONTH.label -> now.withDayOfMonth(now.lengthOfMonth()).minusDays(now.lengthOfMonth().toLong())
            PeriodType.THIS_YEAR.label -> now.withDayOfYear(now.lengthOfYear())
            PeriodType.LAST_YEAR.label -> now.withDayOfYear(now.lengthOfYear()).minusDays(now.lengthOfYear().toLong())
            else -> null
        }
        if(firstDay == null || lastDay == null) return allPastCommands

        return allPastCommands.filter {
            it.deliveryDate!!.toLocalDate().isAfter(firstDay) && it.deliveryDate!!.toLocalDate().isBefore(lastDay)
        }
    }

    private fun goToCommandDetails(command: Command) {
        Log.d("TAG", "Command to details : $command")
        findNavController().navigate(
            HomeFragmentDirections.actionNavHomeToNavCommandDetails(
                commandToDetail = command
            )
        )
    }
}
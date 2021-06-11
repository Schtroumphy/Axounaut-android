package com.jeanloth.project.android.kotlin.axounaut.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CommandAdapter
import com.jeanloth.project.android.kotlin.axounaut.mock.DataMock
import com.jeanloth.project.android.kotlin.axounaut.ui.dummy.DummyContent
import com.jeanloth.project.android.kotlin.axounaut.ui.home.HomeFragmentDirections
import com.jeanloth.project.android.kotlin.domain.entities.Command
import kotlinx.android.synthetic.main.fragment_command_list.*
import kotlinx.android.synthetic.main.fragment_command_list.view.*

/**
 * A fragment representing a list of Command.
 */
class CommandListFragment(
    var displayMode: CommandDisplayMode
    ) : Fragment() {

    enum class CommandDisplayMode{
        IN_PROGRESS,
        TO_COME,
        PAST
    }

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_command_list, container, false)
        view.tv_command_list_title.text = requireContext().resources.getString(
            when(displayMode) {
                CommandDisplayMode.IN_PROGRESS -> R.string.command_to_do
                CommandDisplayMode.TO_COME -> R.string.command_future
                CommandDisplayMode.PAST -> R.string.command_past
            })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the adapter
        rv_command_list.apply{
                layoutManager = LinearLayoutManager(context)
                adapter = CommandAdapter(listOf(DataMock.command1, DataMock.command2)).apply {
                    onClick = {
                        // Command to detail
                        goToCommandDetails(it)
                    }
                }
            }
        }

    private fun goToCommandDetails(command: Command) {
        Log.d("TAG", "Command : $command")
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCommandDetails(
            commandToDetail = command
        ))
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CommandListFragment(CommandDisplayMode.IN_PROGRESS).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
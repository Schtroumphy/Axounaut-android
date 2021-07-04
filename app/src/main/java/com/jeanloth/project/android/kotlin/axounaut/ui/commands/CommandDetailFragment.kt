package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxListAdapter
import kotlinx.android.synthetic.main.fragment_command_detail.*
import kotlinx.android.synthetic.main.layout_header.*

/**
 * A simple [Fragment] subclass.
 * Use the [CommandDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommandDetailFragment : Fragment() {

    val args: CommandDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_previous_or_close.background = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_left_arrow)

        tv_title.text = "Commande n°12345"
        tv_subtitle.text = "Adrien DELONNE"

        bt_previous_or_close.setOnClickListener {
            findNavController().popBackStack()
        }

        // Set the adapter
        rv_command_articles.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = CheckboxListAdapter(args.commandToDetail.articleWrappers).apply {

            }
        }
    }

}
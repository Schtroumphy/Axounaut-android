package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxListAdapter
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.toNameString
import kotlinx.android.synthetic.main.fragment_command_detail.*
import kotlinx.android.synthetic.main.layout_header.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.alertdialog.appcompat.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.onClick
import splitties.views.textColorResource

/**
 * A simple [Fragment] subclass.
 * Use the [CommandDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommandDetailFragment : Fragment() {

    val args: CommandDetailFragmentArgs by navArgs()
    private lateinit var checkboxListAdapter: CheckboxListAdapter
    private val commandVM : CommandVM by viewModel{
        parametersOf(
            args.commandToDetail.idCommand
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()

        checkboxListAdapter = CheckboxListAdapter(args.commandToDetail.articleWrappers).apply {
            onCheckedItem = { item, isChecked ->
                item.statusCode = if(isChecked) ArticleWrapperStatusType.DONE.code else ArticleWrapperStatusType.IN_PROGRESS.code
                commandVM.saveArticleWrapper(item)
            }
        }

        // Set the adapter
        rv_command_articles.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = checkboxListAdapter
        }

        commandVM.currentAWLiveData().observe(viewLifecycleOwner){
            Log.d("[Command details]", "current commend observed for id ${args.commandToDetail.idCommand} : $it")
            if(it == null) {// Has been deleted
                goBack()
            }
            checkboxListAdapter.setItems(it ?: emptyList())
        }

        ic_delete.onClick {
            displayDeleteDialog()
        }
    }

    private fun displayDeleteDialog() {
        requireContext().materialAlertDialog {
            title = getString(R.string.delete_dialog_title)
            message = getString(R.string.delete_dialog_content)
            positiveButton(R.string.delete) {
                commandVM.removeCommand(commandVM.currentCommandMutableLiveData.value!!)
                Snackbar.make(requireView(), "La commande a bien été supprimée.",
                    Snackbar.LENGTH_LONG).show()
                goBack()
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }.onShow {
            positiveButton.textColorResource = R.color.see_green
            negativeButton.textColorResource = R.color.gray_2
        }.show()
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun setupHeader() {
        bt_previous_or_close.background = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_left_arrow)

        tv_title.text = getString(R.string.command_number_label, args.commandToDetail.idCommand)
        tv_subtitle.text = args.commandToDetail.client?.toNameString()
        ic_delete.visibility = VISIBLE

        bt_previous_or_close.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
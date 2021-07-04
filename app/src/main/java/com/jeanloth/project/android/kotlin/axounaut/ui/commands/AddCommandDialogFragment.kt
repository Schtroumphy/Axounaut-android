package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.formatDouble
import com.jeanloth.project.android.kotlin.axounaut.mock.DataMock
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import kotlinx.android.synthetic.main.fragment_add_command_dialog.*
import java.time.LocalDate

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    AddCommandDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class AddCommandDialogFragment : BottomSheetDialogFragment() {

    var articlesActualized = listOf<ArticleWrapper>()
    var isEditMode = true
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_command_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupHeaders()

        adapter = ArticleAdapter(DataMock.articleWrappers, true, requireContext()).apply {
            onAddMinusClick = {
                Log.d("ADD COMMAND", "  article : $it")
                bt_next.visibility  = if(it.count { it.count > 0 } > 0) VISIBLE else INVISIBLE
                articlesActualized = it
            }
        }
        rv_articles.adapter = adapter

        bt_next.setOnClickListener {
            // Display previous button
            if(isEditMode)
                changeEditModeDisplay()
            else {
                // Save command
                    /*var commandToSave = Command(
                        client =
                    )*/
                Log.d("ADD COMMAND", "Save command ")
            }
        }

        et_client.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                et_client.clearFocus()
            }
            false
        }
    }

    private fun changeEditModeDisplay() {
        isEditMode = !isEditMode
        setupPreviousCloseButton()
        setupNextButton()
        adapter.setItems(articlesActualized, isEditMode)
        setupElements()
    }

    private fun setupElements() {
        tv_add_command_title.text = getString(if(isEditMode) R.string.add_command_title else R.string.recap)
        et_client.visibility = if(isEditMode) VISIBLE else GONE
        et_delivery_date.visibility = if(isEditMode) VISIBLE else GONE
        tv_client.visibility = if(isEditMode) GONE else VISIBLE
        tv_delivery_date.visibility = if(isEditMode) GONE else VISIBLE

        Log.d("TAG", "${articlesActualized.filter { it.count > 0 }.map { it.count * it.totalArticleWrapperPrice }.sum()}")

        tv_total_price.visibility = if(isEditMode) GONE else VISIBLE
        if(!isEditMode)  tv_total_price.text = getString(R.string.total_price, articlesActualized.filter { it.count > 0 }.map { it.count * it.article.price }.sum().formatDouble())
    }

    private fun setupPreviousCloseButton() {
        bt_previous_or_close.background = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_close else R.drawable.ic_left_arrow
        )
    }

    private fun setupNextButton() {
        bt_next.background = getDrawable(
            requireContext(),
            if (isEditMode) R.drawable.ic_right_arrow else R.drawable.ic_check
        )
    }

    private fun setupSelectedItems(article: Article) {
        if (articlesActualized.map { it.article.name }.contains(article.name)) {
            /*if (article > 0) {
                //articlesActualized[articlesActualized.map { it.name }.indexOf(article.name)] = article
            }
        } else {
            //articlesActualized.add(article)
        }*/
            //articlesActualized.removeIf { it.count == 0 }
            Log.d("ADD COMMAND", "Selected items : $articlesActualized")
        }
    }

    private fun setupHeaders() {
        Log.d("TEST", "${LocalDate.now()}")
        tv_delivery_date.text = LocalDate.now().toString()

        bt_previous_or_close.setOnClickListener {
            if (isEditMode) {
                articlesActualized = mutableListOf()
                dismiss()
            } else
                changeEditModeDisplay()
        }
    }

    companion object {

        fun newInstance() = AddCommandDialogFragment()

    }
}
package com.jeanloth.project.android.kotlin.axounaut.ui.commands

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.AnalysisListAdapter
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import kotlinx.android.synthetic.main.fragment_add_command_dialog.*
import kotlinx.android.synthetic.main.fragment_analysis.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * A fragment for analysis.
 */
class AnalysisFragment: Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val commandVM : CommandVM by viewModel{
        parametersOf(
            0L
        )
    }
    private val articleVM : ArticleVM by viewModel()
    private lateinit var analysisAdapter : AnalysisListAdapter
    private var allArticles : List<Article> = emptyList()

    enum class AnalysisDisplayMode{
        PRODUCTS,
        CLIENTS,
        INPUT_OUTPUT,
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()

        allArticles = articleVM.getAllArticles()
        Log.d("[Analyse]", "All articles : ${allArticles.map { it.name }}")

        setupChart(allArticles)

        setupBestSeller()

    }

    private fun setupBestSeller() {
        // TODO Get 3 most commanded article
        analysisAdapter = AnalysisListAdapter(convertArticleToItemList(allArticles.sortedByDescending { it.timeOrdered }.take(3)), requireContext())
        rv_analysis.adapter = analysisAdapter
    }

    private fun setupChart(allArticles: List<Article>) {
        val pie = AnyChart.pie()

        // Entries : Article label - CommandTimes
        val data: MutableList<DataEntry> = ArrayList()
        allArticles.forEach {
            Log.d("[Analysis]", "${it.name} - ${it.timeOrdered}")
            data.add(ValueDataEntry(it.name, it.timeOrdered))
        }
        pie.data(data)

        any_chart_view.setChart(pie)
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Analyses")
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideOrShowMenuButton(false)
        mainActivity.replaceHeaderLogoByBackButton(false)
    }

    private fun goToCommandDetails(command: Command) {
        Log.d("TAG", "Command to details : $command")
        findNavController().navigate(
            CommandListFragmentDirections.actionNavCommandListToNavCommandDetails(
                commandToDetail = command
            )
        )
    }

    fun convertArticleToItemList(articles : List<Article>) : List<AnalysisList>{
        val list = mutableListOf<AnalysisList>()
        articles.forEach {
            val articleLabel = requireContext().getString(R.string.article_name, it.name)
            list.add(
                AnalysisList(
                    articleLabel,
                    it.timeOrdered,
                    totalAmount = it.timeOrdered * it.price
                )
            )
        }
        return list
    }
}
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
import com.jeanloth.project.android.kotlin.axounaut.AppLogger.logD
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.AnalysisListAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAnalysisBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.convertArticleToItemList
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
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

    private lateinit var binding: FragmentAnalysisBinding

    enum class AnalysisDisplayMode{
        INGREDIENTS,
        CLIENTS,
        INPUT_OUTPUT,
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupHeader()
        binding = FragmentAnalysisBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allArticles = articleVM.getAllArticles()
        javaClass.logD("All articles : ${allArticles.map { it.label }}")

        setupChart(allArticles)

        setupBestSeller()

    }

    private fun setupBestSeller() {
        // TODO Get 3 most commanded article
        analysisAdapter = AnalysisListAdapter(allArticles.sortedByDescending { it.timeOrdered }.take(3).convertArticleToItemList(requireContext()), requireContext())
        binding.rvAnalysis.adapter = analysisAdapter
    }

    private fun setupChart(allArticles: List<Article>) {
        val pie = AnyChart.pie()

        // Entries : Article label - CommandTimes
        val data: MutableList<DataEntry> = ArrayList()
        allArticles.forEach {
            Log.d("[Analysis]", "${it.label} - ${it.timeOrdered}")
            data.add(ValueDataEntry(it.label, it.timeOrdered))
        }
        pie.data(data)

        binding.anyChartView.setChart(pie)
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Analyses")
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
package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.SwipeToCancelCallback
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import splitties.views.onClick
import java.lang.IllegalArgumentException

/**
 * Fragment that contains steps fragment
 */
class AddArticleFragment : Fragment(), StepListener {

    private val args : AddArticleFragmentArgs by navArgs()
    private val addArticleVM : AddArticleVM by sharedViewModel()
    private val mainVM : MainVM by sharedViewModel()
    private val stockVM : StockVM by viewModel()
    private lateinit var navController: NavController

    private lateinit var checkboxTextViewAdapter: CheckboxTextViewAdapter
    private lateinit var binding: FragmentArticleDetailsBinding

    private var stepCount = 1
    val TAG = "[Article Details Fragment]"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update article to edit in viewModel if not null
        args.articleToEdit?.let {
            addArticleVM.setArticleToEdit(it)
        }

        // Other setup
        setupHeader()

        // Setup adapters
        checkboxTextViewAdapter = CheckboxTextViewAdapter(mutableListOf())

        // Listeners
        stockVM.observePWLiveData().observe(viewLifecycleOwner){
            checkboxTextViewAdapter.setItems(it)
        }

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_container_step)
                as NavHostFragment

        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            stepCount = when(destination.id){
                R.id.nav_step1 -> 1
                R.id.nav_step2 -> 2
                R.id.nav_step3 -> 3
                R.id.nav_step4 -> 4
                else -> throw IllegalArgumentException("Destination id ${destination.id} not known.")
            }
            Log.d(TAG, "Step new count = $stepCount")
            binding.ivBack.visibility = if(stepCount == 1) INVISIBLE else VISIBLE
            binding.tvStep.text = getString(R.string.article_step, stepCount)
            binding.tvStepTitle.text = getString(getStepTitleByStepCount())
            addArticleVM.setStepCount(stepCount)

            binding.btAddArticle.text = getString(if(stepCount == 4) R.string.save else R.string.next)
        }

        addArticleVM.canResumeLD.observe(viewLifecycleOwner){
            Log.d(TAG, "Can resume step $stepCount ? $it")
            binding.btAddArticle.isEnabled = it
        }

        // Click listeners
        binding.ivBack.onClick {
            navController.navigateUp()
        }

        binding.btAddArticle.setOnClickListener {
            if(stepCount < 4){

                val navDirection = when(stepCount){
                    1 -> AddArticleStep1FragmentDirections.actionNavStep1ToNavStep2()
                    2 -> AddArticleStep2FragmentDirections.actionNavStep2ToNavStep3()
                    3 -> AddArticleStep3FragmentDirections.actionNavStep3ToNavStep4()
                    4 -> null
                    else -> null
                }

                // Change fragment step
                navDirection?.let {
                    navController.navigate(it)
                }
                Log.d(TAG, "Step count = $stepCount")
            } else {
                addArticle()
            }
        }

    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Ajouter un article")
    }

    private fun addArticle() {
        Log.d("[Article Details Fragment", "Click on save Article")
        addArticleVM.saveArticle()
        addArticleVM.clearData()
        Snackbar.make(requireView(), "Article ajouté avec succès.", Snackbar.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun getStepTitleByStepCount() : Int{
        return when(stepCount){
            1 -> R.string.article_step1_title
            2 -> R.string.article_step2_title
            3 -> R.string.article_step3_title
            4 -> R.string.article_step4_title
            else -> R.string.article_step1_title
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "On fragment detach")
        addArticleVM.clearData()
    }



}
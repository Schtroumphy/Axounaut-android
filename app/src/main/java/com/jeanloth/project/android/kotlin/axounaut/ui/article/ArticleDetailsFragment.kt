package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick
import java.lang.IllegalArgumentException

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ArticleDetailsFragment : Fragment(), StepListener {

    private val articleVM : ArticleVM by viewModel()
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

        // Other setup
        setupHeader()

        // Setup adapters
        checkboxTextViewAdapter = CheckboxTextViewAdapter(mutableListOf())

        // Setup recycler views
        //binding.rvRecipeArticles.adapter = checkboxTextViewAdapter


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
                Log.d(TAG, "Add article")
                //addArticle()
            }
        }
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Ajouter un article")
    }

    private fun addArticle() {
        /*val articleName = binding.etArticleName.text.toString()
        //val articlePrice = binding.etArticlePrice.text.toString()
        val articleCategory = getArticleCategoryFromLabel(binding.spinnerCategories.selectedItem.toString())

        if( articleName.isEmpty() /*|| articlePrice.isEmpty()*/) {
            Snackbar.make(requireView(), "Veuillez saisir des valeurs valides.",
                Snackbar.LENGTH_LONG).show()
        } else {
            val articleToAdd = Article(
                label = articleName,
               // price = /*articlePrice.toDouble()*/,
                category = articleCategory.code
            )
            Log.d("[Article Details Fragment", "Article to add : $articleToAdd")
            articleVM.saveArticle(articleToAdd)
            Snackbar.make(requireView(), "Article ajouté avec succès.",
                Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }*/
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

}
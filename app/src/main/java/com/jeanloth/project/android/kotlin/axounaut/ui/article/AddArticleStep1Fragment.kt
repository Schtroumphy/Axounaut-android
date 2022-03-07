package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromLabel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ArticleDetailsFragment : Fragment() {

    private val articleVM : ArticleVM by viewModel()
    private val mainVM : MainVM by sharedViewModel()
    private val stockVM : StockVM by viewModel()

    private lateinit var checkboxTextViewAdapter: CheckboxTextViewAdapter
    private lateinit var binding: FragmentArticleDetailsBinding

    private var stepCount = 1

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
        setupSpinner()

        // Setup adapters
        checkboxTextViewAdapter = CheckboxTextViewAdapter(mutableListOf())

        // Setup recycler views
        //binding.rvRecipeArticles.adapter = checkboxTextViewAdapter


        // Listeners
        stockVM.observePWLiveData().observe(viewLifecycleOwner){
            checkboxTextViewAdapter.setItems(it)
        }

        // Click listeners
        binding.btAddArticle.setOnClickListener {
            if(stepCount < 4){
                stepCount++
            } else {
                addArticle()
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

    private fun setupSpinner() {

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArticleCategory.values().map { it.label }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //binding.spinnerCategories.adapter = adapter
    }

}
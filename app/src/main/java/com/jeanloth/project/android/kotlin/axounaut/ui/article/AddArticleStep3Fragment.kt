package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.AppLogger.logD
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleAdapter
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.adapters.IngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep1Binding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep2Binding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep3Binding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromLabel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class AddArticleStep3Fragment : Fragment() {

    private val addArticleVM : AddArticleVM by sharedViewModel()
    private lateinit var binding: FragmentAddArticleStep3Binding

    // Adapters
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleStep3Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Quantity of ingredients automatically edited on add/minus click
        ingredientAdapter = IngredientAdapter(emptyList(), true, requireContext(), true).apply{
            onAddMinusClick = {
                addArticleVM.checkedItemsHasChanged()
            }
        }
        binding.rvProducts.adapter = ingredientAdapter

        addArticleVM.checkedItemsLD.observe(viewLifecycleOwner){
            ingredientAdapter.setItems(it, true, true)
        }

        // Setup time preparing changes
        addArticleVM.timePreparingLiveData.observe(viewLifecycleOwner){
            javaClass.logD("Preparing time : $it")
            binding.tvCountPreparingTime.text = addArticleVM.displayHour()
            binding.ibMinusTime.visibility = if(it > 0f) VISIBLE else INVISIBLE
            binding.ibAddTime.visibility = if(it < 5f) VISIBLE else INVISIBLE
        }

        binding.ibAddTime.onClick {
            addArticleVM.setTimePreparingMutableLiveData(true)
        }

        binding.ibMinusTime.onClick {
            addArticleVM.setTimePreparingMutableLiveData(false)
        }


    }

}
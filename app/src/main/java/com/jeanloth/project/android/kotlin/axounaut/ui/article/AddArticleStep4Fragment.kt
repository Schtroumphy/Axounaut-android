package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.IngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.*
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class AddArticleStep4Fragment : Fragment() {

    private lateinit var binding: FragmentAddArticleStep4Binding
    private val addArticleVM : AddArticleVM by sharedViewModel()

    // Adapters
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleStep4Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRecapArticleName.text = getString(R.string.article_name_recap, addArticleVM.nameLiveData.value)
        binding.tvRecapArticleCategory.text = getString(R.string.article_category_recap, addArticleVM.categoryLiveData.value?.label)
        binding.tvRecapArticlePreparingTime.text = getString(R.string.article_recap_preparing_time, addArticleVM.displayHour())

        // Setup recyclerview
        ingredientAdapter = IngredientAdapter(addArticleVM.checkedItemsLD.value ?: emptyList(), false, requireContext(), true)
        binding.rvRecapRecipeItems.adapter = ingredientAdapter

    }

}
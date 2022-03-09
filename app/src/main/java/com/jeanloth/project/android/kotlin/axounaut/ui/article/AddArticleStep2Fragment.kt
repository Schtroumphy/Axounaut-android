package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep2Binding
import com.jeanloth.project.android.kotlin.axounaut.extensions.hideKeyboard
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Ingredient
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.sharedViewModel
import splitties.views.onClick

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class AddArticleStep2Fragment : Fragment() {

    // TODO instanciate Add article VM shared between all fragment steps
    private val addArticleVM : AddArticleVM by sharedViewModel()

    private lateinit var checkboxTextViewAdapter: CheckboxTextViewAdapter
    private lateinit var binding: FragmentAddArticleStep2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleStep2Binding.inflate(layoutInflater, container, false)
        // Setup adapters
        checkboxTextViewAdapter = CheckboxTextViewAdapter(mutableListOf()).apply{
            onCheckedItem = { item, isChecked ->
                addArticleVM.updateCheckedItemsList(item, isChecked)
            }
        }

        // Click listeners
        binding.tvAddProduct.onClick {
            binding.gpAddIngredient.visibility = View.VISIBLE
            binding.tvAddProduct.visibility = View.GONE
        }

        binding.tvValidateIngredient.onClick {
            saveIngredient()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mock = mutableListOf(
            IngredientWrapper(ingredient = Ingredient(label = "Farine"), quantity = 4.0f),
            IngredientWrapper(ingredient = Ingredient(label = "Beurre 500g"), quantity = 4.0f, quantityType = IngredientQuantityType.G),
            IngredientWrapper(ingredient = Ingredient(label = "Lait 1L"), quantity = 0.0f, quantityType = IngredientQuantityType.L),
            IngredientWrapper(ingredient = Ingredient(label = "Epices"), quantity = 1.0f, quantityType = IngredientQuantityType.G),
            IngredientWrapper(ingredient = Ingredient(label = "Autre 1"), quantity = 1.0f, quantityType = IngredientQuantityType.L),
            IngredientWrapper(ingredient = Ingredient(label = "Test"), quantity = 0.0f, quantityType = IngredientQuantityType.L),
            IngredientWrapper(ingredient = Ingredient(label = "Test 2"), quantity = 2.0f, quantityType = IngredientQuantityType.G),
            IngredientWrapper(ingredient = Ingredient(label = "Test 3"), quantity = 2.0f, quantityType = IngredientQuantityType.G),
            IngredientWrapper(ingredient = Ingredient(label = "Test 4"), quantity = 0.0f, quantityType = IngredientQuantityType.G),
        )
        binding.rvIngredients.adapter = checkboxTextViewAdapter
        //checkboxTextViewAdapter.setItems(mock)

        /** Observers **/
        addArticleVM.result.observe(viewLifecycleOwner){
            checkboxTextViewAdapter.setItems(it)
        }

        binding.etIngredient.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                saveIngredient()
            }
            true
        }

    }

    private fun saveIngredient(){
        hideKeyboard()
        binding.apply {
            gpAddIngredient.visibility = View.GONE
            tvAddProduct.visibility = View.VISIBLE
        }
        addArticleVM.saveIngredientWrapper(IngredientWrapper(ingredient = Ingredient(label = binding.etIngredient.text.toString())))
        binding.etIngredient.setText("")
    }
}
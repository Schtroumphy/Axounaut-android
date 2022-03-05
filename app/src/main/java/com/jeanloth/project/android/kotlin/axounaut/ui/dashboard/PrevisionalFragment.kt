package com.jeanloth.project.android.kotlin.axounaut.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jeanloth.project.android.kotlin.axounaut.adapters.PrevisionalIngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentPrevisionalBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.PrevisionalWrapper
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PrevisionalFragment : Fragment() {

    private val stockVM : StockVM by sharedViewModel()
    private val commandVM : CommandVM by sharedViewModel()
    private lateinit var previsionalIngredientAdapter: PrevisionalIngredientAdapter

    private lateinit var allActualProductWrappers: List<IngredientWrapper>  // Stock
    private lateinit var allNeededRecipeWrappers: List<RecipeWrapper>

    private lateinit var binding: FragmentPrevisionalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrevisionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commandVM.allNotDeliveredCommandLiveData().observe(viewLifecycleOwner){
            allNeededRecipeWrappers = it
            buildPrevisionalWrapper()
        }

        stockVM.observePWLiveData().observe(viewLifecycleOwner){
            allActualProductWrappers = it
            buildPrevisionalWrapper()
        }

        val mock = listOf(
            PrevisionalWrapper(ingredient = Ingredient(label = "Farine"), actual = 4.0f, needed = 5.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Beurre 500g"), actual = 0.0f, needed = 5.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Lait 1L"), actual = 4.0f, needed = 2.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Epices"), actual = 2.0f, needed = 5.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Autre 1"), actual = 1.0f, needed = 1.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Test"), actual = 4.0f, needed = 4.0f),
            PrevisionalWrapper(ingredient = Ingredient(label = "Test 2"), actual = 12.0f, needed = 5.0f)
        )

        previsionalIngredientAdapter = PrevisionalIngredientAdapter(mock, requireContext())

        binding.rvIngredients.adapter = previsionalIngredientAdapter

        // Observe all pws
        //stockVM.observePWLiveData().observe(viewLifecycleOwner){
        //    ingredientAdapter.setItems(it)
        //}
    }

    fun buildPrevisionalWrapper(){
        val liste = mutableListOf<PrevisionalWrapper>()
        allNeededRecipeWrappers.forEach { rw ->
            val actual = allActualProductWrappers.find { it.ingredient.label == rw.ingredient.label }

            if(actual != null) {
                liste.add(PrevisionalWrapper(ingredient = actual.ingredient, actual = actual.quantity, needed = rw.quantity))
            }
        }
        binding.tvNoRecipeSaved.visibility = if(liste.isEmpty()) VISIBLE else GONE
        previsionalIngredientAdapter.setItems(liste)
    }

}
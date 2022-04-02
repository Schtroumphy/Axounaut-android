package com.jeanloth.project.android.kotlin.axounaut.ui.stock

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.adapters.IngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentStocksBinding
import com.jeanloth.project.android.kotlin.axounaut.datastore.StockManager
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Ingredient
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType.Companion.fromVal
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick

class StockFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val stockVM : StockVM by viewModel()
    private lateinit var binding: FragmentStocksBinding

    private val stockManager: StockManager by inject()

    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStocksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()

        setupSpinner()

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

        // Setup adapter
        ingredientAdapter = IngredientAdapter(emptyList(), true, requireContext()).apply {
            onAddMinusClick = {
                Log.d("ADD COMMAND", "  articles list : $it")
                stockVM.saveIngredientWrapper(it)
                stockVM.updateStockLastUpdateDate()
            }
            displayNoArticlesError = {
                //tv_error_no_articles.visibility = if(it) VISIBLE else GONE
            }
        }
        binding.rvIngredients.adapter = ingredientAdapter

        // Observe all ingredient wrappers
        stockVM.observePWLiveData().observe(viewLifecycleOwner){
            ingredientAdapter.setItems(it)
        }

        // Click listeners
        binding.tvAddIngredient.onClick {
            binding.llAddIngredient.visibility = VISIBLE
            binding.tvAddIngredient.visibility = GONE
        }

        binding.tvValidateIngredient.onClick {
            stockVM.saveIngredientWrapper(IngredientWrapper(ingredient = Ingredient(label = binding.etIngredient.text.toString()), quantityType = binding.spinnerQuantityType.selectedItem.toString().fromVal()))
            ingredientAdapter.setItems(mock)
            binding.tvAddIngredient.visibility = VISIBLE
            binding.llAddIngredient.visibility = GONE
        }

        binding.btSeePrevisional.onClick {
            findNavController().navigate(StockFragmentDirections.actionNavStockToNavPrevisional())
        }
    }

    private fun setupSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_spinner_item,
            IngredientQuantityType.values().map { it.label }
        )

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerQuantityType.adapter = adapter

        //val selected: String = spinner_quantity_type.selectedItem.toString()
    }

    private fun setupHeader() {
        mainVM.setHeaderTitle("Stock")
    }


}

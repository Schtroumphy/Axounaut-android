package com.jeanloth.project.android.kotlin.axounaut.ui.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxListAdapter
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxShoppingAdapter
import com.jeanloth.project.android.kotlin.axounaut.adapters.PrevisionalIngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentPrevisionalBinding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentShoppingListBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.PrevisionalWrapper
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick

class ShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListBinding

    private val mainVM : MainVM by sharedViewModel()
    private val stockVM : StockVM by viewModel()

    private lateinit var checkboxShoppingAdapter: CheckboxShoppingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Liste de courses")

        checkboxShoppingAdapter = CheckboxShoppingAdapter(mutableListOf()).apply {
            onCheckedItem = { item, isChecked ->

            }
        }
        binding.rvIngredients.adapter = checkboxShoppingAdapter

        stockVM.previsionalWrappersMediatorLiveData.observe(viewLifecycleOwner){
            binding.tvNoShoppingList.visibility = if(it.isEmpty()) VISIBLE else GONE
            checkboxShoppingAdapter.setItems(it.filter { it.delta < 0f })
        }
    }

}
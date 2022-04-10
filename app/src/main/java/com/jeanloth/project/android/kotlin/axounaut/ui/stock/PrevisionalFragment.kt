package com.jeanloth.project.android.kotlin.axounaut.ui.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.adapters.PrevisionalIngredientAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentPrevisionalBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.StockVM
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.PrevisionalWrapper
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick

class PrevisionalFragment : Fragment() {

    private val stockVM : StockVM by viewModel()
    private val mainVM : MainVM by sharedViewModel()
    private lateinit var previsionalIngredientAdapter: PrevisionalIngredientAdapter

    private lateinit var binding: FragmentPrevisionalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        previsionalIngredientAdapter = PrevisionalIngredientAdapter(emptyList(), requireContext())

        binding = FragmentPrevisionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Prévisionnel", "Du ${stockVM.currentWeekFirstDate} au ${stockVM.currentWeekLastDate}")

        binding.rvIngredients.adapter = previsionalIngredientAdapter

        stockVM.previsionalWrappersMediatorLiveData.observe(viewLifecycleOwner){
            binding.tvNoRecipeSaved.visibility = if(it.isEmpty()) VISIBLE else GONE
            previsionalIngredientAdapter.setItems(it)
        }

        binding.btShoppingList.onClick {
            goToShoppingList()
        }

    }

    private fun goToShoppingList(){
        findNavController().navigate(PrevisionalFragmentDirections.actionNavPrevisionalToNavShoppingFragment())

    }
}
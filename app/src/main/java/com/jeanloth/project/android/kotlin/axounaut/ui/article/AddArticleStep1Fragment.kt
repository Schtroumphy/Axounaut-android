package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep1Binding
import com.jeanloth.project.android.kotlin.axounaut.extensions.hideKeyboard
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromLabel
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Step 1 : Name, type and price of the article to add
 */
class AddArticleStep1Fragment : Fragment() {

    private lateinit var binding: FragmentAddArticleStep1Binding
    private val addArticleVM : AddArticleVM by sharedViewModel()
    private val TAG = "[Add Article Step 1 Fragment]"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddArticleStep1Binding.inflate(layoutInflater, container, false)
        setupSpinner()
        setupPriceListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addArticleVM.priceLiveData().observe(viewLifecycleOwner){
            Log.d(TAG, "Price observed : $it")
            binding.tvCount.text = it.toString()

            binding.ibMinus.visibility = if(it == 0) GONE else VISIBLE
        }

        binding.etArticleName.setOnEditorActionListener { _ , actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                addArticleVM.setArticleName(binding.etArticleName.text.toString())
                binding.etArticleName.clearFocus()
                hideKeyboard()
            }
            true
        }

        binding.etArticleName.setOnFocusChangeListener { _, isFocused ->
            if(!isFocused) addArticleVM.setArticleName(binding.etArticleName.text.toString())
        }
    }

    private fun setupPriceListeners(){
        binding.ibAdd.setOnClickListener {
            addArticleVM.setPrice(true)
            hideKeyboard()
            binding.etArticleName.clearFocus()
        }

        binding.ibMinus.setOnClickListener {
            addArticleVM.setPrice()
            hideKeyboard()
            binding.etArticleName.clearFocus()
        }
    }

    private fun setupSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArticleCategory.values().map { it.label }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.apply {
            adapter = spinnerAdapter
            onFocusChangeListener = View.OnFocusChangeListener { _, p1 -> if(p1) hideKeyboard() }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val category : ArticleCategory = getArticleCategoryFromLabel(adapter.getItem(position).toString())
                    binding.etArticleName.clearFocus()
                    addArticleVM.setArticleCategory(category)
                }

            }
        }


    }

}
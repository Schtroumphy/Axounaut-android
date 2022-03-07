package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.CheckboxTextViewAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentAddArticleStep1Binding
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleDetailsBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.hideKeyboard
import com.jeanloth.project.android.kotlin.axounaut.viewModels.AddArticleVM
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
class AddArticleStep1Fragment : Fragment() {


    private lateinit var checkboxTextViewAdapter: CheckboxTextViewAdapter
    private lateinit var binding: FragmentAddArticleStep1Binding

    private val addArticleVM : AddArticleVM by viewModel()
    private lateinit var navController: NavController

    private var count = 0
    val TAG = "[Add Article Step 1 Fragment]"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleStep1Binding.inflate(layoutInflater, container, false)
        setupSpinner()
        setupPriceListeners()
        setupArticleNameListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addArticleVM.priceLiveData().observe(viewLifecycleOwner){
            Log.d(TAG, "Price observed : $it")
            binding.tvCount.text = it.toString()

            binding.ibMinus.visibility = if(it == 0) GONE else VISIBLE
        }
    }

    private fun setupArticleNameListener() {
        binding.etArticleName.apply {

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    addArticleVM.setArticleName(p0.toString())
                }
            })

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    this.clearFocus()
                }
                true
            }
        }
    }

    private fun setupPriceListeners(){
        binding.ibAdd.setOnClickListener {
            addArticleVM.setPrice(true)
        }

        binding.ibMinus.setOnClickListener {
            addArticleVM.setPrice()
        }
    }

    private fun setupSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArticleCategory.values().map { it.label }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategories.adapter = adapter
        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category : ArticleCategory = getArticleCategoryFromLabel(adapter.getItem(position))
                addArticleVM.setArticleCategory(category)
            }

        }


    }

}
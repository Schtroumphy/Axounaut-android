package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleListAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleBinding
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick


/**
 * A simple [Fragment] subclass.
 * Use the [ArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val articleVM : ArticleVM by viewModel()
    private lateinit var adapter: ArticleListAdapter

    private lateinit var binding: FragmentArticleBinding

    enum class EArticleDisplayMode{
        EDITION,
        DETAILLED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Mes articles")

        adapter = ArticleListAdapter(emptyList(), requireContext()).apply {
            onEditClick = { }
        }
        binding.rvArticlesFragment.adapter = adapter

        binding.addButton.onClick{
            goToArticleDetails()
        }

        lifecycleScope.launchWhenStarted {
            articleVM.allArticlesLiveData().observe(viewLifecycleOwner){
                Log.d("[Article Fragment", "Article observed : $it")
                adapter.setItems(it)
                binding.tvErrorNoArticles.visibility = if(it.isEmpty()) VISIBLE else GONE
            }
        }

    }

    private fun goToArticleDetails() {
        findNavController().navigate(ArticleFragmentDirections.actionNavArticleToNavArticleDetails())
    }

}
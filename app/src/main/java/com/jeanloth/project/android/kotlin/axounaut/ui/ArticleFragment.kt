package com.jeanloth.project.android.kotlin.axounaut.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleListAdapter
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import kotlinx.android.synthetic.main.fragment_add_command_dialog.*
import kotlinx.android.synthetic.main.fragment_article.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleFragment : Fragment() {

    private val articleVM : ArticleVM by viewModel()
    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArticleListAdapter(emptyList(), requireContext())
        rv_articles_fragment.adapter = adapter

        fab_add_article.setOnClickListener{
            articleVM.saveArticle()
        }

        lifecycleScope.launchWhenStarted {
            articleVM.allArticlesLiveData().observe(viewLifecycleOwner){
                Log.d("[Article Fragment", "Article observed : $it")
                adapter.setItems(it)
            }
        }

    }

}
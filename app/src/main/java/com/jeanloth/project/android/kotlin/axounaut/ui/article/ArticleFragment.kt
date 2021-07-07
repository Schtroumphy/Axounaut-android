package com.jeanloth.project.android.kotlin.axounaut.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleListAdapter
import com.jeanloth.project.android.kotlin.axounaut.extensions.openPopUpMenu
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.layout_header.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ArticleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleFragment : Fragment() {

    private val mainVM : MainVM by sharedViewModel()
    private val articleVM : ArticleVM by viewModel()
    private lateinit var adapter: ArticleListAdapter

    enum class EArticleDisplayMode{
        EDITION,
        DETAILLED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.setHeaderTitle("Mes articles")

        adapter = ArticleListAdapter(emptyList(), requireContext()).apply {
            onMenuClick = { view, articleToDelete ->
                view.openPopUpMenu(requireContext(), R.menu.popup_menu, map = mapOf(
                    R.id.menu_delete to {
                        Toast.makeText(requireContext(), "Suppression définitive de : ${articleToDelete.name}", Toast.LENGTH_SHORT).show()
                        articleVM.deleteArticle(articleToDelete)
                    }
                ))
            }
        }
        rv_articles_fragment.adapter = adapter

        cl_add_article_btn.setOnClickListener{
            goToArticleDetails()
        }

        lifecycleScope.launchWhenStarted {
            articleVM.allArticlesLiveData().observe(viewLifecycleOwner){
                Log.d("[Article Fragment", "Article observed : $it")
                adapter.setItems(it)
                tv_error_no_articles.visibility = if(it.isEmpty()) VISIBLE else GONE
            }
        }

    }

    private fun goToArticleDetails() {
        findNavController().navigate(ArticleFragmentDirections.actionNavArticleToNavArticleDetails())
    }

}
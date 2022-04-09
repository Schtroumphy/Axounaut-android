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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.adapters.ArticleListAdapter
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentArticleBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.SwipeToCancelCallback
import com.jeanloth.project.android.kotlin.axounaut.extensions.displayDialog
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
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

        adapter = ArticleListAdapter(articleVM.getAllArticles(), requireContext()).apply {
            onEditClick = {
                goToAddArticleFragment(it)
            }

            onSwipeItem = { article, position ->
                displayCancelOrDeleteArticle(article, position)
            }
        }
        binding.rvArticlesFragment.adapter = adapter

        binding.addButton.onClick{
            goToAddArticleFragment()
        }

        lifecycleScope.launchWhenStarted {
            articleVM.allArticlesLiveData().observe(viewLifecycleOwner){
                Log.d("[Article Fragment", "Article observed : $it")
                adapter.setItems(it)
                binding.tvErrorNoArticles.visibility = if(it.isEmpty()) VISIBLE else GONE
            }
        }

        // Setup swipe to delete article
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : SwipeToCancelCallback(requireContext()) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return true }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.updateArticleStatus(viewHolder.absoluteAdapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvArticlesFragment)
    }

    private fun displayCancelOrDeleteArticle(article: Article, position: Int){
        displayDialog(
            context = requireContext(),
            titleRef = R.string.cancel_dialog_title,
            contentRef = R.string.delete_article_dialog_content,
            negativeButtonLabelRef = R.string.cancel_article,
            negativeAction =  {
                adapter.refreshRecyclerView(position)
            },
            positiveButtonLabelRef = R.string.delete_article,
            positiveAction = {
                articleVM.deleteArticle(article)
            })
    }

    private fun goToAddArticleFragment(article: Article? = null) {
        findNavController().navigate(ArticleFragmentDirections.actionNavArticleToNavAddArticleFragment(
            articleToEdit = article
        ))
    }

}
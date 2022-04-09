package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemArticleListBinding
import com.jeanloth.project.android.kotlin.axounaut.extensions.displayPreparingTime
import com.jeanloth.project.android.kotlin.axounaut.extensions.toCamelCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromCode
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper.Companion.toIngredientWrapper
import splitties.views.onClick

class ArticleListAdapter(
    private var articleList : List<Article>,
    private var context : Context
) : RecyclerView.Adapter<ArticleListAdapter.ArticleHolder>()  {

    var onClick : ((List<ArticleWrapper>) -> Unit)? = null
    var onEditClick : ((Article) -> Unit)? = null
    var onSwipeItem : ((Article, Int) -> Unit)? = null

    init {
        articleList = articleList.filter { !it.isHidden }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_list, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article: Article= articleList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    fun setItems(articles : List<Article>){
        this.articleList = articles.filter { !it.isHidden }
        notifyDataSetChanged()
    }

    fun updateArticleStatus(adapterPosition : Int) {
        onSwipeItem?.invoke(articleList[adapterPosition], adapterPosition)
    }

    fun refreshRecyclerView(position : Int){
        notifyItemChanged(position)
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemArticleListBinding.bind(view)

        fun bind(article : Article){
            binding.tvName.text= article.label.toCamelCase()
            binding.tvPrice.text= context.getString(R.string.price_euro, article.price.toString())
            binding.tvArticleCategoryOrTime.text = getArticleCategoryFromCode(article.category).label

            binding.rvRecipeDetails.adapter = RecipeContentAdapter(article.recipeIngredients.toIngredientWrapper(), context)

            binding.btEditArticle.setOnClickListener {
                onEditClick?.invoke(article)
            }

            binding.btExpand.onClick {
                binding.gpArticleDetails.visibility = if(binding.gpArticleDetails.isVisible) View.GONE else View.VISIBLE
                binding.btExpand.background = getDrawable(context, if(binding.gpArticleDetails.isVisible) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow)
                binding.tvArticleCategoryOrTime.text = if(binding.gpArticleDetails.isVisible) context.getString(R.string.article_short_recap_preparing_time, displayPreparingTime(article.preparingTime)) else getArticleCategoryFromCode(article.category).label
            }
        }
    }

}
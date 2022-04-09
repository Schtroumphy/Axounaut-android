package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemArticleBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper

class ArticleAdapter(
    private var articleList : List<ArticleWrapper>,
    private var isEditMode : Boolean = true,
    private val context : Context
) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>()  {

    var onAddMinusClick : ((List<ArticleWrapper>) -> Unit)? = null
    var displayNoArticlesError : ((Boolean) -> Unit)? = null

    init {
        displayNoArticlesError?.invoke(articleList.isEmpty())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article: ArticleWrapper = articleList[position]
        holder.bind(article, position)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    fun setItems(articles : List<ArticleWrapper>, isEditMode : Boolean = false){
        this.articleList = if(isEditMode) articles else articles.filter { it.count > 0 }
        this.isEditMode = isEditMode

        displayNoArticlesError?.invoke(articleList.isEmpty())
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val binding = ItemArticleBinding.bind(view)

        fun bind(articleWrapper : ArticleWrapper, position : Int){

            binding.tvName.text= articleWrapper.article.label

            val count = articleWrapper.count

            setupElementVisibility(count.toString())

            binding.tvName.setTextColor(getColor(context, R.color.gray_1))
            binding.tvCount.setTextColor(getColor(context, R.color.gray_1))

            if(isEditMode){
                binding.ibMinus.visibility = if(count > 0) VISIBLE else GONE

                binding.tvName.setTextColor(getColor(context, if(count > 0) R.color.orange_003 else R.color.gray_1))
                binding.tvCount.setTextColor(getColor(context, if(count > 0) R.color.orange_002 else R.color.gray_1))

                binding.ibAdd.setOnClickListener {
                    articleWrapper.count = count + 1
                    onAddMinusClick?.invoke(articleList)
                    notifyItemChanged(position)
                }

                binding.ibMinus.setOnClickListener {
                    if(count > 0) {
                        articleWrapper.count = count - 1
                    }
                    onAddMinusClick?.invoke(articleList)
                    notifyItemChanged(position)
                }

            }

        }

        private fun setupElementVisibility(count : String) {
            binding.tvCount.text = if(isEditMode) count else context.resources.getString(R.string.x_count, count)
            binding.ibAdd.visibility = if(isEditMode) VISIBLE else GONE
            if(!isEditMode) binding.ibMinus.visibility = GONE
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK on !")
        }

    }

}
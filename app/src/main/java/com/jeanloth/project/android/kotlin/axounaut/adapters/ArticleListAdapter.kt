package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_article.view.tv_name
import kotlinx.android.synthetic.main.item_article_list.view.*

class ArticleListAdapter(
    private var articleList : List<Article>,
    private val context : Context? = null
) : RecyclerView.Adapter<ArticleListAdapter.ArticleHolder>()  {

    var onClick : ((List<ArticleWrapper>) -> Unit)? = null
    var onMenuClick : ((View, Article) -> Unit)? = null

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
        this.articleList = articles
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(article : Article){
            itemView.tv_name.text= article.name
            itemView.tv_price.text= article.price.toString()

            itemView.bt_menu_more.setOnClickListener {
                onMenuClick?.invoke(it, article)
            }
        }


        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK on !")
        }

    }

}
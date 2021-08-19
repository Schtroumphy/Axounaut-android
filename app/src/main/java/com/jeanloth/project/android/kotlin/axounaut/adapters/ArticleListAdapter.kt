package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.android.synthetic.main.item_article.view.tv_name
import kotlinx.android.synthetic.main.item_article_list.view.*
import splitties.views.onClick

class ArticleListAdapter(
    private var articleList : List<Article>,
    private var context : Context
) : RecyclerView.Adapter<ArticleListAdapter.ArticleHolder>()  {

    var onClick : ((List<ArticleWrapper>) -> Unit)? = null
    var onEditClick : ((Article) -> Unit)? = null

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
            itemView.tv_price.text= context.getString(R.string.price_euro, article.price.toString())
            //itemView.tv_article_stat.text= context.getString(R.string.article_command_number, article.price.toString())

            itemView.bt_edit_article.setOnClickListener {
                onEditClick?.invoke(article)
            }

            itemView.bt_expand.onClick {
                itemView.gp_article_details.visibility = if(itemView.gp_article_details.isVisible) View.GONE else View.VISIBLE
                itemView.bt_expand.background = getDrawable(context, if(itemView.gp_article_details.isVisible) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow)
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
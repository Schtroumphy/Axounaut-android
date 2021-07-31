package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType.Companion.getCommandStatusByCode
import kotlinx.android.synthetic.main.item_command.view.*
import splitties.views.textColorResource

class CommandAdapter(
    private var commandList : List<Command>,
    private var context : Context
) : RecyclerView.Adapter<CommandAdapter.ArticleHolder>()  {

    var onClick : ((Command) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_command, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article: Command = commandList[position]
        holder.bind(article, position)
    }

    override fun getItemCount(): Int {
        return commandList.size
    }

    fun setItems(articles : List<Command>){
        this.commandList =  articles
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(command : Command, position : Int){

            itemView.cv_command.setOnClickListener {
                onClick?.invoke(command)
            }

            itemView.tv_delivery_date.text= command.deliveryDate.toString()
            itemView.tv_client_name.text= command.client?.toNameString()

            itemView.rv_articles_list.adapter = SimpleListAdapter(
                convertArticleWrapperToItemList(command.articleWrappers)
            )

            itemView.tv_status.text = getCommandStatusByCode(command.statusCode).label
            itemView.tv_status.setTextColor(getColor(context,
                when(command.statusCode){
                    CommandStatusType.IN_PROGRESS.code -> R.color.salamander
                    CommandStatusType.DONE.code -> R.color.green_dark_1
                    CommandStatusType.DELIVERED.code -> R.color.gray_2
                    else -> android.R.color.transparent
                }))

            itemView.cv_command.strokeColor = getColor(context, when(command.statusCode){
                CommandStatusType.IN_PROGRESS.code -> R.color.salamander
                CommandStatusType.DONE.code -> R.color.green_dark_1
                CommandStatusType.DELIVERED.code -> R.color.gray_2
                else -> android.R.color.transparent
            })
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK on !")

        }

    }

    fun convertArticleWrapperToItemList(articleWrappers : List<ArticleWrapper>) : List<ItemList>{
        val list = mutableListOf<ItemList>()
        articleWrappers.forEach {
            val articleLabel = context.getString(R.string.article_name_count, it.article.name, it.count)
            list.add(
                ItemList(
                    articleLabel,
                    isStriked = it.statusCode == ArticleWrapperStatusType.DONE.code
                )
            )
        }
        return list
    }

}
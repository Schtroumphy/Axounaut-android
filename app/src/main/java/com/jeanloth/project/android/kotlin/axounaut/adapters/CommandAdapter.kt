package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import kotlinx.android.synthetic.main.item_command.view.*

class CommandAdapter(
    private var commandList : List<Command>,
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

    fun setItems(articles : List<Command>, statusToDisplay : List<CommandStatusType>){
        this.commandList =  articles
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(command : Command, position : Int){

            itemView.setOnClickListener {
                onClick?.invoke(command)
            }

            Log.d("Command adapter", "In Artcile Holder")
            itemView.tv_delivery_date.text= command.deliveryDate.toString()
            itemView.tv_client_name.text= command.client?.toNameString()

            itemView.rv_articles_list.adapter = SimpleListAdapter(
                convertArticleWrapperToItemList(command.articleWrappers)
            )

            itemView.tv_status.text = command.status.label
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
            list.add(
                ItemList(
                    it.article.name,
                    isStriked = it.status == ArticleWrapperStatusType.DONE
                )
            )
        }
        return list
    }

}
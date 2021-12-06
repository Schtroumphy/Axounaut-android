package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType.Companion.getCommandStatusByCode
import kotlinx.android.synthetic.main.item_command.view.*
import splitties.resources.color
import splitties.views.backgroundColor
import splitties.views.onClick

class CommandAdapter(
    private var commandList: List<Command>,
    private var context: Context
) : RecyclerView.Adapter<CommandAdapter.ArticleHolder>() {

    var onClick: ((Command) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_command, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article: Command = commandList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return commandList.size
    }

    fun setItems(articles: List<Command>) {
        this.commandList = articles.sortedBy { it.statusCode }
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(command: Command) {

            itemView.cv_command.onClick {
                onClick?.invoke(command)
            }

            itemView.tv_delivery_date.text = command.deliveryDate.toString()
            itemView.tv_client_name.text = command.client?.toNameString()

            itemView.rv_articles_list.adapter = SimpleListAdapter(
                convertArticleWrapperToItemList(command.articleWrappers), context
            ).apply {
                onClickItem = { onClick?.invoke(command) }
            }

            itemView.tv_status.text = getCommandStatusByCode(command.statusCode).label
            itemView.tv_status.setTextColor(
                getColor(
                    context,
                    when (command.statusCode) {
                        CommandStatusType.TO_DO.code -> R.color.flax
                        CommandStatusType.IN_PROGRESS.code -> R.color.salamander_attenue
                        CommandStatusType.DONE.code -> R.color.green_light_2
                        CommandStatusType.DELIVERED.code -> R.color.blue_001
                        CommandStatusType.PAYED.code -> R.color.marron_golden_1
                        CommandStatusType.INCOMPLETE_PAYMENT.code -> R.color.red_002
                        else -> android.R.color.black
                    }
                )
            )


            itemView.cv_command.view_status.backgroundTintList =
                ContextCompat.getColorStateList(
                    context, when (command.statusCode) {
                        CommandStatusType.TO_DO.code -> R.color.flax
                        CommandStatusType.IN_PROGRESS.code -> R.color.salamander_attenue
                        CommandStatusType.DONE.code -> R.color.green_light_2
                        CommandStatusType.DELIVERED.code -> R.color.blue_001
                        CommandStatusType.PAYED.code -> R.color.marron_golden_1
                        CommandStatusType.INCOMPLETE_PAYMENT.code -> R.color.red_002
                        else -> android.R.color.black
                    }
                )
        }
    }

    fun convertArticleWrapperToItemList(articleWrappers: List<ArticleWrapper>): List<ItemList> {
        val list = mutableListOf<ItemList>()
        articleWrappers.forEach {
            val articleLabel = context.getString(R.string.article_name, it.article.name)
            val quantity = context.getString(R.string.cross_quantity, it.count.toString())
            list.add(
                ItemList(
                    articleLabel,
                    quantity,
                    isDone = it.statusCode == ArticleWrapperStatusType.DONE.code || it.statusCode == ArticleWrapperStatusType.CANCELED.code,
                    isCanceled = it.statusCode == ArticleWrapperStatusType.CANCELED.code
                )
            )
        }
        return list
    }

}
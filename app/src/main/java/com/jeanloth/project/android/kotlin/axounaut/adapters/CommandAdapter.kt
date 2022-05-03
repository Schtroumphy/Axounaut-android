package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemCommandBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType.Companion.getCommandStatusByCode
import com.jeanloth.project.android.kotlin.domain_models.entities.ItemList
import splitties.views.onClick

class CommandAdapter(
    private var commandList: List<Command>,
    private var context: Context
) : RecyclerView.Adapter<CommandAdapter.ArticleHolder>() {

    var onClick: ((Long) -> Unit)? = null

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
        private val binding = ItemCommandBinding.bind(view)
        fun bind(command: Command) {

            binding.cvCommand.onClick {
                onClick?.invoke(command.idCommand)
            }

            binding.tvDeliveryDate.text = command.deliveryDate.toString().replace("-", "/")
            binding.tvClientName.text = command.client?.toNameString()

            binding.rvArticlesList.adapter = SimpleListAdapter(
                convertArticleWrapperToItemList(command.articleWrappers), context
            ).apply {
                onClickItem = { onClick?.invoke(command.idCommand) }
            }

            binding.tvStatus.text = getCommandStatusByCode(command.statusCode).label
            binding.tvStatus.setTextColor(
                getColor(
                    context,
                    when (command.statusCode) {
                        CommandStatusType.TO_DO.code -> R.color.flax
                        CommandStatusType.IN_PROGRESS.code -> R.color.salamander_attenue
                        CommandStatusType.DONE.code -> R.color.green_light_2
                        CommandStatusType.DELIVERED.code -> R.color.teal_700
                        CommandStatusType.PAYED.code -> R.color.marron_golden_1
                        CommandStatusType.INCOMPLETE_PAYMENT.code -> R.color.red_002
                        else -> android.R.color.black
                    }
                )
            )

            binding.viewStatus.backgroundTintList =
                ContextCompat.getColorStateList(
                    context, when (command.statusCode) {
                        CommandStatusType.TO_DO.code -> R.color.flax
                        CommandStatusType.IN_PROGRESS.code -> R.color.salamander_attenue
                        CommandStatusType.DONE.code -> R.color.green_light_2
                        CommandStatusType.DELIVERED.code -> R.color.teal_700
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
            val articleLabel = context.getString(R.string.article_name, it.article.label)
            val quantity = context.getString(R.string.cross_quantity, it.count.toString())
            list.add(
                ItemList(
                    articleLabel,
                    quantity,
                    isDone = it.statusCode == ArticleWrapperStatusType.DONE.code,
                    isCanceled = it.statusCode == ArticleWrapperStatusType.CANCELED.code,
                )
            )
        }
        return list
    }

}
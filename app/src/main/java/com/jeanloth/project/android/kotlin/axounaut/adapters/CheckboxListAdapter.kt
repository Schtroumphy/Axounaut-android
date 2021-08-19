package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import kotlinx.android.synthetic.main.item_checkbox_list.view.*
import kotlinx.android.synthetic.main.item_checkbox_list.view.tv_label

class CheckboxListAdapter(
    private var items : MutableList<ArticleWrapper>,
    private var enableCheckBox : Boolean = true,
) : RecyclerView.Adapter<CheckboxListAdapter.ItemHolder>()  {

    var onCheckedItem : ((ArticleWrapper, Boolean) -> Unit)? = null
    var onSwipeItem : ((ArticleWrapper, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkbox_list, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel: ArticleWrapper = items[position]
        holder.bind(itemLabel, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items : List<ArticleWrapper>, enableCheckBox : Boolean){
        this.items = items.toMutableList()
        this.enableCheckBox = enableCheckBox
        notifyDataSetChanged()
    }

    fun updateArticleStatus(adapterPosition : Int) {
        onSwipeItem?.invoke(items[adapterPosition], adapterPosition)
    }

    fun onItemDelete(position : Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item : ArticleWrapper, position : Int){

            val articleLabel = itemView.context.getString(R.string.article_name, item.article.name)
            val quantity = itemView.context.getString(R.string.article_quantity, item.count)

            itemView.tv_label.text= if(item.statusCode != ArticleWrapperStatusType.DONE.code && item.statusCode != ArticleWrapperStatusType.CANCELED.code) articleLabel else stringBuilderLabel(articleLabel)
            itemView.tv_cb_quantity.text= if(item.statusCode != ArticleWrapperStatusType.DONE.code && item.statusCode != ArticleWrapperStatusType.CANCELED.code) quantity else stringBuilderLabel(quantity)
            itemView.cb_item.isChecked = item.statusCode == ArticleWrapperStatusType.DONE.code
            itemView.cb_item.isEnabled = enableCheckBox

            itemView.cb_item.visibility = if(item.statusCode == ArticleWrapperStatusType.CANCELED.code) INVISIBLE else VISIBLE
            if(item.statusCode == ArticleWrapperStatusType.CANCELED.code) {
                itemView.tv_label.setTextColor(getColor(itemView.context, R.color.red_002))
                itemView.tv_cb_quantity.setTextColor(getColor(itemView.context, R.color.red_002))
                itemView.tv_label_canceled.visibility = VISIBLE
            } else {
                itemView.tv_label.setTextColor(getColor(itemView.context, R.color.gray_2))
                itemView.tv_label_canceled.visibility = GONE
            }
            itemView.tv_label.setOnClickListener {
                if(itemView.cb_item.isEnabled)
                    itemView.cb_item.isChecked = !itemView.cb_item.isChecked
            }

            itemView.cb_item.setOnCheckedChangeListener{ _, isChecked ->
                item.statusCode = if(isChecked) ArticleWrapperStatusType.DONE.code else ArticleWrapperStatusType.IN_PROGRESS.code
                onCheckedItem?.invoke(item, isChecked)
            }
        }
        }
    }

    fun stringBuilderLabel(label : String) : SpannableStringBuilder{
        return SpannableStringBuilder().apply {
            append(label)
            setSpan(
                StrikethroughSpan(),
                this.length - label.length,
                this.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }
    }

package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.ItemList
import kotlinx.android.synthetic.main.item_dot_list.view.*
import splitties.views.onClick

class SimpleListAdapter(
    private var items : List<ItemList>,
    private val context : Context
) : RecyclerView.Adapter<SimpleListAdapter.ItemHolder>()  {

    var onClickItem : (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dot_list, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel: ItemList = items[position]
        holder.bind(itemLabel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items : List<ItemList>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item : ItemList){

            itemView.onClick {
                onClickItem?.invoke()
            }

            itemView.tv_quantity.text = if(!item.isDone && !item.isCanceled){
                item.quantity
            } else {
                stringBuilderLabel(item.quantity, item.isCanceled)
            }

            itemView.tv_label.text = if(!item.isDone && !item.isCanceled){
                item.label
            } else {
                stringBuilderLabel(item.label, item.isCanceled)
            }
        }

    }

    fun stringBuilderLabel(label : String, isCanceled  : Boolean) : SpannableStringBuilder{
        return SpannableStringBuilder().apply {
            append(label)
            setSpan(
                StrikethroughSpan(),
                this.length - label.length,
                this.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if(isCanceled) setSpan(ForegroundColorSpan(getColor(context, R.color.red_002)), this.length - label.length,
                this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
    }

}
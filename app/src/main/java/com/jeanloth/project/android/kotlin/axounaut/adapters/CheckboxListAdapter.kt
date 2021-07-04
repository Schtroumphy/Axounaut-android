package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import kotlinx.android.synthetic.main.item_checkbox_list.view.*

class CheckboxListAdapter(
    private var items : List<ArticleWrapper>,
) : RecyclerView.Adapter<CheckboxListAdapter.ItemHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkbox_list, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel: ArticleWrapper = items[position]
        holder.bind(itemLabel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items : List<ArticleWrapper>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item : ArticleWrapper){

            Log.d("Article adapter", "In Article Holder isEditMode")
            itemView.tv_label.text= if(item.status != ArticleWrapperStatusType.DONE) item.article.name else stringBuilderLabel(item.article.name)
            itemView.cb_item.isChecked = item.status == ArticleWrapperStatusType.DONE
            itemView.cb_item.isEnabled = item.status != ArticleWrapperStatusType.DONE

            itemView.tv_label.setOnClickListener {
                itemView.cb_item.isChecked = !itemView.cb_item.isChecked
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

}
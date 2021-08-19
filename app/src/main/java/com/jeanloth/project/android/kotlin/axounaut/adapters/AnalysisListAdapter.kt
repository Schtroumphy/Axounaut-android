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
import com.jeanloth.project.android.kotlin.domain_models.entities.AnalysisList
import com.jeanloth.project.android.kotlin.domain_models.entities.ItemList
import kotlinx.android.synthetic.main.item_analysis_list.view.*
import kotlinx.android.synthetic.main.item_dot_list.view.*
import kotlinx.android.synthetic.main.item_dot_list.view.tv_quantity

class AnalysisListAdapter(
    private var items : List<AnalysisList>,
    private val context : Context
) : RecyclerView.Adapter<AnalysisListAdapter.ItemHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_analysis_list, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel: AnalysisList = items[position]
        holder.bind(itemLabel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items : List<AnalysisList>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item : AnalysisList){

            itemView.tv_analysis_label.text = item.label
            itemView.tv_analysis_quantity.text = context.getString(R.string.cross_quantity,item.number.toString())
            itemView.tv_analysis_price.text = context.getString(R.string.price_euro,item.totalAmount.toString())
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
            if(isCanceled) setSpan(ForegroundColorSpan(getColor(context, R.color.red_001)), this.length - label.length,
                this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
    }

}
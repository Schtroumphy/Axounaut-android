package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.android.synthetic.main.item_checkbox_article.view.*
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper


class CheckboxTextViewAdapter(
    private var items : MutableList<IngredientWrapper>
) : RecyclerView.Adapter<CheckboxTextViewAdapter.ItemHolder>()  {

    var onCheckedItem : ((IngredientWrapper, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkbox_article, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel = items[position]
        holder.bind(itemLabel, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items : List<IngredientWrapper>){
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item : IngredientWrapper, position : Int){

            val articleLabel = itemView.context.getString(R.string.article_name, item.ingredient.label)

            itemView.tv_label.text= articleLabel

            itemView.tv_label.setOnClickListener {
                if(itemView.cb_item.isEnabled)
                    itemView.cb_item.isChecked = !itemView.cb_item.isChecked
            }

            itemView.cb_item.setOnCheckedChangeListener{ _, isChecked ->
                onCheckedItem?.invoke(item, isChecked)
            }
        }
        }
    }


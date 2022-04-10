package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemCheckboxArticleBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.PrevisionalWrapper

class CheckboxShoppingAdapter(
    private var items: MutableList<PrevisionalWrapper>
) : RecyclerView.Adapter<CheckboxShoppingAdapter.ItemHolder>() {

    var onCheckedItem: ((PrevisionalWrapper, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox_article, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val itemLabel = items[position]
        holder.bind(itemLabel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<PrevisionalWrapper>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCheckboxArticleBinding.bind(view)
        fun bind(item: PrevisionalWrapper) {

            val articleLabel = itemView.context.getString(R.string.article_name, item.ingredient.label + "  x ${item.needed}")

            binding.tvLabel.apply {
                text = articleLabel
                setOnClickListener {
                    if (binding.cbItem.isEnabled)
                        binding.cbItem.isChecked = !binding.cbItem.isChecked
                }
            }

            binding.cbItem.isChecked = item.isSelected

            binding.cbItem.setOnCheckedChangeListener { _, isChecked ->
                onCheckedItem?.invoke(item, isChecked)
            }
        }
    }
}



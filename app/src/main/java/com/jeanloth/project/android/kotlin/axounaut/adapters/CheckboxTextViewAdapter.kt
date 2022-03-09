package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemCheckboxArticleBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper

class CheckboxTextViewAdapter(
    private var items: MutableList<IngredientWrapper>
) : RecyclerView.Adapter<CheckboxTextViewAdapter.ItemHolder>() {

    var onCheckedItem: ((IngredientWrapper, Boolean) -> Unit)? = null

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

    fun setItems(items: List<IngredientWrapper>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCheckboxArticleBinding.bind(view)
        fun bind(item: IngredientWrapper) {

            val articleLabel = itemView.context.getString(R.string.article_name, item.ingredient.label)

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


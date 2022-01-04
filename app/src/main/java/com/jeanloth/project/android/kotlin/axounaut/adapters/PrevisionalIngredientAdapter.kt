package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemIngredientPrevisionalBinding
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemStockIngredientBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.PrevisionalWrapper
import kotlinx.android.synthetic.main.item_article.view.tv_name
import kotlinx.android.synthetic.main.item_stock_ingredient.view.*

class PrevisionalIngredientAdapter(
    private var previsionalWrappers: List<PrevisionalWrapper>,
    private val context: Context
) : RecyclerView.Adapter<PrevisionalIngredientAdapter.PrevisionalIngredientHolder>() {

    lateinit var binding: ItemIngredientPrevisionalBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevisionalIngredientHolder {
        binding =
            ItemIngredientPrevisionalBinding.inflate(LayoutInflater.from(context), parent, false)
        return PrevisionalIngredientHolder(binding)
    }

    enum class PositionInList {
        ALONE, FIRST, BETWEEN, LAST
    }

    override fun onBindViewHolder(holder: PrevisionalIngredientHolder, position: Int) {
        val ingredient = previsionalWrappers[position]

        val ingredientsByCountStatus = previsionalWrappers.sortedBy { it.delta }
        val size = ingredientsByCountStatus.size - 1

        val positionOfMyItem = ingredientsByCountStatus.indexOf(ingredient)
        val positionInList = when {
            positionOfMyItem == size -> if (size == 0) PositionInList.ALONE else PositionInList.LAST
            positionOfMyItem == 0 && size > 0 -> PositionInList.FIRST
            positionOfMyItem < size && size > 0 -> PositionInList.BETWEEN
            else -> PositionInList.BETWEEN
        }

        holder.bind(ingredient, position, positionInList)
    }

    override fun getItemCount(): Int {
        return previsionalWrappers.size
    }

    init {
        sortIngredients(false)
    }

    fun setItems(pws: List<PrevisionalWrapper>) {
        this.previsionalWrappers = pws
        sortIngredients()
    }

    inner class PrevisionalIngredientHolder(binding: ItemIngredientPrevisionalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            previsionalWrapper: PrevisionalWrapper,
            position: Int,
            positionInList: PositionInList
        ) {

            binding.apply {
                tvName.text = previsionalWrapper.ingredient.label
                tvCountActual.text = previsionalWrapper.actual.toString()
                tvCountNeed.text = previsionalWrapper.needed.toString()
                tvCountDelta.text = previsionalWrapper.delta.toString()

                // Line background drawable
                ivLine.background = getDrawable(
                    itemView.context, when (positionInList) {
                        PositionInList.FIRST -> R.drawable.rounded_top_line
                        PositionInList.BETWEEN -> R.drawable.stock_line
                        PositionInList.LAST -> R.drawable.rounded_bottom_line
                        PositionInList.ALONE -> R.drawable.rounded_line
                    }
                )


                // Line color by count status type
                ivLine.backgroundTintList = ColorStateList.valueOf(
                    context.resources.getColor(
                        when {
                            previsionalWrapper.delta < 0 -> R.color.red_002
                            previsionalWrapper.delta == 0f -> R.color.orange_002
                            previsionalWrapper.delta > 0 -> R.color.green_light_2
                            else -> R.color.gray_1
                        }
                    )
                )

                tvCountDelta.setTextColor(
                    context.resources.getColor(
                        when {
                            previsionalWrapper.delta < 0 -> R.color.red_001
                            previsionalWrapper.delta == 0f -> R.color.orange_002
                            previsionalWrapper.delta > 0 -> R.color.green_light_2
                            else -> R.color.gray_1
                        }
                    )
                )
            }

            // Add margin bottom if last item of count status type list
            val params = itemView.iv_line.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin =
                if (positionInList == PositionInList.LAST || positionInList == PositionInList.ALONE) 20 else 0
        }
    }

private fun sortIngredients(reload: Boolean = true) {
    previsionalWrappers = previsionalWrappers.sortedBy { it.delta }
    if (reload) notifyDataSetChanged()
}

}
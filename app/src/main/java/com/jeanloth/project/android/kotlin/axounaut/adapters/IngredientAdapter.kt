package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemStockIngredientBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
/**
 * @param ingredients Ingredients to display
 * @param isEditMode Mode where we can edit quantity of ingredient. If false, edit button are hidden.
 * @param context Context needed
 * @param addArticleMode Mode where we do not sort the list by quantty and we do not show status line
 * **/
class IngredientAdapter(
    private var ingredients : List<IngredientWrapper>,
    private var isEditMode : Boolean = true,
    private val context : Context,
    private var addArticleMode : Boolean = false
) : RecyclerView.Adapter<IngredientAdapter.ArticleHolder>()  {

    var onAddMinusClick : ((IngredientWrapper) -> Unit)? = null
    var displayNoArticlesError : ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stock_ingredient, parent, false)
        return ArticleHolder(itemView)
    }

    enum class PositionInList{
        ALONE, FIRST, BETWEEN, LAST
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val ingredient = ingredients[position]

        val ingredientsByCountStatus = ingredients.filter { it.countStatusType == ingredient.countStatusType  }
        val size = ingredientsByCountStatus.size -1

        val positionOfMyItem = ingredientsByCountStatus.indexOf(ingredient)
        val positionInList = when{
            positionOfMyItem == size -> if(size == 0) PositionInList.ALONE else PositionInList.LAST
            positionOfMyItem == 0 && size > 0 -> PositionInList.FIRST
            positionOfMyItem < size  && size > 0 -> PositionInList.BETWEEN
            else -> PositionInList.BETWEEN
        }

        holder.bind(ingredient, positionInList, position)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    init {
        if(!addArticleMode) sortIngredients(false)
    }

    fun setItems(ingredients : List<IngredientWrapper>, isEditMode : Boolean = true, addArticleMode : Boolean = false){
        this.ingredients = ingredients
        this.isEditMode = isEditMode
        this.addArticleMode = addArticleMode
        if(!addArticleMode) sortIngredients() else notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ItemStockIngredientBinding.bind(view)
        fun bind(ingredientWrapper: IngredientWrapper, positionInList: PositionInList, position: Int){

            binding.tvName.text= ingredientWrapper.ingredient.label

            val count = ingredientWrapper.quantity

            setupElementVisibility(count.toString())

            if(!addArticleMode) {
                binding.ivLine.visibility = VISIBLE
                binding.viewSpacer.visibility = GONE
                // Line background drawable
                binding.ivLine.background = getDrawable(
                    itemView.context, when (positionInList) {
                        PositionInList.FIRST -> R.drawable.rounded_top_line
                        PositionInList.BETWEEN -> R.drawable.stock_line
                        PositionInList.LAST -> R.drawable.rounded_bottom_line
                        PositionInList.ALONE -> R.drawable.rounded_line
                    }
                )

                // Line color by count status type
                binding.ivLine.backgroundTintList = ColorStateList.valueOf(
                    getColor(
                        context, when (ingredientWrapper.countStatusType) {
                            IngredientWrapper.CountStatus.LOW -> R.color.red_002
                            IngredientWrapper.CountStatus.MEDIUM -> R.color.orange_002
                            IngredientWrapper.CountStatus.LARGE -> R.color.green_light_2
                        }
                    )
                )

            } else {
                binding.ivLine.visibility = GONE
                binding.viewSpacer.visibility = VISIBLE
            }
            // Add margin bottom if last item of count status type list
            val params = binding.ivLine.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = if(positionInList == PositionInList.LAST || positionInList == PositionInList.ALONE )  20 else 0

            if(isEditMode){
                binding.ibMinus.visibility = if(count > 0) VISIBLE else GONE

                binding.ibAdd.setOnClickListener {
                    ingredientWrapper.quantity = count + 0.5f
                    onAddMinusClick?.invoke(ingredientWrapper)
                    if(!addArticleMode) sortIngredients() else notifyItemChanged(position)
                }

                binding.ibMinus.setOnClickListener {
                    if(count > 0) {
                        ingredientWrapper.quantity = count - 0.5f
                    }
                    onAddMinusClick?.invoke(ingredientWrapper)
                    if(!addArticleMode) sortIngredients() else notifyItemChanged(position)
                }
            }

        }

        private fun setupElementVisibility(count : String) {
            binding.tvCount.text = if(isEditMode) count else context.resources.getString(R.string.x_count, count)
            binding.ibAdd.visibility = if(isEditMode) VISIBLE else GONE
            if(!isEditMode) binding.ibMinus.visibility = GONE
        }

    }

    private fun sortIngredients(reload : Boolean = true){
        ingredients = ingredients.sortedBy { it.quantityType }.sortedBy { it.countStatusType }
        if(reload) notifyDataSetChanged()

    }

}
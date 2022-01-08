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
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import kotlinx.android.synthetic.main.item_article.view.ib_add
import kotlinx.android.synthetic.main.item_article.view.ib_minus
import kotlinx.android.synthetic.main.item_article.view.tv_count
import kotlinx.android.synthetic.main.item_article.view.tv_name
import kotlinx.android.synthetic.main.item_stock_ingredient.view.*

class IngredientAdapter(
    private var ingredients : List<IngredientWrapper>,
    private var isEditMode : Boolean = true,
    private val context : Context
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

        holder.bind(ingredient, positionInList)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    init {
        sortIngredients(false)
    }

    fun setItems(ingredients : List<IngredientWrapper>, isEditMode : Boolean = true){
        this.ingredients = ingredients
        this.isEditMode = isEditMode
        sortIngredients()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(ingredientWrapper: IngredientWrapper, positionInList: PositionInList){

            itemView.tv_name.text= ingredientWrapper.ingredient.label

            val count = ingredientWrapper.quantity

            setupElementVisibility(count.toString())

            // Line background drawable
            itemView.iv_line.background = getDrawable(itemView.context, when(positionInList){
                PositionInList.FIRST -> R.drawable.rounded_top_line
                PositionInList.BETWEEN -> R.drawable.stock_line
                PositionInList.LAST -> R.drawable.rounded_bottom_line
                PositionInList.ALONE -> R.drawable.rounded_line
            })

            // Line color by count status type
            itemView.iv_line.backgroundTintList = ColorStateList.valueOf(getColor(context, when(ingredientWrapper.countStatusType){
                IngredientWrapper.CountStatus.LOW -> R.color.red_002
                IngredientWrapper.CountStatus.MEDIUM -> R.color.orange_002
                IngredientWrapper.CountStatus.LARGE -> R.color.green_light_2
            }))

            // Add margin bottom if last item of count status type list
            val params = itemView.iv_line.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = if(positionInList == PositionInList.LAST || positionInList == PositionInList.ALONE )  20 else 0

            if(isEditMode){
                itemView.ib_minus.visibility = if(count > 0) VISIBLE else GONE

                itemView.ib_add.setOnClickListener {
                    ingredientWrapper.quantity = count + 0.5f
                    onAddMinusClick?.invoke(ingredientWrapper)
                    sortIngredients()
                }

                itemView.ib_minus.setOnClickListener {
                    if(count > 0) {
                        ingredientWrapper.quantity = count - 0.5f
                    }
                    onAddMinusClick?.invoke(ingredientWrapper)
                    sortIngredients()
                }
            }

        }

        private fun setupElementVisibility(count : String) {
            itemView.tv_count.text = if(isEditMode) count else context.resources.getString(R.string.x_count, count)
            itemView.ib_add.visibility = if(isEditMode) VISIBLE else GONE
            if(!isEditMode) itemView.ib_minus.visibility = GONE
        }

    }

    private fun sortIngredients(reload : Boolean = true){
        ingredients = ingredients.sortedBy { it.quantityType }.sortedBy { it.countStatusType }
        if(reload) notifyDataSetChanged()
    }

}
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
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemRecipeIngredientBinding
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemStockIngredientBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
/**
 * @param ingredients Ingredients to display
 * @param isEditMode Mode where we can edit quantity of ingredient. If false, edit button are hidden.
 * @param context Context needed
 * @param addArticleMode Mode where we do not sort the list by quantty and we do not show status line
 * **/
class RecipeContentAdapter(
    private var ingredients : List<IngredientWrapper>,
    private val context : Context,
) : RecyclerView.Adapter<RecipeContentAdapter.ArticleHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipe_ingredient, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient, position)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setItems(ingredients : List<IngredientWrapper>, isEditMode : Boolean = true, addArticleMode : Boolean = false){
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ItemRecipeIngredientBinding.bind(view)
        fun bind(ingredientWrapper: IngredientWrapper, position: Int){

            binding.tvName.text= ingredientWrapper.ingredient.label
            val count = ingredientWrapper.quantity.toString()
            binding.tvCount.text =  context.resources.getString(R.string.x_count, count)
        }

    }

}
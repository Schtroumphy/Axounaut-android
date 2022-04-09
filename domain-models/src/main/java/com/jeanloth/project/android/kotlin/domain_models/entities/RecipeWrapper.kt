package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class RecipeWrapper(
    val id : Long = 0,
    var ingredient : Ingredient,
    var quantity : Float = 0f
) : Serializable  {

    companion object {

        fun List<IngredientWrapper>.toRecipeWrapper(keepId : Boolean = false) : List<RecipeWrapper>{
            return this.map {
                RecipeWrapper(
                    id = if(keepId) it.id else 0L,
                    ingredient = it.ingredient,
                    quantity = it.quantity
                )
            }
        }

        fun createRecipeWrapperList(ingredients : List<Ingredient>) : List<RecipeWrapper>{
            val listResult = mutableListOf<RecipeWrapper>()
            ingredients.forEach { ingredient ->
                listResult.add(
                    RecipeWrapper(
                        ingredient = ingredient
                    )
                )
            }
            return listResult
        }
    }
}

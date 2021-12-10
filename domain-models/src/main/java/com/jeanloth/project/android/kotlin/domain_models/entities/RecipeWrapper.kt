package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class RecipeWrapper(
    val id : Long = 0,
    var articleId : Long? = null,
    var ingredient : Ingredient,
    var quantity : Double = 0.0,
    var quantityType : IngredientQuantityType = IngredientQuantityType.KG

) : Serializable  {

    companion object {

        fun createWrapperList(ingredients : List<Ingredient>) : List<RecipeWrapper>{
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

package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class IngredientWrapper(
    val id : Long = 0,
    var ingredient : Ingredient,
    var quantity : Float = 0f,
    var isSelected: Boolean = false,
    var quantityType : IngredientQuantityType = IngredientQuantityType.G

) : Serializable  {

    val countStatusType : CountStatus
    get() = when{
        quantity == 0f -> CountStatus.LOW
        quantity < 2 -> CountStatus.MEDIUM
        else -> CountStatus.LARGE
    }

    companion object {

        fun List<RecipeWrapper>.toIngredientWrapper() : List<IngredientWrapper>{
            return this.map {
                IngredientWrapper(
                    id = 0L,
                    ingredient = it.ingredient,
                    quantity = it.quantity
                )
            }
        }

        fun createWrapperList(ingredients : List<Ingredient>) : List<IngredientWrapper>{
            val listResult = mutableListOf<IngredientWrapper>()
            ingredients.forEach { ingredient ->
                listResult.add(
                    IngredientWrapper(
                        ingredient = ingredient
                    )
                )
            }
            return listResult
        }
    }

    enum class CountStatus{
        LOW, MEDIUM, LARGE
    }
}

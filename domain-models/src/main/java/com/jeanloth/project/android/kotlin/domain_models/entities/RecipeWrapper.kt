package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class IngredientWrapper(
    val id : Long = 0,
    var stockId : Long? = null,
    var ingredient : Ingredient,
    var quantity : Double = 0.0,
    var quantityType : IngredientQuantityType = IngredientQuantityType.KG

) : Serializable  {

    val totalIngredientWrapperPrice : Double
    get() = quantity * ingredient.price

    val countStatusType : CountStatus
    get() = when{
        quantity == 0.0 -> CountStatus.LOW
        quantity < 2 -> CountStatus.MEDIUM
        else -> CountStatus.LARGE
    }

    companion object {

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

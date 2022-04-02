package com.jeanloth.project.android.kotlin.axounaut

import com.jeanloth.project.android.kotlin.domain_models.entities.Ingredient
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper

object Mock {

    val mock = mutableListOf(
        IngredientWrapper(ingredient = Ingredient(label = "Farine"), quantity = 4.0f),
        IngredientWrapper(ingredient = Ingredient(label = "Beurre 500g"), quantity = 4.0f, quantityType = IngredientQuantityType.G),
        IngredientWrapper(ingredient = Ingredient(label = "Lait 1L"), quantity = 0.0f, quantityType = IngredientQuantityType.L),
        IngredientWrapper(ingredient = Ingredient(label = "Epices"), quantity = 1.0f, quantityType = IngredientQuantityType.G),
        IngredientWrapper(ingredient = Ingredient(label = "Autre 1"), quantity = 1.0f, quantityType = IngredientQuantityType.L),
        IngredientWrapper(ingredient = Ingredient(label = "Test"), quantity = 0.0f, quantityType = IngredientQuantityType.L),
        IngredientWrapper(ingredient = Ingredient(label = "Test 2"), quantity = 2.0f, quantityType = IngredientQuantityType.G),
        IngredientWrapper(ingredient = Ingredient(label = "Test 3"), quantity = 2.0f, quantityType = IngredientQuantityType.G),
        IngredientWrapper(ingredient = Ingredient(label = "Test 4"), quantity = 0.0f, quantityType = IngredientQuantityType.G),
    )
}
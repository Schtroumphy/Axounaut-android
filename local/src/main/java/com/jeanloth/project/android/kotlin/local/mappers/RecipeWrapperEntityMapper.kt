package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper
import com.jeanloth.project.android.kotlin.local.entities.RecipeWrapperEntity

class RecipeWrapperEntityMapper(val ingredientEntityMapper : IngredientEntityMapper) : Mapper<RecipeWrapper, RecipeWrapperEntity> {

    override fun from(t: RecipeWrapperEntity): RecipeWrapper {

        return RecipeWrapper(
            id = t.id,
            ingredient = ingredientEntityMapper.from(t.ingredient.target),
            quantity = t.quantity
        )
    }

    override fun to(t: RecipeWrapper): RecipeWrapperEntity {
        val recipeWrapperEntity =  RecipeWrapperEntity(
            id = t.id,
            quantity = t.quantity
        )
        recipeWrapperEntity.ingredient.target = ingredientEntityMapper.to(t.ingredient)
        return recipeWrapperEntity
    }
}
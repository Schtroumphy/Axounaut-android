package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType.Companion.fromVal
import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper
import com.jeanloth.project.android.kotlin.local.entities.RecipeWrapperEntity

class RecipeWrapperEntityMapper(val ingredientEntityMapper : IngredientEntityMapper) : Mapper<RecipeWrapper, RecipeWrapperEntity> {

    override fun from(t: RecipeWrapperEntity): RecipeWrapper {

        return RecipeWrapper(
            id = t.ingredientWrapperId,
            articleId = t.article.targetId, // TO one
            ingredient = ingredientEntityMapper.from(t.ingredient.target),
            quantity = t.quantity,
            quantityType = t.quantityTypeLabel.fromVal()
        )
    }

    override fun to(t: RecipeWrapper): RecipeWrapperEntity {
        val recipeWrapperEntity =  RecipeWrapperEntity(
            ingredientWrapperId = t.id,
            quantity = t.quantity,
            quantityTypeLabel = t.quantityType.label
        )
        t.articleId?.let {
            recipeWrapperEntity.article.targetId = it
        }
        recipeWrapperEntity.ingredient.target = ingredientEntityMapper.to(t.ingredient)
        return recipeWrapperEntity
    }
}
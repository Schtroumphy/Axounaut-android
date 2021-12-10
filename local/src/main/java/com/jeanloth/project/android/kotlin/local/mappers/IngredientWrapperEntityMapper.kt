package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType.Companion.fromVal
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import com.jeanloth.project.android.kotlin.local.entities.IngredientWrapperEntity

class IngredientWrapperEntityMapper(val ingredientEntityMapper : IngredientEntityMapper) : Mapper<IngredientWrapper, IngredientWrapperEntity> {

    override fun from(t: IngredientWrapperEntity): IngredientWrapper {

        return IngredientWrapper(
            id = t.ingredientWrapperId,
            ingredient = ingredientEntityMapper.from(t.ingredient.target),
            quantity = t.quantity,
            quantityType = t.quantityTypeLabel.fromVal()
        )
    }

    override fun to(t: IngredientWrapper): IngredientWrapperEntity {
        val articleWrapperEntity =  IngredientWrapperEntity(
            ingredientWrapperId = t.id,
            quantity = t.quantity,
            quantityTypeLabel = t.quantityType.label
        )
        articleWrapperEntity.ingredient.target = ingredientEntityMapper.to(t.ingredient)
        return articleWrapperEntity
    }
}
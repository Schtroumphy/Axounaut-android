package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Ingredient
import com.jeanloth.project.android.kotlin.local.entities.IngredientEntity

class IngredientEntityMapper : Mapper<Ingredient, IngredientEntity> {

    override fun from(t: IngredientEntity): Ingredient{
        return Ingredient(
            id = t.id,
            label = t.label
        )
    }

    override fun to(t: Ingredient): IngredientEntity {
        return IngredientEntity(
            id = t.id,
            label = t.label
        )
    }
}
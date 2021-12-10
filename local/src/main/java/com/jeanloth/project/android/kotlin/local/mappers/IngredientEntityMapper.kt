package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Ingredient
import com.jeanloth.project.android.kotlin.local.entities.ProductEntity

class ProductEntityMapper : Mapper<Ingredient, ProductEntity> {

    override fun from(t: ProductEntity): Ingredient{
        return Ingredient(
            id = t.id,
            label = t.label,
            price = t.price
        )
    }

    override fun to(t: Ingredient): ProductEntity {
        return ProductEntity(
            id = t.id,
            label = t.label,
            price = t.price
        )
    }
}
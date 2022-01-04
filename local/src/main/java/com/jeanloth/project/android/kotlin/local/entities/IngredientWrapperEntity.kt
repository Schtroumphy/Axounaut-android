package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType
import io.objectbox.BoxStore
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class IngredientWrapperEntity(
    @Id
    var ingredientWrapperId : Long = 0,

    var quantity : Float = 0F,
    var quantityTypeLabel : String = IngredientQuantityType.KG.label

) : Entity {

    var ingredient: ToOne<IngredientEntity> = ToOne(this, IngredientWrapperEntity_.ingredient)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
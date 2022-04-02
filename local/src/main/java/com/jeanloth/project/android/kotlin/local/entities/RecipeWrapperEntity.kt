package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.BoxStore
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class RecipeWrapperEntity(
    @Id
    var id : Long = 0,
    var quantity : Float = 0f
) : Entity {

    var ingredient: ToOne<IngredientEntity> = ToOne(this, RecipeWrapperEntity_.ingredient)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
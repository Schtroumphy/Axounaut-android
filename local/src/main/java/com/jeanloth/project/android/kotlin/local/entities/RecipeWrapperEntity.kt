package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientQuantityType
import io.objectbox.BoxStore
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class RecipeWrapperEntity(
    @Id
    var ingredientWrapperId : Long = 0,

    var quantity : Double = 0.0,
    var quantityTypeLabel : String = IngredientQuantityType.KG.label

) : Entity {

    var ingredient: ToOne<IngredientEntity> = ToOne(this, RecipeWrapperEntity_.ingredient)
    var article: ToOne<ArticleEntity> = ToOne(this, RecipeWrapperEntity_.article)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
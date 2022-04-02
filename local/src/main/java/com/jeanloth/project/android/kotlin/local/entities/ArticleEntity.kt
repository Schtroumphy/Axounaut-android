package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import io.objectbox.BoxStore
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class ArticleEntity(
    @Id
    var id: Long = 0,
    var label: String = "",
    var price: Double = 0.0,
    val timeOrdered: Int = 0,
    val category: Int = ArticleCategory.SALTED.code
) : Entity {

    var recipeWrappers: ToMany<RecipeWrapperEntity> = ToMany(this, ArticleEntity_.recipeWrappers)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
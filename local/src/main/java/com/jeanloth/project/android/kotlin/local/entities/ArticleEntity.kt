package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
class ArticleEntity(
    @Id
    var id: Long = 0,
    var name: String = "",
    var price : Double = 0.0,
    val timeOrdered : Int = 0,
    val category : Int = ArticleCategory.SALTED.code
) : Entity
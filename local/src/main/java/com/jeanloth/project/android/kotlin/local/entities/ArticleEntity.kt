package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
data class ArticleEntity(
    @Id
    var id: Long = 0,
    var name: String = "",
    var count : Int = 0,
    var unitPrice : Double = 10.0
) : Entity
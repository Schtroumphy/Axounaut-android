package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ArticleEntity(
    @Id
    var id: Long = 0,
    var name: String? = null
)
package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
data class ArticleEntity(
    @Id
    var id: Long = 0,
    var name: String = ""
) : Entity
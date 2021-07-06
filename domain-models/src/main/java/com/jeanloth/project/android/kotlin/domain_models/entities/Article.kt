package com.jeanloth.project.android.kotlin.domain_models.entities

data class Article(
        val id : Long = 0,
        val name : String,
        val price : Double = 0.0,
        val category : Int = ArticleCategory.SALTED.code,
)

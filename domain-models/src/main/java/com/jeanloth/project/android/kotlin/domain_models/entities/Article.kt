package com.jeanloth.project.android.kotlin.domain_models.entities

data class Article(
        val name : String,
        val category : ArticleCategory? = ArticleCategory.SWEET, // Sucré ou salé
        var count : Int = 0,
        var unitPrice : Double = 0.0
)

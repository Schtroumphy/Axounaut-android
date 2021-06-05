package com.jeanloth.project.android.kotlin.domain.entities

data class Article(
        val name : String,
        val isSweet : Boolean = false, // Sucré ou salé
        var count : Int = 0,
        var unitPrice : Double = 10.0
)

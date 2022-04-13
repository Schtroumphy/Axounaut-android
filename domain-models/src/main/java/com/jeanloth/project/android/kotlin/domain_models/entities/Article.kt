package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class Article(
    val id : Long = 0,
    val label : String = "",
    val price : Int = 0,
    val preparingTime : Float = 0f,
    var timeOrdered : Int = 0,
    var isHidden : Boolean = false,
    val category : Int = ArticleCategory.SALTED.code,
    var recipeIngredients : List<RecipeWrapper> = emptyList()
): Serializable

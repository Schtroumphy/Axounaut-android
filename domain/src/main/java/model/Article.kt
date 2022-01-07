package com.jeanloth.project.android.kotlin.domain_models.entities

data class Article(
        val id : Long = 0,
        val label : String = "",
        val price : Double = 0.0,
        var timeOrdered : Int = 0,
        val category : Int = ArticleCategory.SALTED.code,
        var recipeIngredients : List<RecipeWrapper> = emptyList()
){

}

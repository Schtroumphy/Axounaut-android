package com.jeanloth.project.android.kotlin.domain_models.entities

enum class ArticleCategory( val code : Int, val label : String) {
    SALTED (1, "Salé"),
    SWEET(2, "Sucré");

    companion object{
        fun getArticleCategoryFromCode(code : Int) : ArticleCategory = when(code) {
            SALTED.code -> SALTED
            SWEET.code -> SWEET
            else -> SALTED
        }

        fun getArticleCategoryFromLabel(label : String?) : ArticleCategory = when(label) {
            SALTED.label -> SALTED
            SWEET.label -> SWEET
            else -> SALTED
        }
    }
}
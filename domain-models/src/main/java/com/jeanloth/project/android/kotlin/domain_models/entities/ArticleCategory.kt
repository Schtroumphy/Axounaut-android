package com.jeanloth.project.android.kotlin.domain_models.entities

enum class ArticleCategory( val label : String) {
    SALTED ("Salé"),
    SWEET("Sucré"),
    OTHER ("Autre");

    companion object{
        fun getArticleCategoryFromLabel(label : String) : ArticleCategory = when(label) {
            SALTED.label -> SALTED
            SWEET.label -> SWEET
            OTHER.label -> OTHER
            else -> OTHER
        }
    }
}
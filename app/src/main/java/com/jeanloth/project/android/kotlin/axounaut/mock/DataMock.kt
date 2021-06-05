package com.jeanloth.project.android.kotlin.axounaut.mock

import com.jeanloth.project.android.kotlin.domain.entities.Article

object DataMock {

    val articles = listOf(
        Article("Paini au beurre 1kg", unitPrice = 20.0),
        Article("Paini au beurre 500g", unitPrice = 10.0),
        Article("Chocolat", true, unitPrice = 10.0),
        Article("Rolls Kanèl", true, unitPrice = 10.0),
        Article("Petits pain", unitPrice = 0.50),
        Article("Petits pain x 20.0", unitPrice = 10.0),
        Article("Pain au choco x 10", true, unitPrice = 15.0),
        Article("Flan au coco", true, unitPrice = 10.0),
        Article("Velouté", unitPrice = 12.0),
        Article("Pommes cannelles", unitPrice = 15.0),
    )

    val articles2 = listOf(
        Article("Chocolat"),
        Article("Rolls Kanèl", true),
        Article("Petits pain x 10"),
        Article("Pain au choco x 10"),
        Article("Flan au coco", true),
        Article("Velouté"),
        Article("Pommes cannelles", true),
    )
}
package com.jeanloth.project.android.kotlin.axounaut.mock

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command

object DataMock {

    val articles = listOf(
        Article(label = "Pain au beurre 1kg", price = 20.0),
        Article(label = "Pain au beurre 500g", price = 10.0),
        Article(label = "Chocolat", price = 10.0),
        Article(label = "Rolls Kanèl", price = 10.0),
        Article(label = "Petits pain", price = 0.50),
        Article(label = "Petits pain x 20.0", price = 10.0),
        Article(label = "Pain au choco x 10", price = 15.0),
        Article(label = "Flan au coco", price = 10.0),
        Article(label = "Velouté", price = 12.0),
        Article(label = "Pommes cannelles", price = 15.0),
    )

    val articleWrappers = createWrapperList(articles)

    val articleWrapper1 = ArticleWrapper(
        article = Article(label = "Pain au choco", price = 15.0)
    )
    val articleWrapper2 = ArticleWrapper(
        article = Article(label = "Rolls Kanèl", price = 10.0),
        statusCode = ArticleWrapperStatusType.DONE.code
    )

    val articleWrapper3 = ArticleWrapper(
        article = Article(label = "Chocolat", price = 12.0),
        statusCode = ArticleWrapperStatusType.DONE.code
    )

    val articleWrapper4 = ArticleWrapper(
        article = Article(label = "Flan au coco", price = 7.0)
    )

    val command1 = Command(
        articleWrappers = listOf(
            articleWrapper1, articleWrapper2, articleWrapper3
        )
    )

    val command2 = Command(
        articleWrappers = listOf(
            articleWrapper4, articleWrapper2
        )
    )
}
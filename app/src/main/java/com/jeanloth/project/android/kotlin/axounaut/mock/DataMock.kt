package com.jeanloth.project.android.kotlin.axounaut.mock

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command

object DataMock {

    val articles = listOf(
        Article(name = "Pain au beurre 1kg", price = 20.0),
        Article(name = "Pain au beurre 500g", price = 10.0),
        Article(name = "Chocolat", price = 10.0),
        Article(name = "Rolls Kanèl", price = 10.0),
        Article(name = "Petits pain", price = 0.50),
        Article(name = "Petits pain x 20.0", price = 10.0),
        Article(name = "Pain au choco x 10", price = 15.0),
        Article(name = "Flan au coco", price = 10.0),
        Article(name = "Velouté", price = 12.0),
        Article(name = "Pommes cannelles", price = 15.0),
    )

    val articleWrappers = createWrapperList(articles)

    val articleWrapper1 = ArticleWrapper(
        article = Article(name = "Pain au choco", price = 15.0)
    )
    val articleWrapper2 = ArticleWrapper(
        article = Article(name = "Rolls Kanèl", price = 10.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper3 = ArticleWrapper(
        article = Article(name = "Chocolat", price = 12.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper4 = ArticleWrapper(
        article = Article(name = "Flan au coco", price = 7.0)
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
package com.jeanloth.project.android.kotlin.axounaut.mock

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper.Companion.createWrapperList
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command

object DataMock {

    val articles = listOf(
        Article(name = "Pain au beurre 1kg", unitPrice = 20.0),
        Article(name = "Pain au beurre 500g", unitPrice = 10.0),
        Article(name = "Chocolat", unitPrice = 10.0),
        Article(name = "Rolls Kanèl", unitPrice = 10.0),
        Article(name = "Petits pain", unitPrice = 0.50),
        Article(name = "Petits pain x 20.0", unitPrice = 10.0),
        Article(name = "Pain au choco x 10", unitPrice = 15.0),
        Article(name = "Flan au coco", unitPrice = 10.0),
        Article(name = "Velouté", unitPrice = 12.0),
        Article(name = "Pommes cannelles", unitPrice = 15.0),
    )

    val articleWrappers = createWrapperList(articles)

    val articleWrapper1 = ArticleWrapper(
        article = Article(name = "Pain au choco", unitPrice = 15.0)
    )
    val articleWrapper2 = ArticleWrapper(
        article = Article(name = "Rolls Kanèl", unitPrice = 10.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper3 = ArticleWrapper(
        article = Article(name = "Chocolat", unitPrice = 12.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper4 = ArticleWrapper(
        article = Article(name = "Flan au coco", unitPrice = 7.0)
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
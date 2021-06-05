package com.jeanloth.project.android.kotlin.axounaut.mock

import com.jeanloth.project.android.kotlin.domain.entities.Article
import com.jeanloth.project.android.kotlin.domain.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain.entities.ArticleWrapper.Companion.createWrapperList
import com.jeanloth.project.android.kotlin.domain.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain.entities.Command

object DataMock {

    val articles = listOf(
        Article("Pain au beurre 1kg", unitPrice = 20.0),
        Article("Pain au beurre 500g", unitPrice = 10.0),
        Article("Chocolat", unitPrice = 10.0),
        Article("Rolls Kanèl", unitPrice = 10.0),
        Article("Petits pain", unitPrice = 0.50),
        Article("Petits pain x 20.0", unitPrice = 10.0),
        Article("Pain au choco x 10", unitPrice = 15.0),
        Article("Flan au coco", unitPrice = 10.0),
        Article("Velouté", unitPrice = 12.0),
        Article("Pommes cannelles", unitPrice = 15.0),
    )

    val articleWrappers = createWrapperList(articles)

    val articleWrapper1 = ArticleWrapper(
        article = Article("Pain au choco", unitPrice = 15.0)
    )
    val articleWrapper2 = ArticleWrapper(
        article = Article("Rolls Kanèl", unitPrice = 10.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper3 = ArticleWrapper(
        article = Article("Chocolat", unitPrice = 12.0),
        status = ArticleWrapperStatusType.DONE
    )

    val articleWrapper4 = ArticleWrapper(
        article = Article("Flan au coco", unitPrice = 7.0)
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
package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.content.Context
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.AnalysisList
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType

fun List<ArticleWrapper>.containsCanceledArticles(): Boolean = this.any { it.statusCode == ArticleWrapperStatusType.CANCELED.code }

fun List<ArticleWrapper>.notCanceled(): List<ArticleWrapper> = this.filter { it.statusCode != ArticleWrapperStatusType.CANCELED.code }

fun List<Article>.convertArticleToItemList(context: Context ) : List<AnalysisList>{
    val list = mutableListOf<AnalysisList>()
    this.forEach {
        val articleLabel = context.getString(R.string.article_name, it.label)
        list.add(
            AnalysisList(
                articleLabel,
                it.timeOrdered,
                totalAmount = it.timeOrdered * it.price
            )
        )
    }
    return list
}
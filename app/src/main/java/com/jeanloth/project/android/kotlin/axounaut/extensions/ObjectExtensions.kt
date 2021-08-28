package com.jeanloth.project.android.kotlin.axounaut.extensions

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType


fun List<ArticleWrapper>.containsCanceledArticles(): Boolean = this.any { it.statusCode == ArticleWrapperStatusType.CANCELED.code }

fun List<ArticleWrapper>.notCanceled(): List<ArticleWrapper> = this.filter { it.statusCode != ArticleWrapperStatusType.CANCELED.code }


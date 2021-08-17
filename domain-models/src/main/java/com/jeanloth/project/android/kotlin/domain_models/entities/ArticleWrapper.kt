package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class ArticleWrapper(
    val articleWrapperId : Long = 0,
    var commandId : Long = 0,
    var article : Article,
    var count : Int = 0,
    var totalArticleWrapperPrice : Double? = count * article.price,
    var statusCode : Int = ArticleWrapperStatusType.TO_DO.code

) : Serializable  {
    companion object {

        fun createWrapperList(articles : List<Article>) : List<ArticleWrapper>{
            val listResult = mutableListOf<ArticleWrapper>()
            articles.forEach { article ->
                listResult.add(
                    ArticleWrapper(
                        article = article
                    )
                )
            }
            return listResult
        }
    }
}

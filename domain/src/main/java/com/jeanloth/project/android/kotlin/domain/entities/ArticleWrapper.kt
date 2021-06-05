package com.jeanloth.project.android.kotlin.domain.entities

data class ArticleWrapper(
    val idCommandProduct : Long = 0,
    var article : Article,
    var count : Int = 0,
    var totalArticleWrapperPrice : Double = count * article.unitPrice,
    var status : ArticleWrapperStatusType = ArticleWrapperStatusType.TO_DO

)  {
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

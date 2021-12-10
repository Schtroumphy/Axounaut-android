package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

data class Command(
    val idCommand : Long = 0L,
    val deliveryDate : String? = null,
    var statusCode : Int = CommandStatusType.TO_DO.code,
    var client : AppClient? = null,
    var articleWrappers : List<ArticleWrapper> = mutableListOf<ArticleWrapper>(),
    var reduction : Double? = 0.0,
    var paymentAmount : Double? = null,
    var paymentTypeCode : String? = null
) : Serializable {

    val totalPrice : Double
        get() = this.articleWrappers.filter { it.statusCode != ArticleWrapperStatusType.CANCELED.code }.filter { it.count > 0 }.sumOf { it.totalArticleWrapperPrice ?: 0.0 }
}

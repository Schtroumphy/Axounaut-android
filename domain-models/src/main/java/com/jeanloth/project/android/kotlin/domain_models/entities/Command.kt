package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class Command(
    val idCommand : Long = 0L,
    var deliveryDate : String? = null,
    var statusCode : Int = CommandStatusType.TO_DO.code,
    var client : AppClient? = null,
    var articleWrappers : List<ArticleWrapper> = mutableListOf(),
    var reduction : Int = 0,
    var paymentAmount : Int = 0,
    var paymentTypeCode : String = PaymentType.CASH.code
) : Serializable {

    val totalPrice : Int
        get() = this.articleWrappers.filter { it.count > 0 }.sumOf { it.totalArticleWrapperPrice }

    val totalPriceWithReduction = totalPrice - reduction

    val dueAmount : Int
        get() = totalPrice - reduction - paymentAmount
}

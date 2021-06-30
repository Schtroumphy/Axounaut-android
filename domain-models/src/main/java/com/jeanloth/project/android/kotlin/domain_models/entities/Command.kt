package com.jeanloth.project.android.kotlin.domain.entities

import java.io.Serializable
import java.time.LocalDate

data class Command(
    val idCommand : Long = 0L,
    val deliveryDate : LocalDate? = LocalDate.now(),
    var status : CommandStatusType = CommandStatusType.TO_DO,
    var client : AppClient? = AppClient(),
    var totalPrice : Double? = null,
    var articleWrappers : List<ArticleWrapper>,
    var paymentAmount : Double? = null,
    var paymentType : PaymentType? = null
) : Serializable

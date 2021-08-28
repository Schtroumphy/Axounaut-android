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
    var totalPrice : Double? = null,
    var articleWrappers : List<ArticleWrapper>,
    var reduction : Double? = 0.0,
    var paymentAmount : Double? = null,
    var paymentTypeCode : String? = null
) : Serializable

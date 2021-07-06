package com.jeanloth.project.android.kotlin.domain_models.entities

enum class PaymentType(val code : String) {
    CARD("CB"),
    CASH("ESP"),
    PAYPAL("ON"),
    OTHER("OTH")
}
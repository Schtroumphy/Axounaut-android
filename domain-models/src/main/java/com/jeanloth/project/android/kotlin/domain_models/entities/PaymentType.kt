package com.jeanloth.project.android.kotlin.domain_models.entities

enum class PaymentType(val code : String, val label : String) {
    CARD("CB", "CB"),
    CASH("ESP", "Espèces"),
    LYDIA("LY", "Lydia"),
    PAYPAL("PP", "Paypal"),
    VIREMENT("VR", "Virement bancaire"),
    OTHER("OTH", "Autre");
}
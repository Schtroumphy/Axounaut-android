package com.jeanloth.project.android.kotlin.domain_models.entities

enum class PaymentType(val code : String, val label : String) {
    CASH("ESP", "Espèces"),
    CARD("CB", "CB"),
    LYDIA("LY", "Lydia"),
    PAYPAL("PP", "Paypal"),
    VIREMENT("VR", "Virement"),
    OTHER("OTH", "Autre");

    override fun toString(): String {
        return this.label
    }
}
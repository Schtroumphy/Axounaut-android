package com.jeanloth.project.android.kotlin.domain_models.entities

enum class PaymentType(val code : String, val label : String) {
    CASH("ESP", "Espèces"),
    CARD("CB", "Carte Bancaire"),
    LYDIA("LY", "Lydia"),
    PAYPAL("PP", "Paypal"),
    VIREMENT("VR", "Virement bancaire"),
    OTHER("OTH", "Autre");

    override fun toString(): String {
        return this.label
    }
}
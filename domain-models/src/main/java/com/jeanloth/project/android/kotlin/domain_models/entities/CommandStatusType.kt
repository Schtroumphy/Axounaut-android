package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandStatusType(val code : Int, val label : String) {
        TO_DO(1, "A faire"),
        IN_PROGRESS(2, "En cours"),
        DONE (3, "Terminé"),
        DELIVERED(4, "Livré"),
        CANCELE(5, "Annulé"),
        PAYED(6, "Payée")
}
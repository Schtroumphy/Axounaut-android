package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandStatusType(val label : String) {
        TO_DO("A faire"),
        IN_PROGRESS("En cours"),
        DONE ("Terminé"),
        DELIVERED("Livré"),
        CANCELE("Annulé"),
        PAYED("Payée")
}
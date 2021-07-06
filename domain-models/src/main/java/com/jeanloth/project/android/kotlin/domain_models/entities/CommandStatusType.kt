package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandStatusType(val code : Int, val label : String) {
        TO_DO(1, "A faire"),
        IN_PROGRESS(2, "En cours"),
        DONE (3, "Terminé"),
        DELIVERED(4, "Livré"),
        CANCELED(5, "Annulé"),
        PAYED(6, "Payée");

        companion object{
                fun getCommandStatusByCode(code : Int) : CommandStatusType = when(code){
                        TO_DO.code -> TO_DO
                        IN_PROGRESS.code -> IN_PROGRESS
                        DONE.code -> DONE
                        DELIVERED.code -> DELIVERED
                        CANCELED.code -> CANCELED
                        PAYED.code -> PAYED
                        else -> TO_DO
                }
        }
}
package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandStatusType(val code : Int, val label : String) {
        ALL(0, "Tout"), // For spinner
        TO_DO(1, "A faire"),
        IN_PROGRESS(2, "En cours"),
        DONE (3, "Terminée"),
        DELIVERED(4, "Livrée"),
        INCOMPLETE_PAYMENT(5, "Paiement incomplet"),
        PAYED(6, "Payée"),
        CANCELED(7, "Annulée");

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

                fun getCommandStatusByLabel(label : String) : CommandStatusType = when(label){
                        ALL.label -> ALL
                        TO_DO.label -> TO_DO
                        IN_PROGRESS.label -> IN_PROGRESS
                        DONE.label -> DONE
                        DELIVERED.label -> DELIVERED
                        CANCELED.label -> CANCELED
                        PAYED.label -> PAYED
                        else -> TO_DO
                }
        }
}
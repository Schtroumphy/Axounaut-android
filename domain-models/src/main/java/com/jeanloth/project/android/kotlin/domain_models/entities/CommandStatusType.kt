package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandStatusType(val code : Int, val label : String) {
        ALL(0, "Tout"), // For spinner
        TO_DO(1, "A faire"),
        IN_PROGRESS(2, "En cours"),
        DONE (3, "Terminée"),
        DELIVERED(4, "Livrée"),
        INCOMPLETE_PAYMENT(5, "Paiement incomplet"),
        PAYED(6, "Payée");

        companion object{
                fun getCommandStatusByCode(code : Int) : CommandStatusType = when(code){
                        TO_DO.code -> TO_DO
                        IN_PROGRESS.code -> IN_PROGRESS
                        DONE.code -> DONE
                        DELIVERED.code -> DELIVERED
                        PAYED.code -> PAYED
                        INCOMPLETE_PAYMENT.code -> INCOMPLETE_PAYMENT
                        else -> TO_DO
                }

                fun getCommandStatusByLabel(label : String) : CommandStatusType = when(label){
                        ALL.label -> ALL
                        TO_DO.label -> TO_DO
                        IN_PROGRESS.label -> IN_PROGRESS
                        DONE.label -> DONE
                        DELIVERED.label -> DELIVERED
                        PAYED.label -> PAYED
                        INCOMPLETE_PAYMENT.label -> INCOMPLETE_PAYMENT
                        else -> TO_DO
                }
        }
}
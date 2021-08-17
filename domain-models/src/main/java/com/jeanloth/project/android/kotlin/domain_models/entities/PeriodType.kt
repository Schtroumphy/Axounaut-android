package com.jeanloth.project.android.kotlin.domain_models.entities

enum class PeriodType( val label : String) {
    ALL("Tout"),
    THIS_WEEK("Cette semaine"),
    LAST_WEEK("Sem. dernière"),
    THIS_MONTH("Ce mois-ci"),
    LAST_MONTH("Mois dernier"),
    THIS_YEAR("Cette année"),
    LAST_YEAR("Année dernière");

    companion object{
        fun String.getPeriodTypeFromLabel() =
            when(this){
                THIS_WEEK.label -> THIS_WEEK
                LAST_WEEK.label -> LAST_WEEK
                THIS_MONTH.label -> THIS_MONTH
                LAST_MONTH.label -> LAST_MONTH
                THIS_YEAR.label -> THIS_YEAR
                LAST_YEAR.label -> LAST_YEAR
                else -> ALL
        }
    }
}
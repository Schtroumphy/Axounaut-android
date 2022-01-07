package com.jeanloth.project.android.kotlin.domain_models.entities

enum class IngredientQuantityType (val label: String, val description : String){
    ML("mL", "mililitres"),
    L("l","litres"),
    G("g","grammes"),
    KG("kg", "kilogrammes");

    companion object{
        fun String.fromVal(): IngredientQuantityType {
            return values().first { it.label == this } ?: ML
        }
    }

}

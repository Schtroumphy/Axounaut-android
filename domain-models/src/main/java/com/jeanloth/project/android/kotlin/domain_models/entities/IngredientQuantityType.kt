package com.jeanloth.project.android.kotlin.domain_models.entities

enum class IngredientQuantityType (val label: String, val description : String){
    L("L","litres"),
    G("G","grammes");

    companion object{
        fun String.fromVal(): IngredientQuantityType {
            return values().first { it.label == this }
        }
    }

}

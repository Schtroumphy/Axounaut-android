package com.jeanloth.project.android.kotlin.domain_models.entities

import java.time.LocalDate

data class Stock(
    val id : Long = 0,
    val lastUpdateTime : String = LocalDate.now().toString(),
    var ingredientWrappers : List<IngredientWrapper> = mutableListOf(),
)

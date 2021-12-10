package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class PrevisionalWrapper(
    val id : Long = 0,
    var actualIngredientWrappers : List<IngredientWrapper>,
    val neededRecipeIngredientWrappers : List<IngredientWrapper>

) : Serializable  {

}

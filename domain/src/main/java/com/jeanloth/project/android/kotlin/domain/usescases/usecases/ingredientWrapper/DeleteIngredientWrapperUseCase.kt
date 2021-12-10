package com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper

import com.jeanloth.project.android.kotlin.data.contracts.IngredientWrapperContract
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper

class DeleteIngredientWrapperUseCase(
    private val ingredientWrapperContract: IngredientWrapperContract
){

    fun invoke(ingredientWrapper: IngredientWrapper) {
        ingredientWrapperContract.deleteIngredientWrapper(ingredientWrapper)
    }
}
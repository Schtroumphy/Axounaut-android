package com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper

import com.jeanloth.project.android.kotlin.data.contracts.IngredientWrapperContract

class ObserveAllIngredientWrappersUseCase(
    private val ingredientWrapperContract: IngredientWrapperContract
) {

    fun invoke() = ingredientWrapperContract.observeAllIngredientWrappers()

}
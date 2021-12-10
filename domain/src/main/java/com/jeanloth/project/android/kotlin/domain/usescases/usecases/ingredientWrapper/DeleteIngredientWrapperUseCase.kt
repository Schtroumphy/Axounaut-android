package com.jeanloth.project.android.kotlin.domain.usescases.usecases.productWrapper

import com.jeanloth.project.android.kotlin.data.contracts.IngredientWrapperContract
import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper

class DeleteIngredientWrapperUseCase(
    private val ingredientWrapperContract: IngredientWrapperContract
){

    fun invoke(productWrapper: ProductWrapper) {
        ingredientWrapperContract.deleteProductWrapper(productWrapper)
    }
}
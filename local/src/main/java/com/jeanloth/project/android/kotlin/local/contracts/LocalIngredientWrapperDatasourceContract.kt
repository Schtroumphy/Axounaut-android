package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import kotlinx.coroutines.flow.Flow


interface LocalIngredientWrapperDatasourceContract {

    fun getAllIngredientWrappers() : List<IngredientWrapper>

    fun observeAllIngredientWrappers() : Flow<List<IngredientWrapper>>

    fun saveIngredientWrapper(ingredientWrapper: IngredientWrapper) : Long

    fun deleteIngredientWrapper(ingredientWrapper: IngredientWrapper) : Boolean

}
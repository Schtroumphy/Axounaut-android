package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.IngredientWrapperContract
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalIngredientWrapperDatasourceContract
import kotlinx.coroutines.flow.Flow

class IngredientWrapperRepository(
    private val localIngredientWrapperDatasourceContract: LocalIngredientWrapperDatasourceContract
) : IngredientWrapperContract {

    override fun getAllIngredientWrappers(): List<IngredientWrapper> {
        return localIngredientWrapperDatasourceContract.getAllIngredientWrappers()
    }

    override fun observeAllIngredientWrappers(): Flow<List<IngredientWrapper>> {
        return localIngredientWrapperDatasourceContract.observeAllIngredientWrappers()
    }

    override fun saveIngredientWrapper(ingredientWrapper: IngredientWrapper): Long {
        return localIngredientWrapperDatasourceContract.saveIngredientWrapper(ingredientWrapper)
    }

    override fun deleteIngredientWrapper(ingredientWrapper: IngredientWrapper): Boolean {
        return localIngredientWrapperDatasourceContract.deleteIngredientWrapper(ingredientWrapper)
    }
}
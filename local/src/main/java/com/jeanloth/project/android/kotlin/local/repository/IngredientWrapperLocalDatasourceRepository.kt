package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalIngredientWrapperDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.IngredientWrapperDAO
import com.jeanloth.project.android.kotlin.local.entities.IngredientWrapperEntity
import com.jeanloth.project.android.kotlin.local.mappers.IngredientWrapperEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IngredientWrapperLocalDatasourceRepository(
    private val dao : IngredientWrapperDAO,
    private val mapper : IngredientWrapperEntityMapper,
) : LocalIngredientWrapperDatasourceContract {

    override fun getAllIngredientWrappers(): List<IngredientWrapper> {
        return dao.all.map { mapper.from(it as IngredientWrapperEntity) }
    }

    override fun observeAllIngredientWrappers(): Flow<List<IngredientWrapper>> {
        return dao.observeAll { it.filter { true } }.map {
            it.map { mapper.from(it) }
        }
    }

    override fun saveIngredientWrapper(ingredientWrapper: IngredientWrapper): Long {
        val ingredientWrapperEntity = mapper.to(ingredientWrapper)

        // Save the ingredient wrapper entity
        return dao.box.put(ingredientWrapperEntity)
    }

    override fun deleteIngredientWrapper(ingredientWrapper: IngredientWrapper): Boolean {
        val result = dao.box.remove(mapper.to(ingredientWrapper))
        print("[IngredientWrapperLocalDatasourceRepository] : delete IngredientWrapper result : $result")
        return true
    }


}
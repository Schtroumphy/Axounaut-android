package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalProductWrapperDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ProductWrapperDAO
import com.jeanloth.project.android.kotlin.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.local.mappers.ProductWrapperEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductWrapperLocalDatasourceRepository(
    private val dao : ProductWrapperDAO,
    private val mapper : ProductWrapperEntityMapper,
) : LocalProductWrapperDatasourceContract {

    override fun getAllProductWrappers(): List<ProductWrapper> {
        return dao.all.map { mapper.from(it as ProductWrapperEntity) }
    }

    override fun observeAllProductWrappers(): Flow<List<ProductWrapper>> {
        return dao.observeAll { it.filter { true } }.map {
            it.map { mapper.from(it) }
        }
    }

    override fun saveProductWrapper(productWrapper: ProductWrapper): Long {
        val productWrapperEntity = mapper.to(productWrapper)

        // Save the product wrapper entity
        return dao.box.put(productWrapperEntity)
    }

    override fun deleteProductWrapper(productWrapper: ProductWrapper): Boolean {
        val result = dao.box.remove(mapper.to(productWrapper))
        print("[ProductWrapperLocalDatasourceRepository] : delete ProductWrapper result : $result")
        return true
    }


}
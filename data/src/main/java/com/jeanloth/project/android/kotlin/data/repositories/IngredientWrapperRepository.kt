package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.ProductWrapperContract
import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalProductWrapperDatasourceContract
import kotlinx.coroutines.flow.Flow

class ProductWrapperRepository(
    private val localProductWrapperDatasourceContract: LocalProductWrapperDatasourceContract
) : ProductWrapperContract {

    override fun getAllProductWrappers(): List<ProductWrapper> {
        return localProductWrapperDatasourceContract.getAllProductWrappers()
    }

    override fun observeAllProductWrappers(): Flow<List<ProductWrapper>> {
        return localProductWrapperDatasourceContract.observeAllProductWrappers()
    }

    override fun saveProductWrapper(productWrapper: ProductWrapper): Long {
        return localProductWrapperDatasourceContract.saveProductWrapper(productWrapper)
    }

    override fun deleteProductWrapper(productWrapper: ProductWrapper): Boolean {
        return localProductWrapperDatasourceContract.deleteProductWrapper(productWrapper)
    }
}
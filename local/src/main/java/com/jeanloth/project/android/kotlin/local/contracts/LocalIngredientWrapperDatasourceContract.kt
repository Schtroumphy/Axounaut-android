package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper
import kotlinx.coroutines.flow.Flow


interface LocalProductWrapperDatasourceContract {

    fun getAllProductWrappers() : List<ProductWrapper>

    fun observeAllProductWrappers() : Flow<List<ProductWrapper>>

    fun saveProductWrapper(productWrapper: ProductWrapper) : Long

    fun deleteProductWrapper(productWrapper: ProductWrapper) : Boolean

}
package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.ProductQuantityType.Companion.fromVal
import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.local.entities.ProductWrapperEntity

class ProductWrapperEntityMapper(val productEntityMapper : ProductEntityMapper) : Mapper<ProductWrapper, ProductWrapperEntity> {

    override fun from(t: ProductWrapperEntity): ProductWrapper {

        return ProductWrapper(
            id = t.productWrapperId,
            stockId = t.stock.targetId, // TO one
            product = productEntityMapper.from(t.product.target),
            quantity = t.quantity,
            quantityType = t.quantityTypeLabel.fromVal()
        )
    }

    override fun to(t: ProductWrapper): ProductWrapperEntity {
        val articleWrapperEntity =  ProductWrapperEntity(
            productWrapperId = t.id,
            quantity = t.quantity,
            quantityTypeLabel = t.quantityType.label
        )
        t.stockId?.let {
            articleWrapperEntity.stock.targetId = it
        }
        articleWrapperEntity.product.target = productEntityMapper.to(t.product)
        return articleWrapperEntity
    }
}
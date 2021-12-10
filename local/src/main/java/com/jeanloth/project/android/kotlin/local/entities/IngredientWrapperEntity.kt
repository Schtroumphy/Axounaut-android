package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.ProductQuantityType
import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity_.command
import io.objectbox.BoxStore
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class ProductWrapperEntity(
    @Id
    var productWrapperId : Long = 0,

    var quantity : Double = 0.0 ,
    var quantityTypeLabel : String = ProductQuantityType.KG.label

) : Entity {

    var stock: ToOne<StockEntity> = ToOne(this, ProductWrapperEntity_.stock)
    var product: ToOne<ProductEntity> = ToOne(this, ProductWrapperEntity_.product)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.ProductEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class ProductDAO constructor(boxStore: BoxStore) : BaseDAO<ProductEntity>() {
    public override val box: Box<ProductEntity> = boxStore.boxFor(ProductEntity::class.java)
}
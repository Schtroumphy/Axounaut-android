package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.ProductWrapperEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class ProductWrapperDAO constructor(boxStore: BoxStore) : BaseDAO<ProductWrapperEntity>() {
    public override val box: Box<ProductWrapperEntity> = boxStore.boxFor(ProductWrapperEntity::class.java)
}
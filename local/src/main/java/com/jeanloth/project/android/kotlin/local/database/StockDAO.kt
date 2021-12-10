package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.StockEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class StockDAO constructor(boxStore: BoxStore) : BaseDAO<StockEntity>() {
    public override val box: Box<StockEntity> = boxStore.boxFor(StockEntity::class.java)
}
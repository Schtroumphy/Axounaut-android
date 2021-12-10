package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.IngredientEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class IngredientDAO constructor(boxStore: BoxStore) : BaseDAO<IngredientEntity>() {
    public override val box: Box<IngredientEntity> = boxStore.boxFor(IngredientEntity::class.java)
}

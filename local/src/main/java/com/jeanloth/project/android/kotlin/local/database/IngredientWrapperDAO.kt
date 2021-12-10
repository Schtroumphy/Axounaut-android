package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.IngredientWrapperEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class IngredientWrapperDAO constructor(boxStore: BoxStore) : BaseDAO<IngredientWrapperEntity>() {
    public override val box: Box<IngredientWrapperEntity> = boxStore.boxFor(IngredientWrapperEntity::class.java)
}
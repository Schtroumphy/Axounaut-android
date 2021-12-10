package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.RecipeWrapperEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class RecipeWrapperDAO constructor(boxStore: BoxStore) : BaseDAO<RecipeWrapperEntity>() {
    public override val box: Box<RecipeWrapperEntity> = boxStore.boxFor(RecipeWrapperEntity::class.java)
}
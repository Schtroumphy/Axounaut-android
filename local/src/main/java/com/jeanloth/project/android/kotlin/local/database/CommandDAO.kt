package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.entities.CommandEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class CommandDAO constructor(boxStore: BoxStore) : BaseDAO<CommandEntity>() {
    public override val box: Box<CommandEntity> = boxStore.boxFor(CommandEntity::class.java)
}
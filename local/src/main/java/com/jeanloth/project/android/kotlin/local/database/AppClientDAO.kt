package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class AppClientDAO constructor(boxStore: BoxStore) : BaseDAO<AppClientEntity>() {
    public override val box: Box<AppClientEntity> = boxStore.boxFor(AppClientEntity::class.java)
}
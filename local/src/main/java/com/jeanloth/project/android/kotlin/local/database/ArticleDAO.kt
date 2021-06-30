package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class ArticleDAO constructor(boxStore: BoxStore) : BaseDAO<ArticleEntity>() {
    public override val box: Box<ArticleEntity> = boxStore.boxFor(ArticleEntity::class.java)
}
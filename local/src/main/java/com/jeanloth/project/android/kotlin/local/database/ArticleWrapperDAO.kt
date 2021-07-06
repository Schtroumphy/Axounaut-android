package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity
import io.objectbox.Box
import io.objectbox.BoxStore

class ArticleWrapperDAO constructor(boxStore: BoxStore) : BaseDAO<ArticleWrapperEntity>() {
    public override val box: Box<ArticleWrapperEntity> = boxStore.boxFor(ArticleWrapperEntity::class.java)
}
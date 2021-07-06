package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity_.command
import io.objectbox.BoxStore
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@io.objectbox.annotation.Entity
class ArticleWrapperEntity(
    @Id
    var articleWrapperId : Long = 0,

    var count : Int = 0,
    var totalArticleWrapperPrice : Double? = 0.0,
    var statusCode : Int = ArticleWrapperStatusType.TO_DO.code

    //var article : Article,
) : Entity {

    var command: ToOne<CommandEntity> = ToOne(this, ArticleWrapperEntity_.command)
    var article: ToOne<ArticleEntity> = ToOne(this, ArticleWrapperEntity_.article)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
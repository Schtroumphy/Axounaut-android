package com.jeanloth.project.android.kotlin.domain_models.entities

enum class ArticleWrapperStatusType(val code : Int) {
    TO_DO(1),
    IN_PROGRESS(2),
    DONE(3),
    CANCELED(4)
}
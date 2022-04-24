package com.jeanloth.project.android.kotlin.domain_models.entities

enum class ArticleWrapperStatusType(val code : Int) {
    TO_DO(1),
    IN_PROGRESS(2),
    DONE(3);

    companion object {
        fun getArticleWrapperStatusFromCode(code : Int) : ArticleWrapperStatusType = when(code){
            TO_DO.code -> TO_DO
            IN_PROGRESS.code -> IN_PROGRESS
            DONE.code -> DONE
            else -> TO_DO
        }
    }

}
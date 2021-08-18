package com.jeanloth.project.android.kotlin.domain_models.entities

data class ItemList(
    val label : String,
    val isDone : Boolean = false,
    val isCanceled : Boolean = false,
)

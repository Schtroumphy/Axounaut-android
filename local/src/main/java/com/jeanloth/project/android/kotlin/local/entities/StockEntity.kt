package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
class StockEntity(
    @Id
    var idStock : Long = 0L, // IMPORTANT to make the assignable id "var"
    val lastUpdateDate : String? = null

) : Entity
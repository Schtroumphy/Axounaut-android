package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
class ProductEntity(
    @Id
    var id: Long = 0,
    var label: String = "",
    var price : Double = 0.0
) : Entity
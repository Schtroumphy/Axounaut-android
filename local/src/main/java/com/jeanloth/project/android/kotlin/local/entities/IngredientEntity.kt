package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
class IngredientEntity(
    @Id
    var id: Long = 0,
    var label: String = ""
) : Entity
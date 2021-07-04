package com.jeanloth.project.android.kotlin.local.entities

import io.objectbox.annotation.Id

@io.objectbox.annotation.Entity
data class AppClientEntity(
    @Id
    var idClient: Long = 0,
    var firstname : String = "Adrien DELONNE",
    var lastname : String? = null,
    var phoneNumber : Int? = null,
    var fidelityPoint : Int = 0,
    var isFavorite : Boolean = false
) : Entity
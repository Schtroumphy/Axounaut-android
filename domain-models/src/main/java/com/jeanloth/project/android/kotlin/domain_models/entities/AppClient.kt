package com.jeanloth.project.android.kotlin.domain_models.entities

data class AppClient(
    val idClient : Long = 0L,
    var firstname : String = "Adrien DELONNE",
    var lastname : String? = null,
    var phoneNumber : Int? = null,
    var fidelityPoint : Int = 0,
    var isFavorite : Boolean = false
) {
    override fun toString(): String {
        return this.toNameString()
    }
}

fun AppClient?.toNameString() : String {
    return "${this?.firstname} ${this?.lastname?.toUpperCase()}"
}

fun List<AppClient>.getClientFromName(firstname : String, lastname : String) : AppClient? = this.firstOrNull { it.firstname == firstname && it.lastname == lastname  }


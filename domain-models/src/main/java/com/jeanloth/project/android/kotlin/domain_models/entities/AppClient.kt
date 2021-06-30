package com.jeanloth.project.android.kotlin.domain.entities

data class AppClient(
    val idClient : Long = 0L,
    var firstname : String = "Adrien DELONNE",
    var lastname : String? = null,
    var phoneNumber : Int? = null,
    var fidelityPoint : Int = 0
)

fun AppClient.toNameString() : String{
    return this.lastname?.toUpperCase() ?: "" + " " + this.firstname
}

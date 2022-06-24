package com.jeanloth.project.android.kotlin.domain_models.entities

enum class CommandDisplayMode(val statusCode: List<Int>) {
    IN_PROGRESS(listOf(CommandStatusType.IN_PROGRESS.code, CommandStatusType.DONE.code)),
    TO_COME(listOf(CommandStatusType.TO_DO.code)),
    PAST(listOf(
        CommandStatusType.TO_DO.code,
        CommandStatusType.DONE.code,
        CommandStatusType.PAYED.code,
        CommandStatusType.DELIVERED.code,
        CommandStatusType.INCOMPLETE_PAYMENT.code,
        )
    );

    companion object{
        fun fromVal(value: String) : CommandDisplayMode = values().firstOrNull { it.name == value} ?: TO_COME
    }
}
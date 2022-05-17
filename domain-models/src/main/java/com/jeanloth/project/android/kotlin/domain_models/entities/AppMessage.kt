package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class AppMessage(
    val id : Long = 0,
    val message : String = "",
    val type: MessageType = MessageType.COMMAND

): Serializable

enum class MessageType(val priority: MessagePriority){
    COMMAND(MessagePriority.MEDIUM),
    REMINDER(MessagePriority.HIGH),
    STOCK(MessagePriority.LOW)
}

enum class MessagePriority{
    LOW,
    MEDIUM,
    HIGH
}

package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType


class GetCommandsByStatusCodeUseCase(
    private val commandContract : CommandContract
) {

    operator fun invoke(statuses : List<CommandStatusType>) : Map<Int, List<Command>> {
        val map = mutableMapOf<Int, MutableList<Command>>() // Int for CommandStatusType code
        commandContract.getCommandsByStatus(statuses).forEach {
            if(map.keys.contains(it.statusCode)){
                map[it.statusCode]?.add(it)
            } else {
                map[it.statusCode] = mutableListOf(it)
            }
        }
        return map
    }

    fun countByStatus(statuses : List<CommandStatusType>) : Map<Int, Int> {
        val map = mutableMapOf<Int, Int>() // Int for CommandStatusType code
        commandContract.getCommandsByStatus(statuses).forEach {
            if(map.keys.contains(it.statusCode)){
                map[it.statusCode] = map[it.statusCode]?.plus(1) ?: 0
            } else {
                map[it.statusCode] = 1
            }
        }
        return map
    }
}
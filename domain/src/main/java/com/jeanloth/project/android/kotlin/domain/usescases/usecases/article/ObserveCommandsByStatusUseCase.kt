package com.jeanloth.project.android.kotlin.domain.usescases.usecases.article

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.coroutines.flow.Flow

class ObserveCommandsByStatusUseCase(
    private val commandContract: CommandContract
) {

    fun invoke(statuses : List<CommandStatusType>) : Flow<List<Command>> = commandContract.observeCommandsByStatus(statuses)

}
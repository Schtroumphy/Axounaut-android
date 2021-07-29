package com.jeanloth.project.android.kotlin.domain.usescases.usecases

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract

class GetCommandByIdUseCase(
    private val commandContract: CommandContract
) {

    fun invoke(commandId : Long) = commandContract.getCommandById(commandId)

}
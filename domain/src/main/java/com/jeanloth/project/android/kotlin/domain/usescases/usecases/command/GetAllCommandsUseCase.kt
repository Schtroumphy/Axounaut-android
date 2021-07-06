package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract


class GetAllCommandsUseCase(
    private val commandContract : CommandContract
) {

    fun invoke() = commandContract.getAllCommands()
}
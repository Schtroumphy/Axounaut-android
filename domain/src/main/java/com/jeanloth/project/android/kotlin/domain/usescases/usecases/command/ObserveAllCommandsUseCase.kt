package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract


class ObserveAllCommandsUseCase(
    private val commandContract : CommandContract
) {

    operator fun invoke() = commandContract.observeAllCommands()
}
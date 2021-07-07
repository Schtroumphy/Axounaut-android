package com.jeanloth.project.android.kotlin.domain.usescases.usecases

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract

class ObserveArticleWrapperByCommandIdUseCase(
    private val commandContract: CommandContract
) {

    fun invoke() = commandContract.observeCommands()

}
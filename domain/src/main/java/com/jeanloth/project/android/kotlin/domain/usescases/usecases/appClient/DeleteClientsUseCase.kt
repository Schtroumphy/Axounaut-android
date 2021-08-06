package com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient

import com.jeanloth.project.android.kotlin.data.contracts.AppClientContract
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient

class DeleteClientsUseCase(
    private val appClientContract: AppClientContract
) {

    fun invoke(clients : List<AppClient>) = appClientContract.deleteClients(clients)

}
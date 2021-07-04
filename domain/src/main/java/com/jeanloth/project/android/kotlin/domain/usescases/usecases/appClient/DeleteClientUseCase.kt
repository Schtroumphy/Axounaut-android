package com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient

import com.jeanloth.project.android.kotlin.data.contracts.AppClientContract
import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article

class DeleteClientUseCase(
    private val appClientContract: AppClientContract
) {

    fun invoke(client : AppClient) = appClientContract.deleteClient(client)

}
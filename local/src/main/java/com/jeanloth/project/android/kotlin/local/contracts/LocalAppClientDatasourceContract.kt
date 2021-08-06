package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import kotlinx.coroutines.flow.Flow


interface LocalAppClientDatasourceContract {

    fun getAllClients() : List<AppClient>

    fun observeAllClients() : Flow<List<AppClient>>

    fun saveClient(client: AppClient) : Boolean

    fun deleteClients(clients: List<AppClient>) : Boolean
}
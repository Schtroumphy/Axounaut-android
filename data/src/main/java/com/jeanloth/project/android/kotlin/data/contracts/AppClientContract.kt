package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import kotlinx.coroutines.flow.Flow

interface AppClientContract {

    fun getAllClients() : List<AppClient>

    fun observeClients() : Flow<List<AppClient>>

    fun saveClient(client: AppClient) : Boolean

    fun deleteClient(client: AppClient) : Boolean
}
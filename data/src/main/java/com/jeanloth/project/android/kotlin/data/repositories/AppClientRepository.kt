package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.AppClientContract
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.local.contracts.LocalAppClientDatasourceContract
import kotlinx.coroutines.flow.Flow

class AppClientRepository(
    private val localAppClientDatasourceContract : LocalAppClientDatasourceContract
) : AppClientContract{

    override fun getAllClients(): List<AppClient> {
        return localAppClientDatasourceContract.getAllClients()
    }

    override fun observeClients(): Flow<List<AppClient>> {
        return localAppClientDatasourceContract.observeAllClients()
    }

    override fun saveClient(client: AppClient): Boolean {
        return localAppClientDatasourceContract.saveClient(client)
    }

    override fun deleteClient(client: AppClient): Boolean {
        return localAppClientDatasourceContract.deleteClient(client)
    }
}
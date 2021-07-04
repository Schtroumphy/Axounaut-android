package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.local.contracts.LocalAppClientDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.AppClientDAO
import com.jeanloth.project.android.kotlin.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppClientLocalDatasourceRepository(
    private val dao : AppClientDAO,
    private val mapper : AppClientEntityMapper,
) : LocalAppClientDatasourceContract{

    override fun getAllClients(): List<AppClient> {
        return dao.all.map { mapper.from(it as AppClientEntity) }
    }

    override fun observeAllClients(): Flow<List<AppClient>> {
        val result =  dao.observeAll{
            it.filter { true }
        }.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }

    override fun saveClient(client: AppClient): Boolean {
        print("[AppClientLocalDSRepository] : Save client - Client : $client")
        print("[AppClientLocalDSRepository] : Save client - Client Entity: ${mapper.to(client)}")
        dao.box.put(mapper.to(client))
        return true
    }

    override fun deleteClient(client: AppClient): Boolean {
        val result = dao.box.remove(mapper.to(client))
        print("[AppClientLocalDSRepository] : Delete client result : $result")

        return true
    }


}
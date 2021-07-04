package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity

class AppClientEntityMapper : Mapper<AppClient, AppClientEntity> {

    override fun from(t: AppClientEntity): AppClient {
        return AppClient(
            idClient = t.idClient,
            firstname = t.firstname,
            lastname = t.lastname,
            phoneNumber = t.phoneNumber,
            fidelityPoint = t.fidelityPoint,
            isFavorite = t.isFavorite

        )
    }

    override fun to(t: AppClient): AppClientEntity {
        return AppClientEntity(
            idClient = t.idClient,
            firstname = t.firstname,
            lastname = t.lastname,
            phoneNumber = t.phoneNumber,
            fidelityPoint = t.fidelityPoint,
            isFavorite = t.isFavorite
        )
    }
}
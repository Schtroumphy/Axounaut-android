package com.jeanloth.project.android.kotlin.axounaut.di

import android.content.Context
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.data.contracts.AppClientContract
import com.jeanloth.project.android.kotlin.data.repositories.ArticleRepository
import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.data.repositories.AppClientRepository
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.DeleteClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.ObserveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.SaveClientUseCase
import com.jeanloth.project.android.kotlin.local.contracts.LocalAppClientDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.AppClientDAO
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.entities.MyObjectBox
import com.jeanloth.project.android.kotlin.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import com.jeanloth.project.android.kotlin.local.repository.AppClientLocalDatasourceRepository
import com.jeanloth.project.android.kotlin.local.repository.ArticleLocalDatasourceRepository
import io.objectbox.BoxStore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    viewModel { MainVM() }
    viewModel { ArticleVM( get(), get(), get(), get()) }
    viewModel { ClientVM( get(), get(), get()) }

    // Uses cases
    factory{ GetAllArticlesUseCase(get()) }
    factory{ ObserveArticlesUseCase(get()) }
    factory{ SaveArticleUseCase(get()) }
    factory{ DeleteArticleUseCase(get()) }

    factory{ SaveClientUseCase(get()) }
    factory{ ObserveClientUseCase(get()) }
    factory{ DeleteClientUseCase(get()) }

    // Data repository
    single { ArticleRepository(get()) } bind ArticleContract::class
    single { AppClientRepository(get()) } bind AppClientContract::class

    // Mappers
    single{ ArticleEntityMapper() }
    single{ AppClientEntityMapper() }

    single { provideBoxStore(get())}

    // Local repository
    single { ArticleLocalDatasourceRepository(get(), get())} bind LocalArticleDatasourceContract::class
    single { AppClientLocalDatasourceRepository(get(), get()) } bind LocalAppClientDatasourceContract::class

    // Factory DAO
    factory { ArticleDAO(get()) }
    factory { AppClientDAO(get()) }

}

fun provideBoxStore(context : Context) : BoxStore {
    return MyObjectBox.builder().androidContext(context).build()
}
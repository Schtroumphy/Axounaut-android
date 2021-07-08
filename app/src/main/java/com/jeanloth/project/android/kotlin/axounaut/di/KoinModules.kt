package com.jeanloth.project.android.kotlin.axounaut.di

import android.content.Context
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ClientVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.CommandVM
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.data.contracts.AppClientContract
import com.jeanloth.project.android.kotlin.data.repositories.ArticleRepository
import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.data.contracts.ArticleWrapperContract
import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.data.repositories.AppClientRepository
import com.jeanloth.project.android.kotlin.data.repositories.ArticleWrapperRepository
import com.jeanloth.project.android.kotlin.data.repositories.CommandRepository
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.DeleteClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.ObserveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.SaveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.GetAllCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.local.contracts.LocalAppClientDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleWrapperDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalCommandDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.AppClientDAO
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.database.ArticleWrapperDAO
import com.jeanloth.project.android.kotlin.local.database.CommandDAO
import com.jeanloth.project.android.kotlin.local.entities.MyObjectBox
import com.jeanloth.project.android.kotlin.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleWrapperEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.CommandEntityMapper
import com.jeanloth.project.android.kotlin.local.repository.AppClientLocalDatasourceRepository
import com.jeanloth.project.android.kotlin.local.repository.ArticleLocalDatasourceRepository
import com.jeanloth.project.android.kotlin.local.repository.ArticleWrapperLocalDatasourceRepository
import com.jeanloth.project.android.kotlin.local.repository.CommandLocalDatasourceRepository
import io.objectbox.BoxStore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    viewModel { MainVM() }
    viewModel { ArticleVM( get(), get(), get(), get()) }
    viewModel { ClientVM( get(), get(), get()) }
    viewModel { (currentCommandId : Long) -> CommandVM(
        currentCommandId = currentCommandId,
        get(), get(), get(), get(), get())
    }

    // Uses cases
    factory{ GetAllArticlesUseCase(get()) }
    factory{ ObserveArticlesUseCase(get()) }
    factory{ SaveArticleUseCase(get()) }
    factory{ DeleteArticleUseCase(get()) }

    factory{ SaveClientUseCase(get()) }
    factory{ ObserveClientUseCase(get()) }
    factory{ DeleteClientUseCase(get()) }

    factory{ SaveCommandUseCase(get()) }
    factory{ GetAllCommandsUseCase(get()) }
    factory{ ObserveCommandsUseCase(get()) }
    factory{ DeleteCommandUseCase(get()) }

    factory{ SaveArticleWrapperUseCase(get()) }
    factory{ ObserveCommandByIdUseCase(get()) }

    // Data repository
    single { ArticleRepository(get()) } bind ArticleContract::class
    single { AppClientRepository(get()) } bind AppClientContract::class
    single { CommandRepository(get()) } bind CommandContract::class
    single { ArticleWrapperRepository(get()) } bind ArticleWrapperContract::class

    // Mappers
    single{ ArticleEntityMapper() }
    single{ AppClientEntityMapper() }
    single{ CommandEntityMapper( get(), get(), get()) }
    single{ ArticleWrapperEntityMapper( get()) }

    single { provideBoxStore(get())}

    // Local repository
    single { ArticleLocalDatasourceRepository(get(), get())} bind LocalArticleDatasourceContract::class
    single { AppClientLocalDatasourceRepository(get(), get()) } bind LocalAppClientDatasourceContract::class
    single { CommandLocalDatasourceRepository(get(), get(), get()) } bind LocalCommandDatasourceContract::class
    single { ArticleWrapperLocalDatasourceRepository(get(), get()) } bind LocalArticleWrapperDatasourceContract::class

    // Factory DAO
    factory { ArticleDAO(get()) }
    factory { AppClientDAO(get()) }
    factory { CommandDAO(get()) }
    factory { ArticleWrapperDAO(get()) }

}

fun provideBoxStore(context : Context) : BoxStore? {
    //return null
    return MyObjectBox.builder().androidContext(context).build()
}
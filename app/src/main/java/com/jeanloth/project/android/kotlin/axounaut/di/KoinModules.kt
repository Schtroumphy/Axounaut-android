package com.jeanloth.project.android.kotlin.axounaut.di

import android.content.Context
import com.jeanloth.project.android.kotlin.axounaut.datastore.StockManager
import com.jeanloth.project.android.kotlin.axounaut.viewModels.*
import com.jeanloth.project.android.kotlin.data.contracts.*
import com.jeanloth.project.android.kotlin.data.repositories.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.ObserveAllIngredientWrappersUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticleWrappersByCommandIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.DeleteClientsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.ObserveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.SaveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.DeleteIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.SaveIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.local.contracts.*
import com.jeanloth.project.android.kotlin.local.database.*
import com.jeanloth.project.android.kotlin.local.entities.MyObjectBox
import com.jeanloth.project.android.kotlin.local.mappers.*
import com.jeanloth.project.android.kotlin.local.repository.*
import io.objectbox.BoxStore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    viewModel { MainVM(get(), get()) }
    viewModel { ArticleVM( get(), get(), get()) }
    viewModel { AddArticleVM(get(), get(), get())}
    viewModel { ClientVM( get(), get(), get()) }
    viewModel { AddCommandVM() }
    viewModel { CommandVM(get(), get(), get(), get(), get(), get()) }
    viewModel { (commandId: Long) -> PayCommandVM(commandId = commandId, get(), get()) }
    viewModel { (currentCommandId: Long) -> CommandDetailedVM(currentCommandId = currentCommandId, get(), get(), get(), get(), get()) }
    viewModel { StockVM(get(), get(), get(), get()) }

    // Uses cases
    factory{ GetAllArticlesUseCase(get()) }
    factory{ ObserveArticlesUseCase(get()) }
    factory{ SaveArticleUseCase(get()) }
    factory{ DeleteArticleUseCase(get()) }

    factory{ SaveClientUseCase(get()) }
    factory{ ObserveClientUseCase(get()) }
    factory{ DeleteClientsUseCase(get()) }

    factory{ SaveCommandUseCase(get()) }
    factory{ ObserveAllCommandsUseCase(get()) }
    factory{ GetCommandByIdUseCase(get()) }
    factory{ ObserveCommandsUseCase(get()) }
     factory{ GetCommandsByStatusCodeUseCase(get()) }
    factory{ DeleteCommandUseCase(get()) }
    factory{ ObserveCommandByIdUseCase(get()) }
    factory{ ObserveCommandsByStatusUseCase(get()) }

    factory{ SaveArticleWrapperUseCase(get()) }
    factory{ ObserveArticleWrappersByCommandIdUseCase(get()) }
    factory{ DeleteArticleWrapperUseCase(get()) }

    factory{ ObserveAllIngredientWrappersUseCase(get()) }
    factory{ SaveIngredientWrapperUseCase(get()) }
    factory{ DeleteIngredientWrapperUseCase(get()) }

    // Data repository
    single { ArticleRepository(get()) } bind ArticleContract::class
    single { AppClientRepository(get()) } bind AppClientContract::class
    single { CommandRepository(get()) } bind CommandContract::class
    single { ArticleWrapperRepository(get()) } bind ArticleWrapperContract::class
    single { IngredientWrapperRepository(get()) } bind IngredientWrapperContract::class

    // Mappers
    single{ ArticleEntityMapper(get(), get()) }
    single{ IngredientEntityMapper() }
    single{ AppClientEntityMapper() }
    single{ CommandEntityMapper( get(), get()) }
    single{ StockEntityMapper( get()) }
    single{ ArticleWrapperEntityMapper( get()) }
    single{ IngredientWrapperEntityMapper( get()) }
    single{ RecipeWrapperEntityMapper( get()) }

    single { provideBoxStore(get())}

    // Local repository
    single { ArticleLocalDatasourceRepository(get(), get(), get())} bind LocalArticleDatasourceContract::class
    single { AppClientLocalDatasourceRepository(get(), get()) } bind LocalAppClientDatasourceContract::class
    single { CommandLocalDatasourceRepository(get(), get(), get(), get(), get(), get(), get(), get()) } bind LocalCommandDatasourceContract::class
    single { ArticleWrapperLocalDatasourceRepository(get(), get(), get()) } bind LocalArticleWrapperDatasourceContract::class
    single { IngredientWrapperLocalDatasourceRepository(get(), get()) } bind LocalIngredientWrapperDatasourceContract::class

    // Factory DAO
    factory { ArticleDAO(get()) }
    factory { AppClientDAO(get()) }
    factory { CommandDAO(get()) }
    factory { ArticleWrapperDAO(get()) }
    factory { IngredientDAO(get()) }
    factory { IngredientWrapperDAO(get()) }
    factory { StockDAO(get()) }
    factory { RecipeWrapperDAO(get()) }

    // Other
    single { StockManager(get())}
}

fun provideBoxStore(context : Context) : BoxStore? {
    //return null
    return MyObjectBox.builder().androidContext(context).build()
}
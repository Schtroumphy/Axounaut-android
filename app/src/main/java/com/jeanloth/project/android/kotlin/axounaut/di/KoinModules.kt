package com.jeanloth.project.android.kotlin.axounaut.di

import android.content.Context
import com.jeanloth.project.android.kotlin.axounaut.viewModels.ArticleVM
import com.jeanloth.project.android.kotlin.data.repositories.ArticleRepository
import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.entities.MyObjectBox
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import com.jeanloth.project.android.kotlin.local.repository.ArticleLocalDatasourceRepository
import io.objectbox.BoxStore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    viewModel { ArticleVM( get(), get(), get(), get()) }

    // Uses cases
    factory{ GetAllArticlesUseCase(get()) }
    factory{ ObserveArticlesUseCase(get()) }
    factory{ SaveArticleUseCase(get()) }
    factory{ DeleteArticleUseCase(get()) }

    // Data repository
    single { ArticleRepository(get()) } bind ArticleContract::class

    // Mappers
    single{ ArticleEntityMapper() }

    single { provideBoxStore(get())}

    // Local repository
    single { ArticleLocalDatasourceRepository(get(), get())} bind LocalArticleDatasourceContract::class

    // Factory DAO
    factory { ArticleDAO(get()) }

}

fun provideBoxStore(context : Context) : BoxStore {
    return MyObjectBox.builder().androidContext(context).build()
}
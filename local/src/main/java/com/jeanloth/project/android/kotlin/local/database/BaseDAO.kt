package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.Entity
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import io.objectbox.reactive.SubscriptionBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

abstract class BaseDAO<TEntity : Entity>() {

    protected abstract val box : Box<TEntity>

    val all : List<Entity> get() = box.query().build().find()

    private fun apply(filters : (QueryBuilder<TEntity>) -> (QueryBuilder<TEntity>)) : Query<TEntity> {
        return filters(box.query()).build()
    }

    /**
     * Observe on all elements of the given filters
     */
    fun observeAll( filters : (QueryBuilder<TEntity>) -> (QueryBuilder<TEntity>)) : Flow<List<TEntity>> = apply(filters).subscribe().asFlow()

    /**
     * Observe on first element of the given filters
     */
    fun observeFirst( filters : (QueryBuilder<TEntity>) -> (QueryBuilder<TEntity>)) : Flow<TEntity?> = apply(filters).subscribe().asFlowFirst()


    /**
     * Observe on unique or null element of the given filters
     */
    //fun observeUnique( filters : (QueryBuilder<TEntity>) -> (QueryBuilder<TEntity>)) : Flow<TEntity?> = apply(filters).subscribe().asFlowUnique()


    @ExperimentalCoroutinesApi
    private fun <TEntity> SubscriptionBuilder<TEntity>.asFlow() : Flow<TEntity> = callbackFlow {
        val subscription = observer { sendBlocking(it) }
        awaitClose { subscription.cancel()}
    }

    @ExperimentalCoroutinesApi
    private fun <TEntity> SubscriptionBuilder<List<TEntity>>.asFlowFirst() : Flow<TEntity?> = callbackFlow {
        val subscription = observer { sendBlocking(it.firstOrNull()) }
        awaitClose { subscription.cancel()}
    }

    @ExperimentalCoroutinesApi
    private fun <TEntity> SubscriptionBuilder<List<TEntity>>.asFlowSingle() : Flow<TEntity?> = callbackFlow {
        val subscription = observer { sendBlocking(it.firstOrNull()) }
        awaitClose { subscription.cancel()}
    }
}

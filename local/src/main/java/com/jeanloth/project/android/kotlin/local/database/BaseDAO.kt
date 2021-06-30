package com.jeanloth.project.android.kotlin.local.database

import com.jeanloth.project.android.kotlin.local.entities.Entity
import io.objectbox.Box

abstract class BaseDAO<TEntity : Entity>() {

    protected abstract val box : Box<TEntity>

    val all : List<Entity> get() = box.query().build().find()
}

package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.local.entities.StockEntity
import java.time.LocalDate

class StockEntityMapper(
    private val ingredientWrapperMapper: IngredientWrapperEntityMapper,
) :
    Mapper<Stock, StockEntity> {

    override fun from(t: StockEntity): Stock {
        return Stock(
            id = t.idStock,
            lastUpdateTime = t.lastUpdateDate ?: LocalDate.now().toString()
        )
    }

    override fun to(t: Stock): StockEntity {
        return StockEntity(
            idStock = t.id,
            lastUpdateDate = t.lastUpdateTime
        )
    }
}
package com.jeanloth.project.android.kotlin.domain_models.entities

import java.io.Serializable

data class ProductWrapper(
    val id : Long = 0,
    var stockId : Long? = null,
    var product : Ingredient,
    var quantity : Double = 0.0,
    var quantityType : ProductQuantityType = ProductQuantityType.KG

) : Serializable  {

    val totalProductWrapperPrice : Double
    get() = quantity * product.price

    val countStatusType : CountStatus
    get() = when{
        quantity == 0.0 -> CountStatus.LOW
        quantity < 2 -> CountStatus.MEDIUM
        else -> CountStatus.LARGE
    }

    companion object {

        fun createWrapperList(products : List<Ingredient>) : List<ProductWrapper>{
            val listResult = mutableListOf<ProductWrapper>()
            products.forEach { product ->
                listResult.add(
                    ProductWrapper(
                        product = product
                    )
                )
            }
            return listResult
        }
    }

    enum class CountStatus{
        LOW, MEDIUM, LARGE
    }
}

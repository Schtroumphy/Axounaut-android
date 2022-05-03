package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.axounaut.datastore.StockManager
import com.jeanloth.project.android.kotlin.axounaut.extensions.toLocalDate
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsByStatusUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.ObserveAllIngredientWrappersUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.SaveIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class StockVM (
    private val observeAllIngredientWrappersUseCase: ObserveAllIngredientWrappersUseCase,
    private val saveIngredientWrapperUseCase: SaveIngredientWrapperUseCase,
    private val stockManager: StockManager,
    private val observeCommandsByStatusUseCase: ObserveCommandsByStatusUseCase
): ViewModel() {

    private val ingredientWrapperMutableLiveData = MutableLiveData<List<IngredientWrapper>>(emptyList())
    fun observeIngredientWrappersLiveData() : LiveData<List<IngredientWrapper>> = ingredientWrapperMutableLiveData

    var allRecipeOfNotDeliveredCommandMutableLiveData : MutableLiveData<List<RecipeWrapper>> = MutableLiveData(emptyList()) // ToDo and InProgress commands

    val previsionalWrappersMediatorLiveData = MediatorLiveData<List<PrevisionalWrapper>>()

    private val DAYS_PERIOD_FOR_PREVISIONAL_NOT_DELIVERED = 7L

    lateinit var currentWeekFirstDate : LocalDate
    lateinit var currentWeekLastDate : LocalDate

    init {
        getCurrentWeekDates()

        viewModelScope.launch {
            observeAllIngredientWrappersUseCase.invoke().collect {
                Log.d("[StockVM]", " Ingredient wrappers observed : $it")
                ingredientWrapperMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            // Not delivered date must be calculated from command to deliver from today to today + DAYS_PERIOD_FOR_PREVISIONAL_NOT_DELIVERED
            observeCommandsByStatusUseCase.invoke(listOf(CommandStatusType.TO_DO, CommandStatusType.IN_PROGRESS)).map {
                it.filter { it.deliveryDate?.toLocalDate()?.isAfter(LocalDate.now()) == true && it.deliveryDate?.toLocalDate()?.isBefore(LocalDate.now().plusDays(DAYS_PERIOD_FOR_PREVISIONAL_NOT_DELIVERED)) == true }
            }.collect {
                val list : List<RecipeWrapper> = it.map { it.articleWrappers.map { it.article }.map { it.recipeIngredients }.flatten() }.flatten()
                allRecipeOfNotDeliveredCommandMutableLiveData.postValue(list) // TODO map by recipe wrappers
            }
        }

        previsionalWrappersMediatorLiveData.addSource(allRecipeOfNotDeliveredCommandMutableLiveData){
            buildPrevisionalWrapper()
        }

        previsionalWrappersMediatorLiveData.addSource(ingredientWrapperMutableLiveData){
            buildPrevisionalWrapper()
        }
    }

    private fun buildPrevisionalWrapper(){
        val liste = mutableListOf<PrevisionalWrapper>()
        allRecipeOfNotDeliveredCommandMutableLiveData.value?.forEach { rw ->
            val actual = ingredientWrapperMutableLiveData.value?.find { it.ingredient.id == rw.ingredient.id }

            // Add unique provisional wrapper or update needed value
            if(actual != null){
                if(!liste.any { it.ingredient.id == rw.ingredient.id }) {
                    liste.add(PrevisionalWrapper(ingredient = actual.ingredient, actual = actual.quantity, needed = rw.quantity))
                } else {
                    liste.find { it.ingredient.id == rw.ingredient.id }?.apply {
                        needed += rw.quantity
                    }
                }
            }
        }
        previsionalWrappersMediatorLiveData.postValue(liste)
    }

    fun savePWs(pws : List<IngredientWrapper>) {
        pws.forEach{ saveIngredientWrapper(it)}
    }

    fun saveIngredientWrapper(pw: IngredientWrapper) {
        saveIngredientWrapperUseCase.invoke(pw)
    }

    fun updateStockLastUpdateDate(){
        viewModelScope.launch {
            stockManager.saveStockLastUpdateDate(LocalDate.now().toString())
        }
    }

    private fun getCurrentWeekDates(){
        val today = LocalDate.now()

        // Go backward to get Monday
        currentWeekFirstDate = today
        while (currentWeekFirstDate.dayOfWeek != DayOfWeek.MONDAY) {
            currentWeekFirstDate = currentWeekFirstDate.minusDays(1)
        }

        // Go forward to get Sunday
        currentWeekLastDate = today
        while (currentWeekLastDate.dayOfWeek != DayOfWeek.SUNDAY) {
            currentWeekLastDate = currentWeekLastDate.plusDays(1)
        }
    }


}
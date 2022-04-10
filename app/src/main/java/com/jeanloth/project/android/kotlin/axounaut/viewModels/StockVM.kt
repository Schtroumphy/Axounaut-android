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
import java.util.*

class StockVM (
    private val observeAllIngredientWrappersUseCase: ObserveAllIngredientWrappersUseCase,
    private val saveIngredientWrapperUseCase: SaveIngredientWrapperUseCase,
    private val stockManager: StockManager,
    private val observeCommandsByStatusUseCase: ObserveCommandsByStatusUseCase
): ViewModel() {

    private val ingredientWrapperMutableLiveData = MutableLiveData<List<IngredientWrapper>>(emptyList())
    fun observeIngredientWrappersLiveData() : LiveData<List<IngredientWrapper>> = ingredientWrapperMutableLiveData

    var allNotDeliveredCommandMutableLiveData : MutableLiveData<List<RecipeWrapper>> = MutableLiveData(emptyList()) // ToDo and InProgress commands

    val previsionalWrappersMediatorLiveData = MediatorLiveData<List<PrevisionalWrapper>>()

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
            observeCommandsByStatusUseCase.invoke(listOf(CommandStatusType.TO_DO, CommandStatusType.IN_PROGRESS)).map {
                it.filter { it.deliveryDate?.toLocalDate()?.isBefore(currentWeekLastDate) == true && it.deliveryDate?.toLocalDate()?.isAfter(currentWeekFirstDate) == true }
            }.collect {
                val list : List<RecipeWrapper> = it.map { it.articleWrappers.map { it.article }.map { it.recipeIngredients }.flatten() }.flatten()
                allNotDeliveredCommandMutableLiveData.postValue(list) // TODO map by recipe wrappers
            }
        }

        previsionalWrappersMediatorLiveData.addSource(allNotDeliveredCommandMutableLiveData){
            buildPrevisionalWrapper()
        }

        previsionalWrappersMediatorLiveData.addSource(ingredientWrapperMutableLiveData){
            buildPrevisionalWrapper()
        }
    }

    private fun buildPrevisionalWrapper(){
        val liste = mutableListOf<PrevisionalWrapper>()
        allNotDeliveredCommandMutableLiveData.value?.forEach { rw ->
            val actual = ingredientWrapperMutableLiveData.value?.find { it.ingredient.label == rw.ingredient.label }

            if(actual != null) {
                liste.add(PrevisionalWrapper(ingredient = actual.ingredient, actual = actual.quantity, needed = rw.quantity))
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
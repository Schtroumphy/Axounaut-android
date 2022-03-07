package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCommandVM : ViewModel() {

    private var deliveryDateLiveData : MutableLiveData<String> = MutableLiveData("")
    var clientLiveData : MutableLiveData<AppClient?> = MutableLiveData(null)
    var allArticlesLiveData : MutableLiveData<List<ArticleWrapper>?> = MutableLiveData()

    private var _canResumeStateFlow = MutableStateFlow(false)
    var canResumeStateFlow = _canResumeStateFlow.asStateFlow()

    fun canResume(){
        val isOk = !deliveryDateLiveData.value.isNullOrEmpty() /* && deliveryDate.isDateIsValid()*/
                && clientLiveData.value != null && !allArticlesLiveData.value.isNullOrEmpty()
                && allArticlesLiveData.value!!.any { it.count > 0 }
        Log.d("[AddCommandVM]", "Can resume ? $isOk")
        viewModelScope.launch {
            _canResumeStateFlow.emit(isOk)
        }
    }

    fun setDeliveryDate(deliveryDate : String?){
        deliveryDateLiveData.postValue(deliveryDate)
        canResume()
    }

    fun setClientLiveData(client: AppClient?){
        clientLiveData.postValue(client)
        canResume()
    }

    fun setAllArticlesLiveData(articles : List<ArticleWrapper>){
        allArticlesLiveData.value = articles
    }

    fun setArticlesLiveData(articleWrapper: List<ArticleWrapper> ){
        Log.d("[AddCommandVM]", "BEFORE all articles ? ${allArticlesLiveData.value}")

        articleWrapper.forEach { aw ->
            allArticlesLiveData.value?.find {
                it.article.id == aw.article.id
            }?.count = aw.count
        }
        Log.d("[AddCommandVM]", "AFTER all articles ? ${allArticlesLiveData.value}")

        //articlesLiveData.value = articleWrapper
        allArticlesLiveData = allArticlesLiveData
        canResume()
    }

}
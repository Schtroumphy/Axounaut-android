package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper

class AddCommandVM : ViewModel() {

    var deliveryDateLiveData : MutableLiveData<String> = MutableLiveData("")
    var clientLiveData : MutableLiveData<AppClient?> = MutableLiveData(null)
    var articlesLiveData : MutableLiveData<List<ArticleWrapper>?> = MutableLiveData()

    var canResumeMutableLiveData : MutableLiveData<Boolean> = MutableLiveData(false)
    var canResumeLiveData : LiveData<Boolean> = canResumeMutableLiveData

    fun canResume(){
        var isOk = !deliveryDateLiveData.value.isNullOrEmpty() /* && deliveryDate.isDateIsValid()*/
                && clientLiveData.value != null && !articlesLiveData.value.isNullOrEmpty()
                && articlesLiveData.value!!.any { it.count > 0 }
        Log.d("[AddCommandVM]", "Can resume ? $isOk")
        canResumeMutableLiveData.postValue(isOk)
    }

    fun setDeliveryDate(deliveryDate : String?){
        deliveryDateLiveData.postValue(deliveryDate)
        canResume()
    }

    fun setClientLiveData(client: AppClient ){
        clientLiveData.postValue(client)
        canResume()
    }

    fun setArticlesLiveData(articleWrapper: List<ArticleWrapper> ){
        articlesLiveData.value = articleWrapper
        articlesLiveData = articlesLiveData
        canResume()
    }

}
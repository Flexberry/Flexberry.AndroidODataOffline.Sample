package com.flexberry.androidodataofflinesample

import androidx.lifecycle.MutableLiveData

/**
 * Состояние приложения.
 */
class ApplicationState(
) {
    /**
     * Режим онлайн включен.
     */
    var isOnline: MutableLiveData<Boolean> = MutableLiveData(false)
        private set

    fun setOnline(newValue: Boolean) {
        isOnline.postValue(newValue)
    }
}
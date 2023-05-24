package com.flexberry.androidodataofflinesample

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Состояние приложения.
 */
class ApplicationState(
) {
    /**
     * Режим онлайн включен.
     */
    var isOnline: MutableState<Boolean> = mutableStateOf(false)
        private set

    /**
     * Установить ражим онлайн.
     *
     * @param newValue True если онлайн, иначе false.
     */
    fun setOnline(newValue: Boolean) {
        isOnline.value = newValue
    }
}
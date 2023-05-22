package com.flexberry.androidodataofflinesample

import com.flexberry.androidodataofflinesample.ui.mainmodel.MainViewModel

/**
 * Менеджер режимов онлайн/оффлайн.
 */
class OnlineSwithcer(
) {
    /**
     * Главная форма.
     */
    var mainViewModel: MainViewModel? = null

    /**
     * Режим онлайн включен.
     */
    fun isOnline(): Boolean? {
        return mainViewModel?.state?.isOnline
    }
}
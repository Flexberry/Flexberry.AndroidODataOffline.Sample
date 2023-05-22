package com.flexberry.androidodataofflinesample.ui

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.OnlineSwithcer

/**
 * Общий класс списковых форм.
 *
 * @param readonly Режим только чтение для формы.
 * @param onlineSwithcer Менеджер режимов онлайн/оффлайн.
 */
open class ListFromViewModel(
    private val readonly: Boolean,
    private val onlineSwithcer: OnlineSwithcer
) : ViewModel() {
    /**
     * Режим только чтение включен.
     */
    fun isReadOnly(): Boolean {
        return readonly || onlineSwithcer.isOnline() == false
    }
}
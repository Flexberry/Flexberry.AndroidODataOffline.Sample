package com.flexberry.androidodataofflinesample.ui

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.OnlineSwithcer

/**
 * Общий класс списковых форм.
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
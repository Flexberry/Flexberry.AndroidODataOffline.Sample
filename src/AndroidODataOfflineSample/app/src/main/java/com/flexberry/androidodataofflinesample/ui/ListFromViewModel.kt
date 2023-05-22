package com.flexberry.androidodataofflinesample.ui

import androidx.lifecycle.ViewModel
import com.flexberry.androidodataofflinesample.OnlineSwithcer

open class ListFromViewModel(
    private val readonly: Boolean,
    private val onlineSwithcer: OnlineSwithcer
) : ViewModel() {
    fun isReadOnly(): Boolean {
        return readonly || onlineSwithcer.isOnline() == false
    }
}
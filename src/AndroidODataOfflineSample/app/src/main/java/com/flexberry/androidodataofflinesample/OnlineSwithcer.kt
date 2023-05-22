package com.flexberry.androidodataofflinesample

import com.flexberry.androidodataofflinesample.ui.mainmodel.MainViewModel

class OnlineSwithcer(
) {
    var mainViewModel: MainViewModel? = null

    fun isOnline(): Boolean? {
        return mainViewModel?.state?.isOnline
    }
}
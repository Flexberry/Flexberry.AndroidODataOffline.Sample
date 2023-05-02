package com.flexberry.androidodataofflinesample.data.model

import java.util.UUID

/**
 * Представление внешнего уровня для AppData.
 */
data class AppData(
    val primarykey : UUID,
    val isOnline: Boolean
)
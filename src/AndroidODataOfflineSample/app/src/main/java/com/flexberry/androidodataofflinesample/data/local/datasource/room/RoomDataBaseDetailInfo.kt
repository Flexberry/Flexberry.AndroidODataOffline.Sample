package com.flexberry.androidodataofflinesample.data.local.datasource.room

import kotlin.reflect.KClass

class RoomDataBaseDetailInfo<T : Any>(
    val name: String,
    val kotlinClass: KClass<T>,
    val masterProperty: String
)
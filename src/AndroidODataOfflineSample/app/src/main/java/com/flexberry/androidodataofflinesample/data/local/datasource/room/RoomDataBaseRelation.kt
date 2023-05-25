package com.flexberry.androidodataofflinesample.data.local.datasource.room

import kotlin.reflect.KClass

/**
 * Описание связей дла детейлов и мастеров.
 */
class RoomDataBaseRelation<T : Any>(
    /**
     * Свойство сущности, связанное отношением. Имя детейла или мастера.
     */
    val entityProperty: String,

    /**
     * Тип сущности, которая относится к текущей сущности.
     */
    val kotlinClass: KClass<T>,

    /**
     * Свойство для связывания сущностей.
     */
    val relationProperty: String
)
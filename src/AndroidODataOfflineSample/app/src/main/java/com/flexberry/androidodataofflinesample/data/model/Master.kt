package com.flexberry.androidodataofflinesample.data.model

import java.util.UUID

/**
 * Представление внешнего уровня для [Master].
 */
data class Master(
    val primarykey: UUID,
    val name: String? = null,
    val details: List<Detail>? = null
)

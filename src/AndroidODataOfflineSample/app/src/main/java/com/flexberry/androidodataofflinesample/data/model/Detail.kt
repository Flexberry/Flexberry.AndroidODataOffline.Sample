package com.flexberry.androidodataofflinesample.data.model

import java.util.UUID

/**
 * Представление внешнего уровня для [Detail].
 */
data class Detail(
    val primarykey: UUID,
    val name: String? = null,
    val master: Master,
)

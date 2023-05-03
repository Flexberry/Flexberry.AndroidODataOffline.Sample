package com.flexberry.androidodataofflinesample.data.model

import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

/**
 * Представление внешнего уровня для ApplicationUser.
 */
data class ApplicationUser(
    val primarykey : UUID,
    val createTime: Timestamp,
    val creator: String,
    val editTime: Timestamp,
    val editor: String,
    val name: String,
    val email: String,
    val phone1: String,
    val phone2: String,
    val phone3: String,
    val activated: Boolean,
    val vK: String,
    val facebook: String,
    val twitter: String,
    val birthday: Date,
    val gender: String,
    val vip: Boolean,
    val karma: Double,
    val votes: List<Vote>
)
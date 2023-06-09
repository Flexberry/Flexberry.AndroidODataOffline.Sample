package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

private const val TABLE_NAME = "ApplicationUser"
@Entity(tableName = "ApplicationUser")
data class ApplicationUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "__primaryKey")
    val primarykey : UUID,

    @ColumnInfo(name = "CreateTime")
    val createTime: Timestamp? = null,

    @ColumnInfo(name = "Creator")
    val creator: String? = null,

    @ColumnInfo(name = "EditTime")
    val editTime: Timestamp? = null,

    @ColumnInfo(name = "Editor")
    val editor: String? = null,

    @ColumnInfo(name = "Name")
    val name: String? = null,

    @ColumnInfo(name = "Email")
    var email: String? = null,

    @ColumnInfo(name = "Phone1")
    val phone1: String? = null,

    @ColumnInfo(name = "Phone2")
    val phone2: String? = null,

    @ColumnInfo(name = "Phone3")
    val phone3: String? = null,

    @ColumnInfo(name = "Activated")
    val activated: Boolean? = null,

    @ColumnInfo(name = "VK")
    val vK: String? = null,

    @ColumnInfo(name = "Facebook")
    val facebook: String? = null,

    @ColumnInfo(name = "Twitter")
    val twitter: String? = null,

    @ColumnInfo(name = "Birthday")
    val birthday: Date? = null,

    @ColumnInfo(name = "Gender")
    val gender: String? = null,

    @ColumnInfo(name = "Vip")
    val vip: Boolean? = null,

    @ColumnInfo(name = "Karma")
    val karma: Double? = null
) {
    companion object {
        const val tableName: String = TABLE_NAME
    }
}
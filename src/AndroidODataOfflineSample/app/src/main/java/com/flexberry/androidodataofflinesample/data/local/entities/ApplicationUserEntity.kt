package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import com.flexberry.androidodataofflinesample.data.model.Vote
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

@Entity(tableName = "ApplicationUser")
data class ApplicationUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "__primaryKey")
    val primarykey : UUID,

    @ColumnInfo(name = "CreateTime")
    val createTime: Timestamp,

    @ColumnInfo(name = "Creator")
    val creator: String,

    @ColumnInfo(name = "EditTime")
    val editTime: Timestamp,

    @ColumnInfo(name = "Editor")
    val editor: String,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Email")
    val email: String,

    @ColumnInfo(name = "Phone1")
    val phone1: String,

    @ColumnInfo(name = "Phone2")
    val phone2: String,

    @ColumnInfo(name = "Phone3")
    val phone3: String,

    @ColumnInfo(name = "Activated")
    val activated: Boolean,

    @ColumnInfo(name = "VK")
    val vK: String,

    @ColumnInfo(name = "Facebook")
    val facebook: String,

    @ColumnInfo(name = "Twitter")
    val twitter: String,

    @ColumnInfo(name = "Birthday")
    val birthday: Date,

    @ColumnInfo(name = "Gender")
    val gender: String,

    @ColumnInfo(name = "Vip")
    val vip: Boolean,

    @ColumnInfo(name = "Karma")
    val karma: Double
)

fun ApplicationUserEntity.asExternalModel() = ApplicationUser(
    primarykey = primarykey,
    createTime = createTime,
    creator = creator,
    editTime = editTime,
    editor = editor,
    name = name,
    email = email,
    phone1 = phone1,
    phone2 = phone2,
    phone3 = phone3,
    activated = activated,
    vK = vK,
    facebook = facebook,
    twitter = twitter,
    birthday = birthday,
    gender = gender,
    vip = vip,
    karma = karma
)

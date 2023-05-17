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
    val email: String? = null,

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
)

/*fun ApplicationUserEntity.asExternalModel() = ApplicationUser(
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
)*/

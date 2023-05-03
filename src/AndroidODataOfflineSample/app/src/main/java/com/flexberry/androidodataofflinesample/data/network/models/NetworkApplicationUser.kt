package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

/**
 * Network representation of [ApplicationUser]
 */
data class NetworkApplicationUser(
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
)

fun NetworkApplicationUser.asEntity() = ApplicationUserEntity(
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
    karma = karma,
    votes = emptyList<VoteEntity>()
)
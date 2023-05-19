package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import java.util.Date
import java.util.UUID

/**
 * Представление внешнего уровня для ApplicationUser.
 */
data class ApplicationUser(
    val primarykey : UUID,
    val createTime: Date? = null,
    val creator: String? = null,
    val editTime: Date? = null,
    val editor: String? = null,
    val name: String? = null,
    val email: String,
    val phone1: String? = null,
    val phone2: String? = null,
    val phone3: String? = null,
    val activated: Boolean? = null,
    val vK: String? = null,
    val facebook: String? = null,
    val twitter: String? = null,
    val birthday: Date? = null,
    val gender: String? = null,
    val vip: Boolean? = null,
    val karma: Double? = null,
    val votes: List<Vote>? = null
)

fun NetworkApplicationUser.asLocalModel() = ApplicationUser(
    primarykey = this.__PrimaryKey,
    createTime = this.CreateTime,
    creator = this.Creator,
    editTime = this.EditTime,
    editor = this.Editor,
    name = this.Name,
    email = this.EMail,
    phone1 = this.Phone1,
    phone2 = this.Phone2,
    phone3 = this.Phone3,
    activated = this.Activated,
    vK = this.VK,
    facebook = this.Facebook,
    twitter = this.Twitter,
    birthday = this.Birthday,
    gender = this.Gender,
    vip = this.Vip,
    karma = this.Karma,
    votes = this.Votes?.map { vote -> vote.asLocalModel() },
)
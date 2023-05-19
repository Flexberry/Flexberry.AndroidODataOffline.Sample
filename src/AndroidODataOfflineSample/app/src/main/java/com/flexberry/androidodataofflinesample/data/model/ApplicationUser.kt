package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import java.util.Date
import java.util.UUID

/**
 * Представление внешнего уровня для [ApplicationUser].
 */
data class ApplicationUser (
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

/**
 * Преобразование [NetworkApplicationUser] в [ApplicationUser].
 */
fun NetworkApplicationUser.asLocalModel() = networkApplicationUserAsLocalModel(this)

/**
 * Преобразование [NetworkApplicationUser] в [ApplicationUser].
 * Необходимо для подавления ошибки о рекурсивности вызовов преобразования.
 *
 * @param dataObject Объект данных.
 */
private fun networkApplicationUserAsLocalModel(dataObject: NetworkApplicationUser): ApplicationUser {
    return ApplicationUser(
        primarykey = dataObject.__PrimaryKey,
        createTime = dataObject.CreateTime,
        creator = dataObject.Creator,
        editTime = dataObject.EditTime,
        editor = dataObject.Editor,
        name = dataObject.Name,
        email = dataObject.EMail,
        phone1 = dataObject.Phone1,
        phone2 = dataObject.Phone2,
        phone3 = dataObject.Phone3,
        activated = dataObject.Activated,
        vK = dataObject.VK,
        facebook = dataObject.Facebook,
        twitter = dataObject.Twitter,
        birthday = dataObject.Birthday,
        gender = dataObject.Gender,
        vip = dataObject.Vip,
        karma = dataObject.Karma,
        votes = dataObject.Votes?.map { vote -> vote.asLocalModel() },
    )
}
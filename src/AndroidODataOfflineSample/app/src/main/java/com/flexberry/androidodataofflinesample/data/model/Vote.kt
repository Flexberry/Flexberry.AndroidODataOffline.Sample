package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import java.util.Date
import java.util.UUID

/**
 * Представление внешнего уровня для [Vote].
 */
data class Vote (
    val primarykey : UUID,
    val createTime: Date? = null,
    val creator: String? = null,
    val editTime: Date? = null,
    val editor: String? = null,
    val voteType: VoteType? = null,
    val author: ApplicationUser
)

/**
 * Преобразование [NetworkVote] в [Vote].
 */
fun NetworkVote.asDataModel() = networkVoteAsDataModel(this)

/**
 * Преобразование [NetworkVote] в [Vote].
 * Необходимо для подавления ошибки о рекурсивности вызовов преобразования.
 *
 * @param dataObject Объект данных.
 */
private fun networkVoteAsDataModel(dataObject: NetworkVote): Vote {
    return Vote(
        primarykey = dataObject.__PrimaryKey,
        createTime = dataObject.CreateTime,
        creator = dataObject.Creator,
        editTime = dataObject.EditTime,
        editor = dataObject.Editor,
        voteType = dataObject.VoteType,
        author = dataObject.Author.asDataModel()
    )
}
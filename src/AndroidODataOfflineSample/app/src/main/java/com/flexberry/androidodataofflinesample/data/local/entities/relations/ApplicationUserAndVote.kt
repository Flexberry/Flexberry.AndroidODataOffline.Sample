package com.flexberry.androidodataofflinesample.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity

data class ApplicationUserAndVote(
    @Embedded val user: ApplicationUserEntity,
    @Relation(
        parentColumn = "__primaryKey",
        entityColumn = "applicationUserId"
    )
    val votes: List<VoteEntity>
)

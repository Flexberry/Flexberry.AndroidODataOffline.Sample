package com.flexberry.androidodataofflinesample.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity

data class VoteWithUser(
    @Embedded val vote: VoteEntity,
    @Relation(
        parentColumn = "ApplicationUser",
        entityColumn = "__primaryKey"
    )
    val user: ApplicationUserEntity
)
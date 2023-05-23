package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

/**
 * Сущность [Master].
 */
@Entity(tableName = DetailEntity.tableName)
data class DetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    val primarykey : UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "master")
    val masterId: UUID,
) {
    companion object {
        const val tableName = "Detail"
    }
}

data class DetailRelations(
    @Embedded
    val detail: DetailEntity,

    @Relation(
        parentColumn = "master",
        entityColumn = "primaryKey"
    )
    val master: MasterEntity
)

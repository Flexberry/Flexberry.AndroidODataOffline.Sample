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
@Entity(tableName = MasterEntity.tableName)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    val primarykey : UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String? = null,
) {
    companion object {
        const val tableName = "Master"
    }
}

data class MasterWithRelations(
    @Embedded
    val master: MasterEntity,

    @Relation(
        entity = DetailEntity::class,
        parentColumn = "primaryKey",
        entityColumn = "master"
    )
    val details: List<DetailEntity>? = null
)
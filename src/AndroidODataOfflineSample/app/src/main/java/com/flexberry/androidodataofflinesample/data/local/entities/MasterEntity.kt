package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Сущность [Master].
 */
@Entity(tableName = MasterEntity.tableName)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    val primarykey: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String? = null,
) {
    constructor(
        primarykey: UUID = UUID.randomUUID(),
        name: String? = null,
        details: List<DetailEntity>? = null
    ) : this(
        primarykey = primarykey,
        name = name
    ) {
        this.details = details
    }

    @Ignore
    var details: List<DetailEntity>? = null

    companion object {
        const val tableName = "Master"
    }
}
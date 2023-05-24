package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Сущность [Master].
 */
@Entity(tableName = DetailEntity.tableName)
data class DetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    val primarykey: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "master")
    var masterId: UUID,
) {
    constructor(
        primarykey: UUID = UUID.randomUUID(),
        name: String? = null,
        master: MasterEntity
    ) : this(
        primarykey = primarykey,
        name = name,
        masterId = master.primarykey) {
        this.master = master
    }

    @Ignore
    var master: MasterEntity? = null

    companion object {
        const val tableName = "Detail"
    }
}
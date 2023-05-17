package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flexberry.androidodataofflinesample.data.model.AppData
import java.util.UUID

@Entity(tableName = "AppData")
data class AppDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "__primaryKey")
    val primarykey : UUID,

    @ColumnInfo(name = "IsOnline")
    val isOnline: Boolean
)

/*
fun AppDataEntity.asExternalModel() = AppData(
    primarykey = primarykey,
    isOnline = isOnline
)*/

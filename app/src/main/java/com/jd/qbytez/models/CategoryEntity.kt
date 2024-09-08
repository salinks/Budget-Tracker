package com.jd.qbytez.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bt_categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryID")
    var categoryID: Int=0,

    @ColumnInfo(name = "categoryName")
    var categoryName: String,

    @ColumnInfo(name = "transactionType")
    var transactionType: String,

    @ColumnInfo(name = "iconId")
    var iconId: Int
)



package com.jd.qbytez.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bt_transactions")
data class TransactionsEntity(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "amount")
    var amount: Double,

    @ColumnInfo(name = "transactionType")
    var transactionType: String,

    @ColumnInfo(name = "categoryName")
    var categoryName: String,

    @ColumnInfo(name = "categoryIconId")
    var categoryIconId: Int,

    @ColumnInfo(name = "transactionDate")
    var transactionDate: String,

    @ColumnInfo(name = "transactionDescription")
    var transactionDescription: String,

    @ColumnInfo(name = "createdOn")
    var createdOn: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    var transactionId: Int = 0
)


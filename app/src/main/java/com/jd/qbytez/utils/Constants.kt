package com.jd.qbytez.utils

object Constants {


    fun transactionType(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Income")
        list.add("Expense")
        return list
    }


    val chartDisplay = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )

    val monthNum = listOf(
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12"
    )
}
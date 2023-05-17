package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

open class RoomDataSource<T: Any>(
    protected val db: LocalDatabase,
    private val queryParams: QuerySettings) {

    open fun createObjects(listObjects: List<T>): Int {
        return 0;
    }

    open fun readObjects(querySettings: QuerySettings? = null): List<T> {
        return emptyList();
    }

    open fun updateObjects(listObjects: List<T>): Int {
        return 0;
    }

    open fun deleteObjects(listObjects: List<T>): Int {
        return 0;
    }

    private fun QuerySettings.getRoomDataSourceValue(): String {
        val elements: MutableList<String> = mutableListOf()

        if (this.selectList != null) {
            val selectValue = this.selectList!!.joinToString(",")
            val selectValueFull = if (selectValue.isNullOrEmpty()) "*" else selectValue
            elements.add(selectValueFull)
        }

        if (this.filterValue != null) {
            val filterVal = "${this.filterValue!!.getRoomDataSourceValue()}"
            val filterValFull = if (filterVal.isNullOrEmpty()) "" else "where $filterVal"
            elements.add(filterValFull)
        }

        if (this.orderList != null) {
            val orderValue = this.orderList!!
                .joinToString(",") { x -> "${x.first} ${x.second.getRoomDataSourceValue()}"}
            val orderValueFull = if (orderValue.isNullOrEmpty()) "" else "order by $orderValue"
            elements.add(orderValueFull)
        }

        // В postgresql нет top. Limit ok?
        if (this.topValue != null) {
            elements.add("limit ${this.topValue}")
        }

        // В postgresql нет skip. Offset ok?
        if (this.skipValue != null) {
            elements.add("offset ${this.skipValue}")
        }

        return elements.joinToString("&")
    }

    private fun Filter.getRoomDataSourceValue(): String {
        var result = ""

        when (this.filterType) {
            FilterType.Equal,
            FilterType.NotEqual,
            FilterType.Greater,
            FilterType.GreaterOrEqual,
            FilterType.Less,
            FilterType.LessOrEqual,
            FilterType.Has -> {
                result = "$paramName ${filterType.getRoomDataSourceValue()} '$paramValue'"
            }

            FilterType.Contains,
            FilterType.StartsWith,
            FilterType.EndsWith -> {
                result = "${filterType.getRoomDataSourceValue()}($paramName,'$paramValue')"
            }

            FilterType.And -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getRoomDataSourceValue()} ")
                    { x -> x.getRoomDataSourceValue() }.toString()
            }

            FilterType.Or -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getRoomDataSourceValue()} ")
                    { x -> "(${x.getRoomDataSourceValue()})" }.toString()
            }

            FilterType.Not -> {
                result = "${filterType.getRoomDataSourceValue()} ${filterParams?.get(0)
                    ?.getRoomDataSourceValue()}"
            }
        }

        return result
    }

    private fun OrderType.getRoomDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

    private fun FilterType.getRoomDataSourceValue(): String {
        return when (this) {
            FilterType.Equal -> "eq"
            FilterType.NotEqual -> "neq"
            FilterType.Greater -> "gt"
            FilterType.GreaterOrEqual -> "ge"
            FilterType.Less -> "lt"
            FilterType.LessOrEqual -> "le"
            FilterType.Has -> "has"
            FilterType.Contains -> "contains"
            FilterType.StartsWith -> "startswith"
            FilterType.EndsWith -> "endswith"
            FilterType.And -> "and"
            FilterType.Or -> "or"
            FilterType.Not -> "not"
        }
    }
}
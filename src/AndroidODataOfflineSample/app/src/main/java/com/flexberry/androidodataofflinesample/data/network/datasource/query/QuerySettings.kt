package com.flexberry.androidodataofflinesample.data.network.datasource.query

class QuerySettings(
    var filterValue: Filter? = null,
    var orderList: MutableList<Pair<String, OrderType>>? = null,
    var selectList: MutableList<String>? = null,
    var topValue: Int? = null,
    var skipValue: Int? = null)
{
    fun filter(filterToAdd: Filter): QuerySettings {
        filterValue = if (filterValue == null) {
            filterToAdd
        } else {
            Filter.andFilter(listOf(filterValue!!, filterToAdd))
        }

        return this
    }

    fun orderBy(propName: String, orderType: OrderType = OrderType.Asc): QuerySettings {
        initOrderList()

        val ind = orderList?.indexOfFirst { x -> x.first == propName } ?: -1

        if (ind >= 0) {
            orderList?.set(ind, Pair(propName, orderType))
        } else {
            orderList?.add(Pair(propName, orderType))
        }

        return this;
    }

    fun orderByDescending(propName: String): QuerySettings {
        return orderBy(propName, OrderType.Desc)
    }

    fun top(topValueToSet: Int): QuerySettings {
        topValue = topValueToSet

        return this
    }

    fun skip(skipValueToSet: Int): QuerySettings {
        skipValue = skipValueToSet

        return this
    }

    private fun initOrderList() {
        if (orderList == null) {
            orderList = mutableListOf()
        }
    }

    private fun initSelectList() {
        if (selectList == null) {
            selectList = mutableListOf()
        }
    }
}
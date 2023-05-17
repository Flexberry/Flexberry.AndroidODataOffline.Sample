package com.flexberry.androidodataofflinesample.data.query

class QuerySettings(
    var filterValue: Filter? = null,
    var orderList: MutableList<Pair<String, OrderType>>? = null,
    var selectList: MutableList<String>? = null,
    var topValue: Int? = null,
    var skipValue: Int? = null)
{
    fun filter(vararg filters: Filter): QuerySettings {
        filterValue = if (filterValue == null) {
            Filter.andFilter(filters.asList())
        } else {
            val lst = mutableListOf(filterValue!!)

            lst.addAll(filters)
            Filter.andFilter(lst)
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

        return this
    }

    fun orderByDescending(propName: String): QuerySettings {
        return orderBy(propName, OrderType.Desc)
    }

    fun select(propName: String): QuerySettings {
        initSelectList()

        val ind = selectList?.indexOf(propName) ?: -1

        if (ind < 0) {
            selectList?.add(propName)
        }

        return this
    }

    fun select(propNames: List<String>): QuerySettings {
        propNames.forEach { x -> select(x) }

        return this
    }

    fun select(vararg propNames: String): QuerySettings {
        return select(propNames.asList())
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
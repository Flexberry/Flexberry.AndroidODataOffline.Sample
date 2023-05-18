package com.flexberry.androidodataofflinesample.data.query

/**
 * Настройки запроса.
 *
 * @param filterValue Ограничение.
 * @param orderList Список сортировки, пары ИмяСвойства+Направление.
 * @param selectList Cписок свойств для выбора.
 * @param topValue Сколько вернуть записей из итогового результата.
 * @param skipValue Сколько пропустить записей в итоговом результате.
 */
class QuerySettings(
    var filterValue: Filter? = null,
    var orderList: MutableList<Pair<String, OrderType>>? = null,
    var selectList: MutableList<String>? = null,
    var topValue: Int? = null,
    var skipValue: Int? = null)
{
    /**
     * Задать ограничение.
     *
     * @param filters Ограничения.
     */
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

    /**
     * Задать направление сортировки.
     *
     * @param propName Имя свойства.
     * @param orderType Направление сортировки. Если не задано, то по возратанию.
     */
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

    /**
     * Задать направление сортировки по убыванию.
     *
     * @param propName Имя свойства.
     */
    fun orderByDescending(propName: String): QuerySettings {
        return orderBy(propName, OrderType.Desc)
    }

    /**
     * Добавить поле для выбора.
     *
     * @param propName Имя поля для выбора.
     */
    fun select(propName: String): QuerySettings {
        initSelectList()

        val ind = selectList?.indexOf(propName) ?: -1

        if (ind < 0) {
            selectList?.add(propName)
        }

        return this
    }

    /**
     * Добавить поля для выбора.
     *
     * @param propNames Имена полей для выбора.
     */
    fun select(propNames: List<String>): QuerySettings {
        propNames.forEach { x -> select(x) }

        return this
    }

    /**
     * Добавить поля для выбора.
     *
     * @param propNames Имена полей для выбора.
     */
    fun select(vararg propNames: String): QuerySettings {
        return select(propNames.asList())
    }

    /**
     * Задать количество записей из итогового результата.
     *
     * @param topValueToSet Количество записей из итогового результата.
     */
    fun top(topValueToSet: Int): QuerySettings {
        topValue = topValueToSet

        return this
    }

    /**
     * Задать сколько пропустить записей в итоговом результате.
     *
     * @param skipValueToSet Количество записей для пропуска.
     */
    fun skip(skipValueToSet: Int): QuerySettings {
        skipValue = skipValueToSet

        return this
    }

    /**
     * Инициализация списка сортировки.
     */
    private fun initOrderList() {
        if (orderList == null) {
            orderList = mutableListOf()
        }
    }

    /**
     * Инициализация списка выбора.
     */
    private fun initSelectList() {
        if (selectList == null) {
            selectList = mutableListOf()
        }
    }
}
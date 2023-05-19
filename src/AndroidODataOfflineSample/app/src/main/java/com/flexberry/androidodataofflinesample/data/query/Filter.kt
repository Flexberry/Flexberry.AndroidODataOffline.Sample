package com.flexberry.androidodataofflinesample.data.query

/**
 * Ограничение.
 *
 * @param filterType Тип ограничения.
 * @param filterParams Вложенные ограничения.
 * @param paramName Имя параметра.
 * @param paramValue Зачение параметра.
 */
class Filter(
    val filterType: FilterType,
    val filterParams: List<Filter>? = null,
    val paramName: String? = null,
    val paramValue: Any? = null) {

    companion object {
        /**
         * Ограничение типа [FilterType.Equal].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun equalFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.Equal, null,  paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.NotEqual].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun notEqualFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.NotEqual, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.Greater].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun greaterFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.Greater, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.GreaterOrEqual].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun greaterOrEqualFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.GreaterOrEqual, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.Less].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun lessFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.Less, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.LessOrEqual].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun lessOrEqualFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.LessOrEqual, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.Has].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun hasFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.Has, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.Contains].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun containsFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.Contains, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.StartsWith].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun startsWithFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.StartsWith, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.EndsWith].
         *
         * @param paramName Имя параметра.
         * @param paramValue Зачение параметра.
         */
        fun endsWithFilter(paramName: String, paramValue: Any): Filter {
            return Filter(FilterType.EndsWith, null, paramName, paramValue)
        }

        /**
         * Ограничение типа [FilterType.And].
         *
         * @param filterParams Вложенные ограничения.
         */
        fun andFilter(filterParams: List<Filter>): Filter {
            return Filter(FilterType.And, filterParams)
        }

        /**
         * Ограничение типа [FilterType.And].
         *
         * @param filterParams Вложенные ограничения.
         */
        fun andFilter(vararg filterParams: Filter): Filter {
            return andFilter(filterParams.asList())
        }

        /**
         * Ограничение типа [FilterType.Or].
         *
         * @param filterParams Вложенные ограничения.
         */
        fun orFilter(filterParams: List<Filter>): Filter {
            return Filter(FilterType.Or, filterParams)
        }

        /**
         * Ограничение типа [FilterType.Or].
         *
         * @param filterParams Вложенные ограничения.
         */
        fun orFilter(vararg filterParams: Filter): Filter {
            return orFilter(filterParams.asList())
        }

        /**
         * Ограничение типа [FilterType.Not].
         *
         * @param filterParam Ограничение.
         */
        fun notFilter(filterParam: Filter): Filter {
            return Filter(FilterType.Not, listOf(filterParam))
        }

        /**
         * Ограничение типа IN. Параметр должен быть равен одному из значений, по типу paramValues.contains(paramName).
         *
         * @param paramName Имя параметра.
         * @param paramValues Набор значений параметра.
         */
        fun inFilter(paramName: String, paramValues: List<Any>): Filter {
            return orFilter(paramValues.map { equalFilter(paramName, it) })
        }
    }
}

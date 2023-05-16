package com.flexberry.androidodataofflinesample.data.network.datasource.query

class Filter(
    val filterType: FilterType,
    val filterParams: List<Filter>? = null,
    val paramName: String? = null,
    val paramValue: String? = null) {

    companion object {
        fun equalFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.Equal, null,  paramName, paramValue)
        }

        fun notEqualFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.NotEqual, null, paramName, paramValue)
        }

        fun greaterFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.Greater, null, paramName, paramValue)
        }

        fun greaterOrEqualFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.GreaterOrEqual, null, paramName, paramValue)
        }

        fun lessFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.Less, null, paramName, paramValue)
        }

        fun lessOrEqualFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.LessOrEqual, null, paramName, paramValue)
        }

        fun hasFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.Has, null, paramName, paramValue)
        }

        fun containsFilter(paramName: String, paramValue: String): Filter {
            return Filter(FilterType.Contains, null, paramName, paramValue)
        }

        fun andFilter(filterParams: List<Filter>): Filter {
            return Filter(FilterType.And, filterParams)
        }

        fun orFilter(filterParams: List<Filter>): Filter {
            return Filter(FilterType.Or, filterParams)
        }

        fun notFilter(filterParam: Filter): Filter {
            return Filter(FilterType.Not, listOf(filterParam))
        }
    }
}


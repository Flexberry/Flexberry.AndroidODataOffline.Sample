package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import android.util.Log
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaField

/**
 * Общий источник данных OData.
 */
open class OdataDataSourceCommon {
    /**
     * Параметры для формирования URL к OData.
     */
    private class UrlParamNames {
        companion object {
            /**
             * Имя параметра списка выбора.
             */
            const val select: String = "\$select"

            /**
             * Имя параметра ограничения.
             */
            const val filter: String = "\$filter"

            /**
             * Имя параметра сортировки.
             */
            const val order: String = "\$orderby"

            /**
             * Имя параметра количества возвращаемых объектов.
             */
            const val top: String = "\$top"

            /**
             * Имя параметра количества пропускаемых объектов.
             */
            const val skip: String = "\$skip"

            /**
             * Имя параметра для расширения списка возвращаемых свойств объекта.
             */
            const val expand: String = "\$expand"
        }
    }

    /**
     * Стратегия обработки свойств объекта при конвертации его в JSON.
     *
     * @param odataTypeInfo Информация о типе объекта.
     * @see [ExclusionStrategy]
     */
    private class OdataExclusionStrategy(val odataTypeInfo: OdataDataSourceTypeInfo<*>):
        ExclusionStrategy {

        /**
         * Принятие решения о пропуске свойства объекта.
         */
        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            val fieldName = f?.name
            val fieldType = f?.declaredClass?.simpleName
            val fieldOdataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(fieldType)

            // Если есть детейл, или поле является мастером, то должны его пропустить.
            return odataTypeInfo.hasDetail(fieldName)
                    || (fieldOdataTypeInfo != null && !fieldOdataTypeInfo.isEnum)
        }

        /**
         * Принятие решения о пропуске типа объкета.
         */
        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }
    }

    /**
     * OData URL.
     */
    private val odataUrl = "http://stands-backend.flexberry.net/odata"

    /**
     * Имя свойства первичного ключа.
     */
    private val primaryKeyPropertyName = "__PrimaryKey"

    /**
     * Формат даты в OData.
     */
    private val odataDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    /**
     * Создать объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(vararg dataObjects: Any): Int {
        return createObjects(dataObjects.asList())
    }

    /**
     * Создать объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            // Имя типа объекта.
            val odataObjectSimpleName = dataObject::class.simpleName
            // Информация о типе объекта.
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            // Имя объекта в OData.
            val odataObjectName = odataTypeInfo.fullOdataTypeName
            // Url запроса.
            val url = URL("$odataUrl/$odataObjectName")
            // Преобразуем объект в JSON.
            var jsonObject = getGson(odataTypeInfo).toJson(dataObject)

            // Добавляем мастеров.
            jsonObject = addMasters(dataObject, jsonObject)

            // Соединение.
            val connection = url.openConnection() as HttpURLConnection

            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Content-Length", jsonObject.length.toString())

            DataOutputStream(connection.outputStream).use { it.writeBytes(jsonObject) }

            if (connection.responseCode == 201) {
                objectsCount++
            } else {
                Log.e("ERROR", "Failed to create object. Failed Connection.")
                Log.d("OBJECT", dataObject.toString())
            }

            connection.disconnect()

            // Еще надо найти детейлы (атрибуты типа List<OdataType>).
            // И сохранить детейлы отдельно. Т.к. из json основного объекта они исключены.
            dataObject::class.declaredMemberProperties
                .filter { x -> odataTypeInfo.hasDetail(x.name) }
                .forEach { detailProperty ->
                    // Детейловое свойство.
                    val detailPropertyValue = (detailProperty as KProperty1<Any, List<*>?>).get(dataObject)
                    // Список детейловых объектов.
                    val detailList = detailPropertyValue?.filterNotNull()

                    if (detailList?.any() == true) {
                        // Создание детейлов.
                        createObjects(detailList)
                    }
                }
        }

        return objectsCount
    }

    /**
     * Вычитать объекты.
     *
     * @param querySettings Параметры ограничения.
     * @param kotlinClass Класс объекта.
     * @return Список объектов.
     */
    fun readObjects(kotlinClass: KClass<*>, querySettings: QuerySettings? = null): List<Any> {
        // Имя типа объекта.
        val odataObjectSimpleName = kotlinClass.simpleName
        // Информация о типе объекта.
        val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
        // Имя объекта в OData.
        val odataObjectName = odataTypeInfo.fullOdataTypeName
        // Итоговые параметры запроса.
        val queryParams = mutableListOf<String>()
        // Значение расширения для вычитываемых свойств.
        val expandValue = getRequestExtension(kotlinClass)

        // Добавляем настройки вычитки.
        if (querySettings != null) {
            queryParams.add(querySettings.getOdataDataSourceValue())
        }

        // Добавляем расширение вычитки.
        if (expandValue != null) {
            queryParams.add(expandValue)
        }

        // Итоговые параметры запроса в URL.
        val queryParamsValue = if (queryParams.any()) {
            "?${queryParams.joinToString("&")}"
        } else {
            ""
        }

        // Url запроса.
        val url = URL("$odataUrl/$odataObjectName$queryParamsValue")
        // Итоговый список вычитанных объектов.
        val resultList = mutableListOf<Any>()
        // Соединение.
        val connection  = url.openConnection() as HttpURLConnection

        if(connection.responseCode == 200)
        {
            // Входящий поток данных.
            val inputSystem = connection.inputStream
            val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
            // Все данные в строке.
            val jsonString = inputStreamReader.readText()
            // Все данные в JSON.
            val jsonValue = JSONObject(jsonString)
            // Объекты приходят в массиве "value".
            val objectsArray = jsonValue.getJSONArray("value")

            for (i in 0 until objectsArray.length()) {
                val objectJson = objectsArray[i].toString()

                try {
                    // Преобразование JSON  в объект данных.
                    val objectValue = getGson(odataTypeInfo).fromJson(objectJson, kotlinClass.java)

                    resultList.add(objectValue)
                }
                catch (e: Exception) {
                    Log.e("ERROR", "Error in odata request", e)
                }
            }

            inputStreamReader.close()
            inputSystem.close()
        }
        else
        {
            Log.e("ERROR", "Failed to read objects. Failed Connection.")
            Log.d("URL", url.toString())
        }

        return resultList
    }

    /**
     * Вычитать объекты.
     *
     * @param T Тип объекта.
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    inline fun <reified T: Any> readObjects(querySettings: QuerySettings? = null) : List<T> {
        return readObjects(T::class, querySettings) as List<T>
    }

    /**
     * Обновить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(vararg dataObjects: Any): Int {
        return updateObjects(dataObjects.asList())
    }

    /**
     * Обновить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            // Имя типа объекта.
            val odataObjectSimpleName = dataObject::class.simpleName
            // Информация о типе объекта.
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            // Имя объекта в OData.
            val odataObjectName = odataTypeInfo.fullOdataTypeName
            // Свойство primaryKey.
            val primaryKeyProperty = dataObject::class.members.first {it.name == primaryKeyPropertyName } as KProperty1<Any, UUID>
            // Значение primaryKey.
            val pkValue = primaryKeyProperty.get(dataObject)
            // Url запроса.
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
            // Преобразуем объект в JSON.
            var jsonObject = getGson(odataTypeInfo).toJson(dataObject)

            // Добавляем мастеров.
            jsonObject = addMasters(dataObject, jsonObject)

            // Соединение.
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH")
            connection.requestMethod = "PATCH"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Content-Length", jsonObject.length.toString())

            DataOutputStream(connection.outputStream).use { it.writeBytes(jsonObject) }

            if (connection.responseCode == 204) {
                objectsCount++
            }
            else {
                Log.e("ERROR", "Filed to update object $pkValue. Failed Connection.")
                Log.d("OBJECT", dataObject.toString())
            }

            connection.disconnect()
        }

        return objectsCount
    }

    /**
     * Удалить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(vararg dataObjects: Any): Int {
        return deleteObjects(dataObjects.asList())
    }

    /**
     * Удалить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            // Имя типа объекта.
            val odataObjectSimpleName = dataObject::class.simpleName
            // Информация о типе объекта.
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            // Имя объекта в OData.
            val odataObjectName = odataTypeInfo.fullOdataTypeName
            // Свойство primaryKey.
            val primaryKeyProperty = dataObject::class.members.first {it.name == primaryKeyPropertyName } as KProperty1<Any, UUID>
            // Значение primaryKey.
            val pkValue = primaryKeyProperty.get(dataObject)
            // Url запроса.
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
            // Соединение.
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("X-HTTP-Method-Override", "DELETE")
            connection.requestMethod = "DELETE"

            if (connection.responseCode == 204) {
                objectsCount++
            } else {
                Log.e("ERROR", "Filed to delete object $pkValue. Failed Connection.")
                Log.d("OBJECT", dataObject.toString())
            }

            connection.disconnect()
        }

        return objectsCount
    }

    /**
     * Добавить данные по мастерам объекта.
     * Мастера должны идти как "ИмяМастера@odata.bind": "ИмяОДатаТипа(Ключ)".
     *
     * @param dataObject Объект данных.
     * @param jsonObject JSON строка.
     * @return JSON строка с добавленными мастерами объекта.
     */
    private fun addMasters(dataObject: Any, jsonObject: String): String {
        val jsonValue = JSONObject(jsonObject)
        val odataObjectClass = dataObject::class

        // Берем свойства, которые являются мастерами.
        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type
            val propTypeName = propType?.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(propTypeName)

            if (odataTypeInfo != null && !odataTypeInfo.isEnum) {
                // Мастеровой объект.
                val masterObject = (prop as KProperty1<Any, *>).get(dataObject)

                if (masterObject != null && propType != null) {
                    // Если у него есть свойство primaryKey.
                    val pkProperty = propType.kotlin.declaredMemberProperties
                        .firstOrNull { x -> x.name == primaryKeyPropertyName }

                    // Если есть значение primaryKey.
                    if (pkProperty != null) {
                        val pkValue = (pkProperty as KProperty1<Any, UUID>).get(masterObject)

                        // Добавляем в JSON.
                        jsonValue.put(
                            "$propName@odata.bind",
                            "${odataTypeInfo.fullOdataTypeName}($pkValue)"
                        )
                    }
                }
            }
        }

        // TODO: Костыль!!!
        if (odataObjectClass.simpleName == "NetworkVote") {
            jsonValue.put("Suggestion@odata.bind", "EmberFlexberryDummySuggestions(0b76edce-5900-45ec-a958-b9d6bb943b2d)")
        }

        return jsonValue.toString()
    }

    /**
     * Получить расширение свойств объекта для вычитки.
     *
     * @param odataObjectClass Класс объекта.
     */
    /// TODO: тут в будущем появится параметр в виде представления объекта.
    private fun getRequestExtension(odataObjectClass: KClass<*>): String? {
        val resultList = mutableListOf<String>()

        // Берем всех мастеров объекта.
        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type?.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(propType)

            if (odataTypeInfo != null && !odataTypeInfo.isEnum) {
                // Добавляем расширение, чтобы нам вернули primaryKey каждого мастера.
                val elem = "$propName(${UrlParamNames.select}=$primaryKeyPropertyName)"

                resultList.add(elem)
            }
        }

        // TODO: Костыль!!!
        if (odataObjectClass.simpleName == "NetworkVote") {
            val elem = "Suggestion(${UrlParamNames.select}=$primaryKeyPropertyName)"

            resultList.add(elem)
        }

        return if (resultList.any()) {
            "${UrlParamNames.expand}=${resultList.joinToString(",")}"
        } else {
            null
        }
    }

    /**
     * Получить JSON преобразователь объектов.
     *
     * @param typeInfo Информация о типе объекта.
     * @return Gson преобразователь.
     * @see [Gson]
     */
    private fun getGson(typeInfo: OdataDataSourceTypeInfo<*>): Gson {
        return GsonBuilder()

            // Устанавливаем формат даты.
            .setDateFormat(odataDateTimeFormat)

            // Обрабьотка исключений при формировании JSON.
            .addSerializationExclusionStrategy(OdataExclusionStrategy(typeInfo))
            .create()
    }

    /**
     * Получить строковое значение для [QuerySettings].
     *
     * @return Строковое значение.
     */
    private fun QuerySettings.getOdataDataSourceValue(): String {
        val elements: MutableList<String> = mutableListOf()

        if (this.selectList != null) {
            val selectValue = this.selectList!!.joinToString(",")
            elements.add("${UrlParamNames.select}=$selectValue")
        }

        if (this.filterValue != null) {
            elements.add("${UrlParamNames.filter}=${this.filterValue!!.getOdataDataSourceValue()}")
        }

        if (this.orderList != null) {
            val orderValue = this.orderList!!
                .joinToString(",") { x -> "${x.first} ${x.second.getOdataDataSourceValue()}"}
            elements.add("${UrlParamNames.order}=$orderValue")
        }

        if (this.topValue != null) {
            elements.add("${UrlParamNames.top}=${this.topValue}")
        }

        if (this.skipValue != null) {
            elements.add("${UrlParamNames.skip}=${this.skipValue}")
        }

        return elements.joinToString("&")
    }

    /**
     * Получить строковое значение для [Filter].
     *
     * @return Строковое значение.
     */
    private fun Filter.getOdataDataSourceValue(): String {
        var result = ""

        when (this.filterType) {
            FilterType.Equal,
            FilterType.NotEqual,
            FilterType.Greater,
            FilterType.GreaterOrEqual,
            FilterType.Less,
            FilterType.LessOrEqual,
            FilterType.Has -> {
                result = "${evaluateParamNameAsString(paramName)} ${filterType.getOdataDataSourceValue()} ${evaluateParamValueForFilter(paramValue)}"
            }

            FilterType.Contains,
            FilterType.StartsWith,
            FilterType.EndsWith -> {
                result = "${filterType.getOdataDataSourceValue()}(${evaluateParamNameAsString(paramName)},'$paramValue')"
            }

            // filterParams[0] and filterParams[1] and ... and filterParams[n].
            FilterType.And -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getOdataDataSourceValue()} ")
                    { x -> x.getOdataDataSourceValue() }.toString()
            }

            // (filterParams[0]) or (filterParams[1]) or ... or (filterParams[n]).
            FilterType.Or -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getOdataDataSourceValue()} ")
                    { x -> "(${x.getOdataDataSourceValue()})" }.toString()
            }

            FilterType.Not -> {
                result = "${filterType.getOdataDataSourceValue()} ${filterParams?.get(0)
                    ?.getOdataDataSourceValue()}"
            }
        }

        return result
    }

    /**
     * Получить строковое значение для [OrderType].
     *
     * @return Строковое значение.
     */
    private fun OrderType.getOdataDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

    /**
     * Получить строковое значение для [FilterType].
     *
     * @return Строковое значение.
     */
    private fun FilterType.getOdataDataSourceValue(): String {
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

    /**
     * Преобразовать имя параметра к строке для OData.
     *
     * @param paramName Имя параметра.
     * @return Строковое значение.
     */
    private fun evaluateParamNameAsString(paramName: String?): String {
        if (paramName == null) return "null"

        return paramName.replace('.', '/')
    }

    /**
     * Преобразовать значение параметра к строке для OData.
     *
     * @param paramValue Значение параметра.
     * @return Строковое значение.
     */
    private fun evaluateParamValueForFilter(paramValue: Any?): String {
        if (paramValue == null) return "null"

        // Строка.
        if (paramValue is String) return "'$paramValue'"

        // Дата.
        if (paramValue is Date) {
            val simpleDateFormat = SimpleDateFormat(odataDateTimeFormat, Locale.PRC)
            return simpleDateFormat.format(paramValue)
        }

        // Enum.
        if (paramValue::class.isSubclassOf(Enum::class)) {
            val typeName = paramValue::class.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(typeName)

            if (odataTypeInfo != null) {
                return "${odataTypeInfo.enumFilterTypeName}'$paramValue'"
            }
        }

        return "$paramValue"
    }
}
package com.flexberry.androidodataofflinesample.data.network.datasource

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

open class OdataDataSourceCommon {
    private class UrlParamNames {
        companion object {
            const val select: String = "\$select"
            const val filter: String = "\$filter"
            const val order: String = "\$orderby"
            const val top: String = "\$top"
            const val skip: String = "\$skip"
            const val expand: String = "\$expand"
        }
    }

    private class OdataExclusionStrategy(val odataTypeInfo: OdataDataSourceTypeInfo<*>):
        ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            val fieldName = f?.name
            val fieldType = f?.declaredClass?.simpleName
            val fieldOdataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(fieldType)

            // Если есть детейл, или поле является мастером, то должны его пропустить.
            return odataTypeInfo.hasDetail(fieldName)
                    || (fieldOdataTypeInfo != null && !fieldOdataTypeInfo.isEnum)
        }

        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }

    }

    private val odataUrl = "http://stands-backend.flexberry.net/odata"
    private val primaryKeyPropertyName = "__PrimaryKey"
    private val odataDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun createObjects(vararg dataObjects: Any): Int {
        return createObjects(dataObjects.asList())
    }

    fun createObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            val odataObjectSimpleName = dataObject::class.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            val odataObjectName = odataTypeInfo.fullOdataTypeName

            var jsonObject = getGson(odataTypeInfo).toJson(dataObject)

            jsonObject = addMasters(dataObject, jsonObject)

            val url = URL("$odataUrl/$odataObjectName")
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
                    val detailPropertyValue = (detailProperty as KProperty1<Any, List<*>?>).get(dataObject)
                    val detailList = detailPropertyValue?.filterNotNull()

                    if (detailList?.any() == true) {
                        createObjects(detailList)
                    }
                }
        }

        return objectsCount
    }

    fun readObjects(kotlinClass: KClass<*>, querySettings: QuerySettings? = null): List<Any> {
        val odataObjectSimpleName = kotlinClass.simpleName
        val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
        val odataObjectName = odataTypeInfo.fullOdataTypeName
        var queryParamsValue = ""
        val queryParams = mutableListOf<String>()
        val expandValue = getRequestExtension(kotlinClass)

        if (querySettings != null) {
            queryParams.add(querySettings.getOdataDataSourceValue())
        }

        if (expandValue != null) {
            queryParams.add(expandValue)
        }

        if (queryParams.any()) {
            queryParamsValue = "?${queryParams.joinToString("&")}"
        }

        val url = URL("$odataUrl/$odataObjectName$queryParamsValue")
        val resultList = mutableListOf<Any>()
        val connection  = url.openConnection() as HttpURLConnection

        if(connection.responseCode == 200)
        {
            val inputSystem = connection.inputStream
            val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
            val jsonString = inputStreamReader.readText()
            val jsonValue = JSONObject(jsonString)
            val objectsArray = jsonValue.getJSONArray("value")

            for (i in 0 until objectsArray.length()) {
                val objectJson = objectsArray[i].toString()

                try {
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

    fun updateObjects(vararg dataObjects: Any): Int {
        return updateObjects(dataObjects.asList())
    }

    fun updateObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            val odataObjectSimpleName = dataObject::class.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            val odataObjectName = odataTypeInfo.fullOdataTypeName
            val primaryKeyProperty = dataObject::class.members.first {it.name == primaryKeyPropertyName } as KProperty1<Any, UUID>
            var jsonObject = getGson(odataTypeInfo).toJson(dataObject)

            jsonObject = addMasters(dataObject, jsonObject)

            val pkValue = primaryKeyProperty.get(dataObject)
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
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

    fun deleteObjects(vararg dataObjects: Any): Int {
        return deleteObjects(dataObjects.asList())
    }

    fun deleteObjects(listObjects: List<Any>): Int {
        var objectsCount = 0

        listObjects.forEach { dataObject ->
            val odataObjectSimpleName = dataObject::class.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(odataObjectSimpleName)!!
            val odataObjectName = odataTypeInfo.fullOdataTypeName
            val primaryKeyProperty = dataObject::class.members.first {it.name == primaryKeyPropertyName } as KProperty1<Any, UUID>

            val pkValue = primaryKeyProperty.get(dataObject)
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
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

    private fun addMasters(dataObject: Any, jsonObject: String): String {
        val jsonValue = JSONObject(jsonObject)
        val odataObjectClass = dataObject::class

        // Мастера должны идти как ИмяМастера@odata.bind: ИмяОДатаТипа(Ключ)
        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type
            val propTypeName = propType?.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(propTypeName)

            if (odataTypeInfo != null && !odataTypeInfo.isEnum) {
                val masterObject = (prop as KProperty1<Any, *>).get(dataObject)

                if (masterObject != null && propType != null) {
                    val pkProperty = propType.kotlin.declaredMemberProperties
                        .firstOrNull { x -> x.name == primaryKeyPropertyName }

                    if (pkProperty != null) {
                        val pkValue = (pkProperty as KProperty1<Any, UUID>).get(masterObject)

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

    /// TODO: тут в будущем появится параметр в виде предатавления объекта.
    private fun getRequestExtension(odataObjectClass: KClass<*>): String? {
        val resultList = mutableListOf<String>()

        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type?.simpleName
            val odataTypeInfo = OdataDataSourceTypeManager.getInfoByTypeName(propType)

            if (odataTypeInfo != null && !odataTypeInfo.isEnum) {
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

    private fun getGson(typeInfo: OdataDataSourceTypeInfo<*>): Gson {
        return GsonBuilder()
            .setDateFormat(odataDateTimeFormat)
            .addSerializationExclusionStrategy(OdataExclusionStrategy(typeInfo))
            .create()
    }

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

            FilterType.And -> {
                result = filterParams
                    ?.joinToString(" ${filterType.getOdataDataSourceValue()} ")
                    { x -> x.getOdataDataSourceValue() }.toString()
            }

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

    private fun OrderType.getOdataDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

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

    private fun evaluateParamNameAsString(paramName: String?): String {
        if (paramName == null) return "null"

        return paramName.replace('.', '/')
    }

    private fun evaluateParamValueForFilter(paramValue: Any?): String {
        if (paramValue == null) return "null"
        if (paramValue is String) return "'$paramValue'"

        if (paramValue is Date) {
            val simpleDateFormat = SimpleDateFormat(odataDateTimeFormat, Locale.PRC)
            return simpleDateFormat.format(paramValue)
        }

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
package com.flexberry.androidodataofflinesample.data.network.datasource

import android.util.Log
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.google.gson.Gson
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

open class OdataDataSource<T : Any>(private val odataObjectClass: KClass<T>)
{
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

    private val odataUrl = "http://stands-backend.flexberry.net/odata"
    private val primaryKeyPropertyName = "__PrimaryKey"
    private val primaryKeyProperty = odataObjectClass.members.first {it.name == primaryKeyPropertyName } as KProperty1<T, UUID>
    private val odataObjectName = OdataDataSourceTypeManager.getOdataTypeName(odataObjectClass.simpleName)!!

    fun createObject(obj: T): Int {
        return createObjects(listOf(obj))
    }

    fun createObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            var jsonObject = Gson().toJson(obj)

            jsonObject = convertMasters(jsonObject)

            val url = URL("$odataUrl/$odataObjectName")
            val connection = url.openConnection() as HttpURLConnection
            connection.doOutput = true

            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Content-Length", jsonObject.length.toString())

            DataOutputStream(connection.outputStream).use { it.writeBytes(jsonObject) }

            if (connection.responseCode == 201) {
                cnt++;
            }
            else {
                Log.e("ERROR", "Failed to create object. Failed Connection.")
                Log.d("OBJECT", obj.toString())
            }

            connection.disconnect()
        }

        return cnt;
    }

    fun readObjects(querySettings: QuerySettings? = null): List<T>
    {
        var queryParamsValue = ""
        val queryParams = mutableListOf<String>()
        val expandValue = getRequestExtension()

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
        val lstResult = mutableListOf<T>()
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
                    val objectValue = Gson().fromJson(objectJson, odataObjectClass.java)

                    lstResult.add(objectValue)
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

        return lstResult;
    }

    fun updateObject(obj: T): Int {
        return updateObjects(listOf(obj))
    }

    fun updateObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            var jsonObject = Gson().toJson(obj)

            jsonObject = convertMasters(jsonObject)

            val pkValue = primaryKeyProperty.get(obj)
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.requestMethod = "PATCH";
            connection.doOutput = true

            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Content-Length", jsonObject.length.toString())

            DataOutputStream(connection.outputStream).use { it.writeBytes(jsonObject) }

            if (connection.responseCode == 204) {
                cnt++
            }
            else {
                Log.e("ERROR", "Filed to update object $pkValue. Failed Connection.")
                Log.d("OBJECT", obj.toString())
            }

            connection.disconnect()
        }

        return cnt
    }

    fun deleteObject(obj: T): Int {
        return deleteObjects(listOf(obj))
    }

    fun deleteObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            val pkValue = primaryKeyProperty.get(obj)
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
            connection.requestMethod = "DELETE";

            if (connection.responseCode == 204) {
                cnt++
            } else {
                Log.e("ERROR", "Filed to delete object $pkValue. Failed Connection.")
                Log.d("OBJECT", obj.toString())
            }

            connection.disconnect()
        }

        return cnt
    }

    private fun convertMasters(jsonObject: String): String {
        val jsonValue = JSONObject(jsonObject)
        val jsonNewValue = JSONObject(jsonObject)

        // Мастера должны идти как ИмяМастера@odata.bind: ИмяОДатаТипа(Ключ)
        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type?.simpleName
            val odataTypeName = OdataDataSourceTypeManager.getOdataTypeName(propType)

            if (odataTypeName != null) {
                val node = jsonValue[propName] as JSONObject
                val pkValue = node.get(primaryKeyPropertyName)

                jsonNewValue.remove(propName)
                jsonNewValue.put("$propName@odata.bind", "$odataTypeName($pkValue)")
            }
        }

        // TODO: Костыль!!!
        if (odataObjectClass.simpleName == "NetworkVote") {
            jsonNewValue.put("Suggestion@odata.bind", "EmberFlexberryDummySuggestions(0b76edce-5900-45ec-a958-b9d6bb943b2d)")
        }

        return jsonNewValue.toString()
    }

    /// TODO: тут в будущем появится параметр в виде предатавления объекта.
    private fun getRequestExtension(): String? {
        val lstRes = mutableListOf<String>()

        odataObjectClass.declaredMemberProperties.forEach { prop ->
            val propName = prop.name
            val propType = prop.javaField?.type?.simpleName
            val odataTypeName = OdataDataSourceTypeManager.getOdataTypeName(propType)

            if (odataTypeName != null) {
                val elem = "$propName(${UrlParamNames.select}=$primaryKeyPropertyName)"

                lstRes.add(elem)
            }
        }

        // TODO: Костыль!!!
        if (odataObjectClass.simpleName == "NetworkVote") {
            val elem = "Suggestion(${UrlParamNames.select}=$primaryKeyPropertyName)"

            lstRes.add(elem)
        }

        return if (lstRes.any()) {
            "${UrlParamNames.expand}=${lstRes.joinToString(",")}"
        } else {
            null
        }
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
                result = "$paramName ${filterType.getOdataDataSourceValue()} ${getParamValueAsString(paramValue)}"
            }

            FilterType.Contains,
            FilterType.StartsWith,
            FilterType.EndsWith -> {
                result = "${filterType.getOdataDataSourceValue()}($paramName,'$paramValue')"
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

    private fun getParamValueAsString(paramValue: Any?): String {
        if (paramValue == null) return "null"
        if (paramValue is UUID) return "$paramValue"

        return "'$paramValue'"
    }
}
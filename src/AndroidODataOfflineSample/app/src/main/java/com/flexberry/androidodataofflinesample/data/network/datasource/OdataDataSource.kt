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

open class OdataDataSource<T : Any>(
    private val odataObjectName: String,
    private val odataObjectClass: KClass<T>)
{
    private val odataUrl = "http://stands-backend.flexberry.net/odata"
    private val primaryKeyPropertyName = "__PrimaryKey"
    private val primaryKeyProperty = odataObjectClass.members.first {it.name == primaryKeyPropertyName } as KProperty1<T, UUID>

    fun createObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            val jsonObject = Gson().toJson(obj)
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
        var querySettingsValue = ""

        if (querySettings != null) {
            querySettingsValue = "?${querySettings.getOdataDataSourceValue()}"
        }

        val url = URL("$odataUrl/$odataObjectName$querySettingsValue")
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

    fun updateObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            val jsonObject = Gson().toJson(obj)
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

    private fun QuerySettings.getOdataDataSourceValue(): String {
        val selectUrlParamName = "\$select"
        val filterUrlParamName = "\$filter"
        val orderUrlParamName = "\$orderby"
        val topUrlParamName = "\$top"
        val skipUrlParamName = "\$skip"
        val elements: MutableList<String> = mutableListOf()

        if (this.selectList != null) {
            val selectValue = this.selectList!!.joinToString(",")
            elements.add("$selectUrlParamName=$selectValue")
        }

        if (this.filterValue != null) {
            elements.add("$filterUrlParamName=${this.filterValue!!.getOdataDataSourceValue()}")
        }

        if (this.orderList != null) {
            val orderValue = this.orderList!!
                .joinToString(",") { x -> "${x.first} ${x.second.getOdataDataSourceValue()}"}
            elements.add("$orderUrlParamName=$orderValue")
        }

        if (this.topValue != null) {
            elements.add("$topUrlParamName=${this.topValue}")
        }

        if (this.skipValue != null) {
            elements.add("$skipUrlParamName=${this.skipValue}")
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
                result = "$paramName ${filterType.getOdataDataSourceValue()} '$paramValue'"
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
}
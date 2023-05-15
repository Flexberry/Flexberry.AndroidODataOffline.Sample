package com.flexberry.androidodataofflinesample.data.network.datasource

import android.util.Log
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
                Log.e("ERROR", "Failed Connection")
            }

            connection.disconnect()
        }

        return cnt;
    }

    fun readObjects(): List<T>
    {
        val lstResult = mutableListOf<T>()
        val url = URL("$odataUrl/$odataObjectName")
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
            Log.e("ERROR", "Failed Connection")
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
                Log.e("ERROR", "Failed Connection")
            }

            connection.disconnect()
        }

        return cnt
    }

    fun deleteObjects(listObjects: List<T>): Int {
        var cnt = 0

        listObjects.forEach { obj ->
            val jsonObject = Gson().toJson(obj)
            val pkValue = primaryKeyProperty.get(obj)
            val url = URL("$odataUrl/$odataObjectName($pkValue)")
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
            connection.requestMethod = "DELETE";

            if (connection.responseCode == 204) {
                cnt++
            } else {
                Log.e("ERROR", "Failed Connection")
            }

            connection.disconnect()
        }

        return cnt
    }
}
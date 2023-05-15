package com.flexberry.androidodataofflinesample.utils

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataService() {
    init {

    }

    val odataUrlValue = "http://stands-backend.flexberry.net/odata"

    // todo: проверить возможно ли добавить параметр в атрибуты объекта...
    inline fun <reified T> fetchData(odataName: String): List<T>
    {
        var lstResult = mutableListOf<T>()
        val url = URL("$odataUrlValue/$odataName")
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
                    // todo: сделать через настройки атрибутов объекта...
                    .replace("__PrimaryKey", "primaryKey")

                try {
                    val objectValue = Gson().fromJson(objectJson, T::class.java)

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
}
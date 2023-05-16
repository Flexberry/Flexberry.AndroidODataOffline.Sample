package com.flexberry.androidodataofflinesample

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flexberry.androidodataofflinesample.ui.mainmodel.MainScreen
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidODataOfflineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        // Настройки безопасности для выполнения http запросов. Разрешить все.
        // TODO Возможно это нужно перенести в AndroidOdataOfflineSampleApplication : Application()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

fun OdataTest(){
    val client = ODataClientFactory.getClient();
    client.getConfiguration().setDefaultPubFormat(ContentType.APPLICATION_JSON);

    // TODO как-то надо подождать выполнение запроса, т.к падает ошибка в response.body.
    // Если выполнять эту команду в интерактивной оболочке, то раза с третьего она проходит и данные пользователей получаются.
    try {
        val request = client.retrieveRequestFactory.getEntitySetRequest(client.newURIBuilder("http://stands-backend.flexberry.net/odata").appendEntitySetSegment("EmberFlexberryDummyApplicationUsers").build());
        val response = request.execute();
        val entitySet = response.body;
    } catch (e: Exception) {
        Log.e("ERROR", "Error in odata request", e)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidODataOfflineSampleTheme {
        MainScreen()
    }
}
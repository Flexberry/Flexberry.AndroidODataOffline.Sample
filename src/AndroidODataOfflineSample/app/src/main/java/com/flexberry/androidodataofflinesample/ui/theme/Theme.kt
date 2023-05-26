package com.flexberry.androidodataofflinesample.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
        primary = LightGrey,
        onPrimary = Black,
        secondary = PurpleGrey80,
        tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
        primary = LightGrey,
        onPrimary = Black,
        secondary = PurpleGrey40,
        tertiary = Pink40,

        /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AndroidODataOfflineSampleTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        // Установлено значение по умолчанию «false», чтобы отключить динамический цвет в приложении на Android 12+
        // Будут использоваться цвета темы приложения на всех версиях Android
        dynamicColor: Boolean = false,
        content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}

@Composable
fun listFormBottomMenu(
    modifier: Modifier = Modifier,
    onAddItemButtonClicked: (() -> Unit)? = null,
    onBackButtonClicked: (() -> Unit)? = null
) {
    val tileSize = with(LocalDensity.current) {80.dp.toPx()}
    Box(modifier = modifier.fillMaxSize()){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 20.dp, bottom = 0.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(
                                red = MaterialTheme.colorScheme.background.red,
                                green = MaterialTheme.colorScheme.background.green,
                                blue = MaterialTheme.colorScheme.background.blue,
                                alpha = 0f
                            ),
                            MaterialTheme.colorScheme.background
                        ),
                        endY = tileSize
                    )
                )
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            if (onAddItemButtonClicked != null) {
                Button(
                    modifier = modifier.size(80.dp),
                    shape = RoundedCornerShape(10),
                    onClick = onAddItemButtonClicked
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 48.sp
                    )
                }
            }


        }

        Box(
            modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 20.dp, bottom = 0.dp, start = 20.dp)
            .align(Alignment.BottomCenter),
            contentAlignment = Alignment.CenterStart
        ) {
            if (onBackButtonClicked != null) {
                Button(
                    modifier = modifier.size(80.dp),
                    shape = RoundedCornerShape(50),
                    onClick = onBackButtonClicked
                ) {
                    Icon(
                        modifier = modifier.size(40.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "На главную",
                    )
                }
            }
        }
    }
}

@Composable
fun editFormTopMenu(
    modifier: Modifier = Modifier,
    onCloseButtonClicked: (() -> Unit)? = null,
    onSaveCloseButtonClicked: (() -> Unit)? = null,
    onSaveButtonClicked: (() -> Unit)? = null
) {
    Row(modifier = modifier) {
        Row(modifier = modifier.weight(1f)) {
            editFormTopMenuButton(text = "Close", onButtonClicked = onCloseButtonClicked)
        }

        Spacer(modifier = modifier.size(16.dp))

        Row(
            modifier = modifier.weight(2f),
            horizontalArrangement = Arrangement.End
        ) {
            editFormTopMenuButton(text = "Save & Close", onButtonClicked = onSaveCloseButtonClicked)

            Spacer(modifier = modifier.size(8.dp))

            editFormTopMenuButton(text = "Save", onButtonClicked = onSaveButtonClicked)
        }
    }
}

@Composable
fun editFormTopMenuButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: (() -> Unit)? = null
) {
    val buttonSize = 90.dp
    if (onButtonClicked != null) {
        Button(
            modifier = modifier
                .size(buttonSize),
            onClick = onButtonClicked
        ) {
            Text(
                text = text,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}


// Для предпросмотра в Android Studio
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode",
    showBackground = true,
)
@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Composable
fun ListItemsPreview() {
    AndroidODataOfflineSampleTheme {
        listFormBottomMenu()
    }
}
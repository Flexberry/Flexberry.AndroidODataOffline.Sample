# Внедрение зависимостей с помощью Hilt

В качестве DI в Android проектах рекомендуется использовать Hilt. Hilt является раширением контейнера Dagger2. Работает с проектом Jetpack (набор библиотек и инструментов, созданный командой Google для упрощения разработки под Android)

## Установка необходимых компонентов

1) build.gradle(app)

```kotlin
plugins {
    id 'com.google.dagger.hilt.android'
    id 'kotlin-kapt'
}

dependencies {

    // необходимо для внедрения view-моделей в Activity, компоненты и compose.
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"

    // Hilt DI.
    implementation 'com.google.dagger:hilt-android:2.44'
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}

```

2) build.gradle(Project)

```kotlin
plugins {
    id 'com.google.dagger.hilt.android' version '2.44' apply false
}
```

3) Добавить application name в манифест (должно совпадать с именем класса Application())

```
<application
        android:name="AndroidOdataOfflineSampleApplication"
```


## Создание класса Application

Для Hilt необходимо создать класс, наследуемый от Application.

```kotlin
package com.flexberry.androidodataofflinesample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidOdataOfflineSampleApplication : Application()
```

## Внедрение ViewModel в Activity и Compose

1) MainViewModel

```kotlin
package com.flexberry.androidodataofflinesample.ui.mainmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    data class MainViewModelState (val isOnline: Boolean)

    fun appUserButton():Unit {
        // Функционал для кнопки "ApplicationUser"
    }
}
```

2) Инжектим в MainActivity. В Activity для этого дабавляются аннотации @AndroidEntryPoint

```kotlin
package com.flexberry.androidodataofflinesample

import androidx.activity.viewModels

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Внедрение viewModel через hilt.
    private val mainViewModel: MainViewModel by viewModels()
```


3) Инжектим в Compose класс mainScreen, который отрисовывает главное меню

```kotlin
package com.flexberry.androidodataofflinesample.ui.mainmodel

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen( modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel() ) {
```

## Внедрение Repository в ViewModel

1) Repository. Добавить @Inject constructor()

```kotlin
package com.flexberry.androidodataofflinesample.data

import javax.inject.Inject

class AppDataRepository @Inject constructor()
{
....
}
```

2) Инжектим в ViewModel. Добавить аннотацию @HiltViewModel, добавить репозиторий в конструктор

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppDataRepository
) : ViewModel() {
....
}
```

Ссылка на документацию
https://developer.android.com/training/dependency-injection

## Внедрение DataSource в Repository.

Некоторый DataSource реализует интерфейс NetworkDataSource и имеет несколько вариаций. В разных репозиториях нужны разные реализации этого DataSource, поэтому внедрение будет происходить с использованием именованных регистраций.

1) Интрерфейс (имеет дженерик тип T, это просто особенность примера)

```kotlin
package com.flexberry.androidodataofflinesample.data.network.interfaces

import com.flexberry.androidodataofflinesample.data.query.QuerySettings

interface NetworkDataSource<T> {
    fun createObjects(vararg dataObjects: T): Int
    fun createObjects(listObjects: List<T>): Int
    fun readObjects(querySettings: QuerySettings? = null): List<T>
    fun updateObjects(vararg dataObjects: T): Int
    fun updateObjects(listObjects: List<T>): Int
    fun deleteObjects(vararg dataObjects: T): Int
    fun deleteObjects(listObjects: List<T>): Int
}
```

2) Реализация интерфейса в базовом классе DataSource (от которого наследуются его вариации)

```kotlin
open class OdataDataSource<T : Any> (private val odataObjectClass: KClass<T>) : NetworkDataSource<T>
{
    override fun createObjects(vararg dataObjects: T): Int {
        ...
    }

    override fun createObjects(listObjects: List<T>): Int {
        ...
    }

    override fun readObjects(querySettings: QuerySettings?): List<T> {
        ...
    }

    override fun updateObjects(vararg dataObjects: T): Int {
        ...
    }

    override fun updateObjects(listObjects: List<T>): Int {
        ...
    }

    override fun deleteObjects(vararg dataObjects: T): Int {
        ...
    }

    override fun deleteObjects(listObjects: List<T>): Int {
        ...
    }

}
```

3) Регистрация

Для регистраций экземпляров, реализующих интрефейс, создают класс с аннотацией @Module.

```kotlin
package com.flexberry.androidodataofflinesample.data.di

import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationUserNetworkDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoteNetworkDatasource

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @ApplicationUserNetworkDataSource
    @Provides
    fun provideApplicationUserNetworkDataSource(): NetworkDataSource<NetworkApplicationUser> {
        return ApplicationUserOdataDataSource()
    }

    @VoteNetworkDatasource
    @Provides
    fun provideVoteNetworkDataSource(): NetworkDataSource<NetworkVote> {
        return VoteOdataDataSource()
    }
}
```

В данном примере выполняются именованные регистрации разных реализаций интерфейса NetworkDataSource<T>. Имена задаются с помощью переменных annotation.


4) Инжект в репозиторий

```kotlin
class ApplicationUserRepository @Inject constructor(
    @ApplicationUserNetworkDataSource private val networkDataSource: NetworkDataSource<NetworkApplicationUser>
) {

...

}
```
# Источник данных OData

OData как источник данных используется во многих проектах. 

Для более универсальной работы с OData в Android были созданны классы:
- [OdataDataSourceCommon](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/odata/OdataDataSourceCommon.kt): общий источник данных OData;
- [OdataDataSource](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/odata/OdataDataSource.kt): типизированный источник данных OData;
- [OdataDataSourceTypeInfo](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/odata/OdataDataSourceTypeInfo.kt): информация о типе, который может использовать OdataDataSource;
- [OdataDataSourceTypeManager](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/odata/OdataDataSourceTypeManager.kt): менеджер типов для всех OdataDataSource;

## OdataDataSourceCommon

Общий источник данных для OData.

Предоставляет общие методы для работы с объектами.

```kotlin
open class OdataDataSourceCommon {
    /**
     * Создать объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(vararg dataObjects: Any): Int {
        ...
    }

    /**
     * Создать объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(listObjects: List<Any>): Int {
        ...
    }

    /**
     * Вычитать объекты.
     *
     * @param querySettings Параметры ограничения.
     * @param kotlinClass Класс объекта.
     * @return Список объектов.
     */
    fun readObjects(kotlinClass: KClass<*>, querySettings: QuerySettings? = null): List<Any> {
        ...
    }

    /**
     * Вычитать объекты.
     *
     * @param T Тип объекта.
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    inline fun <reified T: Any> readObjects(querySettings: QuerySettings? = null) : List<T> {
        ...
    }

    /**
     * Обновить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(vararg dataObjects: Any): Int {
        ...
    }

    /**
     * Обновить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(listObjects: List<Any>): Int {
        ...
    }

    /**
     * Удалить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(vararg dataObjects: Any): Int {
        ...
    }

    /**
     * Удалить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(listObjects: List<Any>): Int {
        ...
    }
}
```

Примеры вызова соответсвенно:
```kotlin
val ds = OdataDataSourceCommon()
val createCount = ds.createObjects(obj1, obj2)
val createCount = ds.createObjects(listOf(obj1, obj2))
val dataObjects = ds.readObjects(NetworkApplicationUser::class) as List<NetworkApplicationUser>
val dataObjects = ds.readObjects<NetworkApplicationUser>()
val updateCount = ds.updateObjects(obj1, obj2)
val updateCount = ds.updateObjects(listOf(obj1, obj2))
val deleteCount = ds.deleteObjects(obj1, obj2)
val deleteCount = ds.deleteObjects(listOf(obj1, obj2))
```

## OdataDataSource

Типизированный источник данных OData. 
Реализует интерфейс [**NetworkDataSource\<T\>**](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/interfaces/NetworkDataSource.kt)

Для его создания необходимо явно указать с каким классом он будет работать.

```kotlin
open class OdataDataSource<T : Any> (private val odataObjectClass: KClass<T>) : NetworkDataSource<T>
{
    ...
}
```

Пример OdataDataSource для *ApplicationUser*. Его сетевая модель называется *NetworkApplicationUser*:
```kotlin
class ApplicationUserOdataDataSource: OdataDataSource<NetworkApplicationUser>(NetworkApplicationUser::class)
{

}
```

Содержит в себе 4 основных метода CRUD и их вариации.

## Создание объектов **createObjects**

Формирует запрос на добавление объектов в OData.

```kotlin
override fun createObjects(vararg dataObjects: T): Int {
    ...
}

override fun createObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество созданных объектов.

## Чтение объектов **readObjects**

Формирует запрос на вычитку объектов из OData. Получает данные и формирует из них объекты проекта.

Принимает параметром [QuerySettings](%D0%9D%D0%B0%D1%81%D1%82%D1%80%D0%BE%D0%B9%D0%BA%D0%B8%20%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%BE%D0%B2%20QuerySettings.md) - настройки ограничений для вычитки данных. Внутри метода они преобразуются в нужные URL параметры и добавляются к строке URL запроса.

```kotlin
override fun readObjects(querySettings: QuerySettings? = null): List<T> {
    ...
}
```

Возвращает список созданных объектов.

Пример использования ограничения querySettings:
```kotlin
val ds = VoteOdataDataSource()
val querySettings = QuerySettings()
    .filter(
        Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
        Filter.notEqualFilter("VoteType", VoteType.Dislike)
    )
    .top(10)

val objs = ds.readObjects(querySettings)

if (objs.any()) {
    ...
}
```

## Обновление объектов **updateObjects**

Формирует запрос на обновление объектов в OData.

```kotlin
override fun updateObjects(vararg dataObjects: T): Int {
    ...
}

override fun updateObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество обновленных объектов.

## Удаление объектов **deleteObjects**

Формирует запрос на удаление объектов в OData.

```kotlin
override fun deleteObjects(vararg dataObjects: T): Int {
    return deleteObjects(dataObjects.asList())
}

override fun deleteObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество удаленных объектов.

## OdataDataSourceTypeInfo
Информация о типе, который может использовать OdataDataSource.

- **kotlinClass**: класс языка kotlin, например *NetworkApplicationUser::class*;
- **namespace**: пространоство имен этого типа в OData, например *EmberFlexberryDummy*;
- **odataTypeName**: имя типа данных в OData, например *ApplicationUser*
- **details**: список детейловых свойств объекта;
- **isEnum**: является ли тип перечислимым;
- **typeName**: имя типа в проекте, например *NetworkApplicationUser*;
- **fullOdataTypeName**: вычислимое свойство, имя типа для обращения к OData, например *EmberFlexberryDummyApplicationUser*;
- **enumFilterTypeName**: вычислимое свойство, имя типа для обращения к Enum свойству, например *EmberFlexberryDummy.VoteType*.

С помощью метода **hasDetail** можно узнать содержит ли класс указанный детейл.

```kotlin
class OdataDataSourceTypeInfo<T : Any>(
    val kotlinClass: KClass<T>,
    val namespace: String,
    val odataTypeName: String,
    val isEnum: Boolean = false,
    val details: List<String>? = null
) {
    val typeName = kotlinClass.simpleName!!
    val fullOdataTypeName = "$namespace$odataTypeName"
    val enumFilterTypeName = "$namespace.$odataTypeName"

    fun hasDetail(detailName: String?): Boolean {
        return details?.contains(detailName) ?: false
    }
}
```

## OdataDataSourceTypeManager

Менеджер типов для всех OdataDataSource.

Содержит коллекцию [**OdataDataSourceTypeInfo**](#odatadatasourcetypeinfo). Данные задаются при инициализации.

Предоставляет методы:
- **getInfoByTypeName**: получить информацию о типе по его имени в проекте;
- **getInfoByOdataTypeName**: получить информацию о типе по его имени в OData;

Пример:
```kotlin
class OdataDataSourceTypeManager {
    companion object {
        private val odataTypeMap  = listOf(
            OdataDataSourceTypeInfo(
                kotlinClass = NetworkApplicationUser::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "ApplicationUsers",
                details = listOf("Votes")),
            OdataDataSourceTypeInfo(
                kotlinClass = NetworkVote::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "Votes"
            ),
            OdataDataSourceTypeInfo(
                kotlinClass = VoteType::class,
                namespace = "EmberFlexberryDummy",
                odataTypeName = "VoteType",
                isEnum = true)
        )

        fun getInfoByTypeName(typeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.typeName == typeName }
        }

        fun getInfoByOdataTypeName(odataTypeName: String?): OdataDataSourceTypeInfo<*>? {
            return odataTypeMap.firstOrNull { x -> x.odataTypeName == odataTypeName
                    || x.fullOdataTypeName == odataTypeName }
        }
    }
}
```
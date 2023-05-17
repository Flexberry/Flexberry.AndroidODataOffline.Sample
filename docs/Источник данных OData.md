# Источник данных OData

OData как источник данных используется во многих проектах. 

Для более универсальной работы с OData в Android были созданны классы:
- [OdataDataSource](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/OdataDataSource.kt): типизированный источник данных OData;
- [OdataDataSourceTypeInfo](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/OdataDataSourceTypeInfo.kt): информация о типе, который может использовать OdataDataSource;
- [OdataDataSourceTypeManager](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/network/datasource/OdataDataSourceTypeManager.kt): менеджер типов для всех OdataDataSource;

## OdataDataSource

Типизированный источник данных OData

Для его создания необходимо явно указать с каким классом он будет работать.

```kotlin
open class OdataDataSource<T : Any>(private val odataObjectClass: KClass<T>)
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
fun createObjects(vararg dataObjects: T): Int {
    return createObjects(dataObjects.asList())
}

fun createObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество созданных объектов.

## Чтение объектов **readObjects**

Формирует запрос на вычитку объектов из OData. Получает данные и формирует из них объекты проекта.

Принимает параметром [QuerySettings](%D0%9D%D0%B0%D1%81%D1%82%D1%80%D0%BE%D0%B9%D0%BA%D0%B8%20%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%BE%D0%B2%20QuerySettings.md) - настройки ограничений для вычитки данных. Внутри метода они преобразуются в нужные URL параметры и добавляются к строке URL запроса.

```kotlin
fun readObjects(querySettings: QuerySettings? = null): List<T> {
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
fun updateObjects(vararg dataObjects: T): Int {
    return updateObjects(dataObjects.asList())
}

fun updateObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество обновленных объектов.

## Удаление объектов **deleteObjects**

Формирует запрос на удаление объектов в OData.

```kotlin
fun deleteObjects(vararg dataObjects: T): Int {
    return deleteObjects(dataObjects.asList())
}

fun deleteObjects(listObjects: List<T>): Int {
    ...
}
```

Возвращает количество удаленных объектов.

## OdataDataSourceTypeInfo
Информация о типе, который может использовать OdataDataSource.

- **typeName**: имя типа в проекте, например *NetworkApplicationUser*;
- **namespace**: пространоство имен этого типа в OData, например *EmberFlexberryDummy*;
- **odataTypeName**: имя типа данных в OData, например *ApplicationUser*
- **isEnum**: является ли тип перечислимым;
- **fullOdataTypeName**: вычислимое свойство, имя типа для обращения к OData, например *EmberFlexberryDummyApplicationUser*;
- **enumFilterTypeName**: вычислимое свойство, имя типа для обращения к Enum свойству, например *EmberFlexberryDummy.VoteType*.

```kotlin
class OdataDataSourceTypeInfo(
    val typeName: String,
    val namespace: String,
    val odataTypeName: String,
    val isEnum: Boolean = false
) {
    val fullOdataTypeName = "$namespace$odataTypeName"
    val enumFilterTypeName = "$namespace.$odataTypeName"
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
        private val odataTypeMap: List<OdataDataSourceTypeInfo> = listOf(
            OdataDataSourceTypeInfo(
                NetworkApplicationUser::class.simpleName!!,
                "EmberFlexberryDummy",
                "ApplicationUsers"),
            OdataDataSourceTypeInfo(
                NetworkVote::class.simpleName!!,
                "EmberFlexberryDummy",
                "Votes"),
            OdataDataSourceTypeInfo(
                VoteType::class.simpleName!!,
                "EmberFlexberryDummy",
                "VoteType",
                true)
        )

        fun getInfoByTypeName(typeName: String?): OdataDataSourceTypeInfo? {
            return odataTypeMap.firstOrNull { x -> x.typeName == typeName }
        }

        fun getInfoByOdataTypeName(odataTypeName: String?): OdataDataSourceTypeInfo? {
            return odataTypeMap.firstOrNull { x -> x.odataTypeName == odataTypeName
                    || x.fullOdataTypeName == odataTypeName }
        }
    }
}
```
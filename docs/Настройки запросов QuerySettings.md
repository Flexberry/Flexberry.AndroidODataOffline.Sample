# Настройки запросов QuerySettings

Для задания условий ограничения запросов к источникам данных были созданы классы для хранения этих условий.
Классы содержаться в пространстве имен **com.flexberry.androidodataofflinesample.data.query**.

- [Filter](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/query/Filter.kt): настройки фильтрации;
- [FilterType](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/query/FilterType.kt): enum типов фильтров;
- [OrderType](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/query/OrderType.kt): enum направлений сортировки;
- [QuerySettings](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/blob/develop/src/AndroidODataOfflineSample/app/src/main/java/com/flexberry/androidodataofflinesample/data/query/QuerySettings.kt): класс настроек. Содержит в себе все настройки ограничения.

## Filter, настройки фильтрации

Содержит свойства:
- **filterType**: тип фильтра;
- **filterParams**: вложенные фильтры. Применимо только к типам *And*, *Or* и *Not*. Например если тип будет *And*, то фильтр будет означать *filterParams[0] And filterParams[1] And ... And filterParams[n]*;
- **paramName**: имя параметра ограничения. Уровни иерархии разделаются точкой, например *"Author.Name"* ограничение по имени автора;
- **paramValue**: значение параметра ограничения.

```kotlin
class Filter(
    val filterType: FilterType,
    val filterParams: List<Filter>? = null,
    val paramName: String? = null,
    val paramValue: String? = null) {
    ...
}
```

Примеры использования:
```kotlin
Filter.containsFilter("Name", "Test")
Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey)
Filter.notEqualFilter("VoteType", VoteType.Dislike)
```

## FilterType, типы фильтрации

Доступные типы фильтрации:
```kotlin
enum class FilterType() {
    Equal,
    NotEqual,
    Greater,
    GreaterOrEqual,
    Less,
    LessOrEqual,
    Has,
    Contains,
    StartsWith,
    EndsWith,
    And,
    Or,
    Not
}
```

## OrderType, направление сортировки

Доступные направления сортировки:
```kotlin
enum class OrderType() {
    Asc,
    Desc,
}
```

## QuerySettings, настройки ограничения

Содержит свойства:
- **filterValue**: установленный фильтр;
- **orderList**: список сортировки, пары **ИмяСвойства+Направление**;
- **selectList**: список свойств для выбора;
- **topValue**: сколько вернуть записей из итогового результата;
- **skipValue**: сколько пропустить записей в итоговом результате.

Например если нашлось 10 записей, а у нас указано
```kotlin
QuerySettings().skip(3).top(5)
```

то в результате должны получить записи с 4 по 8 включительно.

```kotlin
class QuerySettings(
    var filterValue: Filter? = null,
    var orderList: MutableList<Pair<String, OrderType>>? = null,
    var selectList: MutableList<String>? = null,
    var topValue: Int? = null,
    var skipValue: Int? = null) {
    ...
}
```

## Примеры использования

Имя = NameForTest, упорядочить по имени, вернуть первые 5 записей
```kotlin
val querySettings = QuerySettings()
    .filter(Filter.equalFilter("Name", "NameForTest"))
    .orderBy("Name")
    .top(5)
```

Автор = objUser, VoteType != Dislike, вернуть первые 10 записей
```kotlin
val querySettings = QuerySettings()
    .filter(
        Filter.equalFilter("Author.__PrimaryKey", objUser.__PrimaryKey),
        Filter.notEqualFilter("VoteType", VoteType.Dislike)
    )
    .top(10)
```

(Karma > 50 ИЛИ Name содержит 'Test') И (Activated = true ИЛИ EMail заканчивается на 'gmail.com')
```kotlin
val querySettings = QuerySettings()
    .filter(
        Filter.orFilter(
            Filter.greaterFilter("Karma", 50),
            Filter.containsFilter("Name", "Test")
        ),
        Filter.orFilter(
            Filter.equalFilter("Activated", true),
            Filter.endsWithFilter("EMail", "gmail.com")
        )
    )
```
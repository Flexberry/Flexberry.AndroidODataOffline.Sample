# Использование библиотеки Room

**Room** - это библиотека, которая является уровнем абстракции над SQLite и упрощает организацию хранения данных. Служит для работы с локальной (offline) БД в приложении.

## 1. Зависимости

*build.gradle(app)*

```kotlin
dependencies {
    def room_version = "2.5.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Kotlin Extensions and Coroutines support for Room
    testImplementation("androidx.room:room-testing:$room_version") // Test helpers
}

```

## 2. Сущности
*@Entity* - это аннотация, которую необходимо указывать для класса данных, описывающего конкретную таблицу в БД.

```kotlin
@Entity(tableName = MasterEntity.tableName)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo(name = "primaryKey")
    val primarykey: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String? = null,
) {
    @Ignore
    var details: List<DetailEntity>? = null

    companion object {
        const val tableName = "Master"
    }
}
```
- *@PrimaryKey* - аннотация для объявления первичного ключа сущности
- *@ColumnInfo* - аннотация для настроек конкретного столбца сущности
- *@Ignore* - аннотация позволяет подсказать Room, что это поле не должно записываться в базу или читаться из нее.

## 3. Объекты доступа к данным
*@Dao* - аннотация для объявления интерфейса (или абстрактного класса), который будет производить манипуляции с данными.

В данном приложении используется интерфейс, содержащий описание четырех основных CRUD-операций:

```kotlin
interface BaseDao<T> {
    @Insert
    open fun insertObjects(appData: List<T>): List<Long>

    @RawQuery
    open fun getObjects(query: SimpleSQLiteQuery): List<T>

    @Update
    open fun updateObjects(appData: List<T>): Int

    @Delete
    open fun deleteObjects(appData: List<T>): Int
}
```

- *@Insert* - аннотация, которая позволяет выполнить вставку в таблицу базы данных.
- *@RawQuery* - аннотация, которая позволяет выполнить чтение из таблицы базы данных в случае, если строка запроса генерируется во время выполнения (как в данном приложении). *Если запрос известен заренее, то лучше отдавать предпочтение использованию аннотации **@Query***
- *@Update* - аннотация, которая позволяет выполнить обновление некоторых строк в таблице базы данных.
- *@Delete* - аннотация, которая позволяет выполнить удаление некоторых строк в таблице базы данных.

От этого интерфейса наследуются абстрактные классы для конкретных сущностей:
```kotlin
@Dao
abstract class MasterDao: BaseDao<MasterEntity> {

}
```

## 4. База данных
*@Database* - аннотация, которой помечается основной класс по работе с базой данных. Этот класс должен быть абстрактным и наследовать **RoomDatabase**:

```kotlin
@Database(
    entities = [
        MasterEntity::class,
        DetailEntity::class
    ],
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getMasterDao(): MasterDao
    abstract fun getDetailDao(): DetailDao
}
```
- *entities* - используемые Entity. Для каждого Entity класса из указанного списка будет создана таблица.
- *version* - версия базы данных (нужна, например, при миграции между разными версиями БД)

## 5. Конвертация типов
Иногда Entity-объекты могут содержать поля, которые не являются примитивами, и не могут быть сохранены в БД.

Например, SQLite не имеет каких-либо функций, которые помогают напрямую обработать тип Timestamp. Поэтому при попытке сохранить объект с полем данного типа возникает ошибка. Создание класса, содержащего методы конвертации [Timestamp -> Long] и [Long -> Timestamp] помогают решить данную проблему:

```kotlin
class Converters {
    @TypeConverter
    fun fromLongToTimestamp(long: Long?): Timestamp? {
        return long?.let { Timestamp(it) }
    }

    @TypeConverter
    fun fromTimestampToLong(date: Timestamp?): Long? {
        return date?.time
    }
}
```

- *@TypeConverter* - аннотация для методов конвертации между двумя типами.

Данный класс с методами конвертации указывается для класса, которому необходима обработка неподдерживаемых SQLite-ом типов. Глобальное решение - прописать аннотацию для класса БД. В этом случае Room сможет использовать его во всех Entity и Dao.

```kotlin
@Database(
    entities = [
        MasterEntity::class,
        DetailEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getMasterDao(): MasterDao
    abstract fun getDetailDao(): DetailDao
}
```

- *@TypeConverters* - аннотация для указания классов-конвертеров.

## 6. Внедрение базы данных Room через Hilt

В данном случае класс базы данных назван *LocalDatabase*, а следующая цепочка классов в совокупности отвечает за взаимодействие с БД:

1) Интерфейс с описанием общих методов CRUD-операций
```kotlin
interface LocalDataSource<T> {
    fun createObjects(vararg dataObjects: T): Int
    fun createObjects(listObjects: List<T>): Int
    fun readObjects(querySettings: QuerySettings? = null): List<T>
    fun updateObjects(vararg dataObjects: T): Int
    fun updateObjects(listObjects: List<T>): Int
    fun deleteObjects(vararg dataObjects: T): Int
    fun deleteObjects(listObjects: List<T>): Int
}
```

2) Менеджер сущностей для Room. В конструктор передается LocalDatabase, с помощью которого будет получен конкретный dao для каждой сущности (в данный класс каждая новая сущность добавляется вручную в текущем проекте)
```kotlin
class RoomDataBaseManager @Inject constructor(
    db: LocalDatabase
) {
    // ...
    // dao = db.getMasterDao()
    // ...
}
```

3) Общий источник данных Room. В конструктор передается RoomDataBaseManager из п.2.
```kotlin
open class RoomDataSourceCommon @Inject constructor(
    private val dataBaseManager: RoomDataBaseManager
) {
    // Реализованы метод createObjects, readObjects, updateObjects, deleteObjects
}
```

4) Источник данных Room. Наследуется от интерфейса из п.1; Создается экземпляр класса RoomDataSourceCommon из п.3; Здесь T - конкретнный Entity-класс.
```kotlin
open class RoomDataSource<T: Any> @Inject constructor(
    private val entityObjectClass: KClass<T>,
    dataBaseManager: RoomDataBaseManager
) : LocalDataSource<T> {
    // Экземпляр RoomDataSourceCommon, в котором находится конечная реализация базовых методов
    private val roomDataSourceCommon = RoomDataSourceCommon(dataBaseManager)
    
    // Override для методов из интерфейса из п.1. Вызов конечной реализации из roomDataSourceCommon
    override fun createObjects(vararg dataObjects: T): Int {
        return this.createObjects(dataObjects.asList())
    }

    // ...
    // Аналогично для readObjects, updateObjects, deleteObjects
}
```
5) DataSource класс для конкретной сущности. Наследуется от RoomDataSource из п.4
```kotlin
class MasterRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<MasterEntity>(MasterEntity::class, dataBaseManager) {

}
```

## 7. Пример CRUD-операций
Рассмотрим создание, вычитку, обновление и удаление для *MasterEntity*
1) Пусть у нас есть локальный DataSource из предыдущего пункта:
```kotlin
val ds = RoomDataSourceCommon(dataBaseManager)
```
2) Прежде всего необходимо создать объект:
```kotlin
val master = MasterEntity(
    name = "MasterName"
)
```
3) При создании записи в БД, метод возвращает количество созданных строк:
```kotlin
val countMastersCreated = ds.createObjects(master)
```
4) Изменим имя у данного объекта и обновим. При обновлении также возвращается целое число обновленных строк:
```kotlin
master.name = "Mister X"
val countMastersUpdated = ds.updateObjects(master)
```
5) При вычитке объектов возвращается список нужного нам типа (в данном случае *MasterEntity*). Данный пример вернет все объекты из таблицы *Master*:
```kotlin
val masterEntityList = ds.readObjects()
```
6) При удалении записей также возвращается число удаленных строк:
```kotlin
val countMastersDeleted = ds.deleteObjects(master)
```
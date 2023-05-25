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

```kotlin
@Dao
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
- *@RawQuery* - аннотация, которая позволяет выполнить чтение из таблицы базы данных в случае, если строка запроса генерируется во время выполнения (как в данном приложении):
```kotlin
val finalQuery = StringBuilder()
finalQuery.append("SELECT * FROM $tableName WHERE name LIKE '%name_%'")
val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());
return dao.getObjects(simpleSQLiteQuery)
```
- *@Update* - аннотация, которая позволяет выполнить обновление некоторых строк в таблице базы данных.
- *@Delete* - аннотация, которая позволяет выполнить удаление некоторых строк в таблице базы данных.
- *@Query* - аннотация, которая позволяет выполнить чтение из таблицы базы данных. Если запрос известен заренее, то лучше отдавать предпочтение использованию данной аннотации (а не **@RawQuery**). Здесь же можно использовать переданные параметры, подставив их после двоеточия в запросе:
```kotlin
@Query("SELECT * FROM BaseTable WHERE primarykey = :pk")
open fun getObject(pk: UUID): List<T>
```

В данном проекте от базового интерфейса наследуются абстрактные классы для конкретных сущностей. Аннотация **@Dao** в этом случае используется для наследников, и отсутствует у базового интерфейса.


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
-  В данном проекте через класс базы данных получаем DAO-объекты. При этом в приложении должен быть единственный экземпляр данного класса.

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

1) В данном приложении БД существует в единственном экземпляре. Именованная регистрация происходит в специальном классе **DataSourceModule.kt**:
```kotlin
@Provides
@Singleton
fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
    return Room.inMemoryDatabaseBuilder(appContext, LocalDatabase::class.java)
        .allowMainThreadQueries()
        .build()
}
```

2) Здесь же регистрируем вспомогательный класс RoomDataBaseManager, параметром которого является экземпляр класса базы данных:
```kotlin
@Provides
@Singleton
fun provideRoomDataBaseManager(localDatabase: LocalDatabase): RoomDataBaseManager {
    return RoomDataBaseManager(localDatabase)
}
```
- Конструкторы сразу нескольких классов получают его в качестве параметра, поэтому он упоминается здесь. Однако для простоты примеров экземпляр данного класса не будет использоваться.

3) В этом же классе определяем аннотацию для конкретного DataSource-а, и регистрируем его. В качестве параметра передается упомянутый выше RoomDataBaseManager:
```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MasterLocalDataSource

@MasterLocalDataSource
@Provides
fun provideMasterLocalDataSource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<MasterEntity> {
    return MasterRoomDataSource(roomDataBaseManager)
}
```

4) Затем по созданной аннотации для конкретного DataSource-а, мы подтягиваем его в репозиторий через @Inject-конструктор:
```kotlin
class MasterRepository @Inject constructor(
    @MasterLocalDataSource private val localDataSource: LocalDataSource<MasterEntity>
) {
    // Используя экземпляр локального DataSource-а, можно вызывать метды CRUD-операций
    fun readMastersOffline(dataObjects: List<Master>) {
        localDataSource.readObjects().map { it.asLocalModel() }
    }
}
```

## 7. Пример CRUD-операций
Рассмотрим получение объектов *MasterEntity*
1) В репозиторий из предыдущего пункта добавим вызов метода readObjects
```kotlin
class MasterRepository @Inject constructor(
    @MasterLocalDataSource private val localDataSource: LocalDataSource<MasterEntity>
) {
    // Используя экземпляр локального DataSource-а, можно вызывать метды CRUD-операций
    fun readMastersOffline(dataObjects: List<Master>) {
        localDataSource.readObjects().map { it.asLocalModel() }
    }
}
```

2) Описание методов CRUD-операций реализовано в интерфейсе, от которого наследуется класс репозитория:
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

3) Конкретный DataSource (MasterRoomDataSource) в нашем случае не содержит собственных методов, т.к. они определены в базовом классе RoomDataSource:
```kotlin
class MasterRoomDataSource @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : RoomDataSource<MasterEntity>(MasterEntity::class, dataBaseManager) {

}

open class RoomDataSource<T: Any> @Inject constructor(
    dataBaseManager: RoomDataBaseManager
) : LocalDataSource<T> {
    override fun readObjects(): List<T> {
        // Для построения строки запроса можно передавать вспомогательный объект. Убран для простоты примера
        val finalQuery = StringBuilder()
        finalQuery.append("SELECT * FROM $tableName")

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery.toString());

        return MasterDao.getObjects(simpleSQLiteQuery) 
    }
}
```
- В данном проекте для собственного удобства были созданы классы *RoomDataSourceCommon*, *RoomDataBaseManager*, *RoomDataBaseEntityInfo*, в которые из основного класса *RoomDataSource* вынесена логика получения информации об используемом конкретном типе Entity-объектов, получение соответствующих DAO-объектов и т.д. Для простоты, данные примеры приведены без использования этих классов.
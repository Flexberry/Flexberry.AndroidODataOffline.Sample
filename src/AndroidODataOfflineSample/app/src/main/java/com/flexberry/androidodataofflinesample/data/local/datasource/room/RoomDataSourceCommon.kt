package com.flexberry.androidodataofflinesample.data.local.datasource.room

import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.utils.Converters
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.FilterType
import com.flexberry.androidodataofflinesample.data.query.OrderType
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import com.flexberry.androidodataofflinesample.data.query.View
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf

/**
 * Общий источник данных Room.
 *
 * @param dataBaseManager Менеджер типов данных.
 */
open class RoomDataSourceCommon @Inject constructor(
    private val dataBaseManager: RoomDataBaseManager
) {
    // Имя свойства первичного ключа.
    private val primaryKeyPropertyName = "primarykey"
    // Конвертация данных.
    private val converters = Converters()
    // Список синонимов для имен таблиц (мастеров).
    private val tableAliases = mutableListOf<String>()

    /**
     * Создать объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(vararg dataObjects: Any): Int {
        return createObjects(dataObjects.asList())
    }

    /**
     * Создать объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество созданных объектов.
     */
    fun createObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            // Имя типа сущности.
            val entityObjectSimpleName = entityObject::class.simpleName
            // Информация о типе сущности.
            val entityObjectDataBaseInfo = dataBaseManager.getDataBaseInfoForTypeName(entityObjectSimpleName)!!

            if (isEntityExists(entityObject)) {
                countResult += updateObjects(entityObject)
            } else {
                countResult += entityObjectDataBaseInfo.insertObjectsToDataBase(listOf(entityObject))

                // Еще надо найти детейлы (атрибуты типа List<OdataType>).
                // И сохранить детейлы отдельно. Т.к. из json основного объекта они исключены.
                entityObject::class.declaredMemberProperties
                    .filter { x -> entityObjectDataBaseInfo.hasDetail(x.name) }
                    .forEach { detailProperty ->
                        // Детейловое свойство.
                        val detailPropertyValue =
                            (detailProperty as KProperty1<Any, List<*>?>).get(entityObject)
                        // Список детейловых объектов.
                        val detailList = detailPropertyValue?.filterNotNull()

                        if (detailList?.any() == true) {
                            // Создание детейлов.
                            createObjects(detailList)
                        }
                    }
            }
        }

        return countResult
    }

    /**
     * Вычитать объекты.
     *
     * @param kotlinClass Класс объекта.
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    fun readObjects(kotlinClass: KClass<*>, querySettings: QuerySettings?, view: View? = null): List<Any> {
        // Имя типа сущности.
        val entityObjectSimpleName = kotlinClass.simpleName
        // Информация о типе сущности.
        val entityObjectDataBaseInfo = dataBaseManager.getDataBaseInfoForTypeName(entityObjectSimpleName)!!
        // Имя таблицы.
        val tableName = entityObjectDataBaseInfo.tableName
        // Параметры запроса.
        val finalQuery = querySettings?.getRoomDataSourceValue(kotlinClass, entityObjectDataBaseInfo.tableName, view)
            ?: "SELECT * FROM $tableName"

        val simpleSQLiteQuery = SimpleSQLiteQuery(finalQuery)

        val resultList = entityObjectDataBaseInfo.getObjectsFromDataBase(simpleSQLiteQuery)

        // Смотрим детейлы представления.
        view?.detailViews?.forEach { (detailName, detailView) ->
            // Детейловое свойство.
            val detailProperty = kotlinClass.declaredMemberProperties
                .firstOrNull { it.name == detailName }
            // Информация про детейл.
            val detailInfo = entityObjectDataBaseInfo.getDetailInfo(detailName)

            // Если такое свойство вообще есть и оно известно...
            if (detailProperty != null && detailInfo != null) {

                // ... то для каждого объекта подгрузим данные детейла.
                resultList.forEach { currentEntity ->
                    // Ключ текущего объекта.
                    val currentPrimaryKey = getPrimaryKeyValue(currentEntity)

                    if (currentPrimaryKey != null) {
                        // Чтение объектов детейла.
                        val detailEntities = readObjects(
                            detailInfo.kotlinClass,
                            QuerySettings(Filter.equalFilter(detailInfo.relationProperty, currentPrimaryKey)),
                            detailView
                        )

                        // Устанавливанием значение детейла.
                        (detailProperty as KMutableProperty1<Any, List<*>?>).set(currentEntity, detailEntities)
                    }
                }
            }
        }

        // Смотрим мастера типа.
        entityObjectDataBaseInfo.masters?.forEach { masterInfo ->
            // Свойство связи из которого возьмем значение первичного ключа для мастера.
            val relationProperty = kotlinClass.declaredMemberProperties
                .firstOrNull { it.name == masterInfo.relationProperty } as KProperty1<Any, *>
            // Свойство в которое запишем мастеровую сущность.
            val entityProperty = kotlinClass.declaredMemberProperties
                .firstOrNull { it.name == masterInfo.entityProperty } as KMutableProperty1<Any, Any>

            // Для каждой сущности.
            resultList.forEach { currentEntity ->
                // Значение мастерового ключа.
                val relationPropertyValue = relationProperty.get(currentEntity)

                // Формируем представление мастера.
                val masterView = if (view == null) {
                    // Только первичный ключ.
                    View("", primaryKeyPropertyName)
                } else {
                    //Списку мастеровых свойств в текущем представлении.
                    val listProperties = view.propertiesTree
                        .firstOrNull { it.name == masterInfo.entityProperty }
                        ?.children?.listProperties

                    if (listProperties != null) {
                        // Формируем представление по списку мастеровых свойств в текущем представлении.
                        View(listProperties)
                    } else {
                        // Только первичный ключ.
                        View("", primaryKeyPropertyName)
                    }
                }

                // Если значение ключа мастера не пусто.
                if (relationPropertyValue != null) {
                    // Вычитываем мастера.
                    val masterEntities = readObjects(
                        masterInfo.kotlinClass,
                        QuerySettings(Filter.equalFilter(primaryKeyPropertyName, relationPropertyValue)),
                        masterView
                    )

                    // Если мастер вычитался.
                    if (masterEntities.size == 1) {
                        // Записываем значение мастера.
                        entityProperty.set(currentEntity, masterEntities[0])
                    }
                }
            }
        }

        return resultList
    }

    /**
     * Вычитать объекты.
     *
     * @param T Тип объекта.
     * @param querySettings Параметры ограничения.
     * @return Список объектов.
     */
    inline fun <reified T: Any> readObjects(querySettings: QuerySettings? = null, view: View? = null) : List<T> {
        return readObjects(T::class, querySettings, view) as List<T>
    }

    /**
     * Удалить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            // Имя типа сущности.
            val entityObjectSimpleName = entityObject::class.simpleName
            // Информация о типе сущности.
            val entityObjectDataBaseInfo = dataBaseManager.getDataBaseInfoForTypeName(entityObjectSimpleName)!!

            countResult += if (!isEntityExists(entityObject)) {
                1
            } else {
                entityObjectDataBaseInfo.deleteObjectsFromDataBase(listOf(entityObject))
            }
        }

        return countResult
    }

    /**
     * Удалить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество удаленных объектов.
     */
    fun deleteObjects(vararg dataObjects: Any): Int {
        return deleteObjects(dataObjects.asList())
    }

    /**
     * Обновить объекты.
     *
     * @param listObjects Список объектов данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(listObjects: List<Any>): Int {
        var countResult = 0

        listObjects.forEach { entityObject ->
            // Имя типа сущности.
            val entityObjectSimpleName = entityObject::class.simpleName
            // Информация о типе сущности.
            val entityObjectDataBaseInfo = dataBaseManager.getDataBaseInfoForTypeName(entityObjectSimpleName)!!

            if (!isEntityExists(entityObject)) {
                countResult += createObjects(entityObject)
            } else {
                countResult += entityObjectDataBaseInfo.updateObjectsInDataBase(listOf(entityObject))

                // Еще надо найти детейлы (атрибуты типа List<OdataType>).
                // И сохранить детейлы отдельно. Т.к. из json основного объекта они исключены.
                entityObject::class.declaredMemberProperties
                    .filter { x -> entityObjectDataBaseInfo.hasDetail(x.name) }
                    .forEach { detailProperty ->
                        // Детейловое свойство.
                        val detailPropertyValue =
                            (detailProperty as KProperty1<Any, List<*>?>).get(entityObject)
                        // Список детейловых объектов.
                        val detailList = detailPropertyValue?.filterNotNull()

                        if (detailList?.any() == true) {
                            // Создание детейлов.
                            updateObjects(detailList)
                        }
                    }
            }
        }

        return countResult
    }

    /**
     * Обновить объекты.
     *
     * @param dataObjects Объекты данных.
     * @return Количество обновленных объектов.
     */
    fun updateObjects(vararg dataObjects: Any): Int {
        return updateObjects(dataObjects.asList())
    }

    /**
     * Получить список строковых значений для [QuerySettings].
     *
     * @return Список строковых значений.
     */
    private fun QuerySettings.getRoomDataSourceValue(kotlinClass: KClass<*>, tableName: String, view: View? = null): String {
        // Берем список свойств из представления, иначе из настроек.
        val selectPropertiesList = (view
            ?.propertiesTree
            ?.filter { it.children == null }
            ?.map { it.name } ?: selectList) as MutableList<String>?

        // Добавим первичный ключ, если его нет.
        if (selectPropertiesList != null && !selectPropertiesList.contains(primaryKeyPropertyName)) {
            selectPropertiesList.add(primaryKeyPropertyName)
        }

        // Список возвращаемых полей.
        val selectValue = selectPropertiesList
            ?.joinToString(",") { "$tableName.$it" }
            ?.ifEmpty { null }
        // Ограничение на вычитку.
        val whereValue = filterValue?.getRoomDataSourceValue(kotlinClass)?.ifEmpty { null }
        // Сортировка.
        val orderValue = this.orderList?.joinToString(",")
            { x -> "${x.first} ${x.second.getRoomDataSourceValue()}"}
            ?.ifEmpty { null }
        // Количество возвращаемых строк результата.
        val limitValue = topValue?.toString()
        // Сколько пропустить записей в итоговом результате.
        val offsetValue = skipValue?.toString()

        // Присоединить таблицы
        var joinValue = getQueryJoins(kotlinClass, tableAliases)

        // Итоговый запрос.
        var resultQuery =
            """
                SELECT ${selectValue ?: "*"} 
                FROM $tableName 
                $joinValue
                ${if (whereValue != null) { "WHERE $whereValue" } else { "" }}
                ${if (orderValue != null) { "ORDER BY $orderValue" } else { "" }}
                ${if (limitValue != null) { "LIMIT $limitValue" } else { "" }}
                ${if (offsetValue != null) { "OFFSET $offsetValue" } else { "" }}
            """

        // Делаем его красивым.
        resultQuery = resultQuery.trimIndent()

        while (resultQuery.contains("\n\n")) resultQuery = resultQuery.replace("\n\n", "\n")

        return resultQuery
    }

    /**
     * Получить строковое значение для [Filter].
     *
     * @return Строковое значение.
     */
    private fun Filter.getRoomDataSourceValue(kotlinClass: KClass<*>): String {
        // Итоговый результат.
        var result = ""
        // Приведенное имя параметра.
        val paramNameValue = evaluateParamNameAsString(paramName)
        // Строковое значение ограничения.
        val filterTypeValue = filterType.getRoomDataSourceValue()
        // Приведенное значение параметра.
        val paramValueTransformed = evaluateParamValueForFilter(paramValue)

        when (this.filterType) {
            FilterType.Equal,
            FilterType.NotEqual,
            FilterType.Greater,
            FilterType.GreaterOrEqual,
            FilterType.Less,
            FilterType.LessOrEqual -> {
                result = "$paramNameValue $filterTypeValue $paramValueTransformed"
            }

            FilterType.Has,
            FilterType.Contains -> {
                result = "$paramNameValue $filterTypeValue '%$paramValue%'"
            }

            FilterType.StartsWith -> {
                result = "$paramNameValue $filterTypeValue '$paramValue%'"
            }
            FilterType.EndsWith -> {
                result = "$paramNameValue $filterTypeValue '%$paramValue'"
            }

            FilterType.And -> {
                result = filterParams
                    ?.joinToString(" $filterTypeValue ")
                    { x -> x.getRoomDataSourceValue(kotlinClass) }.toString()
            }

            FilterType.Or -> {
                result = filterParams
                    ?.joinToString(" $filterTypeValue ")
                    { x -> "(${x.getRoomDataSourceValue(kotlinClass)})" }.toString()
            }

            FilterType.Not -> {
                result = "$filterTypeValue ${filterParams?.get(0)
                    ?.getRoomDataSourceValue(kotlinClass)}"
            }
        }

        return result
    }

    /**
     * Получить строковое значение для [OrderType].
     *
     * @return Строковое значение.
     */
    private fun OrderType.getRoomDataSourceValue(): String {
        return when (this) {
            OrderType.Asc -> "asc"
            OrderType.Desc -> "desc"
        }
    }

    /**
     * Получить строковое значение для [FilterType].
     *
     * @return Строковое значение.
     */
    private fun FilterType.getRoomDataSourceValue(): String {
        return when (this) {
            FilterType.Equal -> "="
            FilterType.NotEqual -> "<>"
            FilterType.Greater -> ">"
            FilterType.GreaterOrEqual -> ">="
            FilterType.Less -> "<"
            FilterType.LessOrEqual -> "<="
            FilterType.Has -> "like"
            FilterType.Contains -> "like"
            FilterType.StartsWith -> "like"
            FilterType.EndsWith -> "like"
            FilterType.And -> "and"
            FilterType.Or -> "or"
            FilterType.Not -> "not"
        }
    }

    /**
     * Получить значение первичного ключа в объекте.
     *
     * @param entityObject Объект данных.
     * @return primaryKey: [UUID]
     */
    private fun getPrimaryKeyValue(entityObject: Any): UUID? {
        // Да, но лучше вытянуть первое поле с аннотацией @PrimaryKey
        val primaryKeyProperty =
            entityObject::class.members.firstOrNull { it.name == primaryKeyPropertyName } as KProperty1<Any, UUID>?

        return primaryKeyProperty?.get(entityObject)
    }

    /**
     * Преобразовать имя параметра к строке для Room.
     *
     * @param paramName Имя параметра.
     * @return Строковое значение.
     */
    private fun evaluateParamNameAsString(paramName: String?): String {
        if (paramName == null) return "null"
        
        val prefixIndex = paramName.indexOfLast { it == '.' }
        var returnValue = ""
        var fieldName = paramName

        // Если в имени параметра встречается точка, ограничение по мастеру.
        if (prefixIndex > 0) {
            // Префикс свойства, имя мастра или путь до него.
            val prefix = paramName.substring(0, prefixIndex)

            // Имя самого свойства.
            fieldName = paramName.substring(prefixIndex + 1)

            // Добавляем в синонимы.
            if (!tableAliases.contains(prefix)) tableAliases.add(prefix)

            // Индекс синонима.
            val aliasIndex = tableAliases.indexOf(prefix)

            // Все таблицы мстеров имеют синоним "table0","table1","table2" и т.д.
            returnValue = "table$aliasIndex."
        }

        returnValue += fieldName

        return returnValue
    }

    /**
     * Преобразовать значение параметра к строке для Room.
     *
     * @param paramValue Значение параметра.
     * @return Строковое значение.
     */
    private fun evaluateParamValueForFilter(paramValue: Any?): String {
        if (paramValue == null) return "null"

        if (paramValue is String) return "'$paramValue'"
        if (paramValue is Boolean) return if (paramValue) "1" else "0"
        if (paramValue is UUID) return "'${converters.fromUUIDtoString(paramValue)}'"
        if (paramValue is Timestamp) return converters.fromTimestampToLong(paramValue).toString()
        if (paramValue is Date) return converters.fromDateToLong(paramValue).toString()
        if (paramValue::class.isSubclassOf(Enum::class)) return "'$paramValue'"

        return "$paramValue"
    }

    /**
     * Сформировать JOIN для мастеров запроса.
     *
     * @param kotlinClass Тип сущности.
     * @param queryPathsList Используемые мастера или пути до мастера.
     * @param parentAlias Р
     * @return JOIN чать запроса к данным.
     */
    private fun getQueryJoins(
        kotlinClass: KClass<*>,
        queryPathsList: List<String>,
        parentAlias: String? = null,
        parentPrefix: String? = null): String {
        // Использованные префиксы имен мастеров.
        val usedPrefixes = mutableListOf<String>()
        // Текущая информация базы данных сущности.
        val thisInfo = dataBaseManager.getDataBaseInfoForTypeName(kotlinClass.simpleName)!!
        // Возвращаемое значение.
        var returnValue = ""

        queryPathsList.forEach { queryPath ->
            // Индекс точки в имени мастера, вложенный мастер.
            val indexOfDot = queryPath.indexOfFirst { it == '.' }
            // Имя мастера.
            var propertyName = queryPath

            // Если есть точка в имени, значит мастер вложен.
            if (indexOfDot > 0) {
                // Имя свойства мастера.
                propertyName = queryPath.substring(0, indexOfDot)

                // Нужно для добавления промежуточных значений.
                // Если вдруг есть сразу ограничение Мастер1.Мастер2.Мастер3....
                if (!tableAliases.contains(propertyName)) tableAliases.add(propertyName)

                // Если такой мастер уже попадался, то пропускаем.
                if (usedPrefixes.contains(propertyName)) return@forEach // continue

                // Добавляем в список использованных мастеров.
                usedPrefixes.add(propertyName)
            }

            // Информация о мастере сущности.
            val masterInfo = thisInfo.getMasterInfo(propertyName)

            if (masterInfo != null) {
                // Информация о базе данных мастера.
                val propertyInfo = dataBaseManager.getDataBaseInfoForTypeName(masterInfo.kotlinClass.simpleName)

                if (propertyInfo != null) {
                    // Текущий полный префикс/путь мастера.
                    val fullPrefix = if (parentPrefix != null) {
                        "$parentPrefix."
                    } else {
                        ""
                    } + propertyName
                    // Индекс синонима мастера.
                    val tableIndex = tableAliases.indexOf(fullPrefix)
                    // Синоним мастера.
                    val tableAlias = "table$tableIndex"
                    // Синоним родителя.
                    val parentAliasValue = parentAlias ?: thisInfo.tableName

                    // Соедиение таблиц.
                    returnValue += "LEFT JOIN ${propertyInfo.tableName} AS $tableAlias " +
                            "ON $parentAliasValue.${masterInfo.relationProperty} = $tableAlias.$primaryKeyPropertyName\n"

                    // Префикс текущего мастера с точкой.
                    val prefixToSearch = "$propertyName."
                    // Сформируем дочерние пути до мастеров.
                    val children = queryPathsList
                        .filter { it.startsWith(prefixToSearch) }
                        .map { it.substring(prefixToSearch.length)}

                    // Соединяем дочерние мастера.
                    if (children.any()) {
                        returnValue += getQueryJoins(masterInfo.kotlinClass, children, tableAlias, fullPrefix)
                    }
                }
            }
        }

        return returnValue
    }

    fun isEntityExists(entityObject: Any): Boolean {
        val primaryKeyValue = getPrimaryKeyValue(entityObject)!!
        val existedObject = readObjects(entityObject::class,
            QuerySettings(Filter.equalFilter(primaryKeyPropertyName, primaryKeyValue))
        )

        return existedObject.isNotEmpty()
    }
}
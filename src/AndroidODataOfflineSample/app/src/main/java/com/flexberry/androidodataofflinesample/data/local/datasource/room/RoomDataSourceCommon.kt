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
    private val primaryKeyPropertyName = "primarykey"
    private val converters = Converters()
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

            countResult += entityObjectDataBaseInfo.insertObjectsToDataBase(listOf(entityObject))

            // Еще надо найти детейлы (атрибуты типа List<OdataType>).
            // И сохранить детейлы отдельно. Т.к. из json основного объекта они исключены.
            entityObject::class.declaredMemberProperties
                .filter { x -> entityObjectDataBaseInfo.hasDetail(x.name) }
                .forEach { detailProperty ->
                    // Детейловое свойство.
                    val detailPropertyValue = (detailProperty as KProperty1<Any, List<*>?>).get(entityObject)
                    // Список детейловых объектов.
                    val detailList = detailPropertyValue?.filterNotNull()

                    if (detailList?.any() == true) {
                        // Создание детейлов.
                        createObjects(detailList)
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
            val relationProperty = kotlinClass.declaredMemberProperties
                .filter { it.name == masterInfo.relationProperty } as KProperty1<Any, *>
            val entityProperty = kotlinClass.declaredMemberProperties
                .filter { it.name == masterInfo.entityProperty } as KMutableProperty1<Any, Any>

            resultList.forEach { currentEntity ->
                val relationPropertyValue = relationProperty.get(currentEntity)
                val masterView = if (view == null) {
                    View("", primaryKeyPropertyName)
                } else {
                    View(view.propertiesTree.listProperties.filter { it.name == masterInfo.entityProperty })
                }

                if (relationPropertyValue != null) {
                    val masterEntities = readObjects(
                        masterInfo.kotlinClass,
                        QuerySettings(Filter.equalFilter(primaryKeyPropertyName, relationPropertyValue)),
                        masterView
                    )

                    if (masterEntities.size == 1) {
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

            countResult += entityObjectDataBaseInfo.deleteObjectsFromDataBase(listOf(entityObject))
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

            countResult += entityObjectDataBaseInfo.updateObjectsInDataBase(listOf(entityObject))
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
            ?.propertiesTree?.listProperties
            ?.filter { it.children == null }
            ?.map { it.name } ?: selectList) as MutableList<String>?

        if (selectPropertiesList != null && !selectPropertiesList.contains(primaryKeyPropertyName)) {
            selectPropertiesList.add(primaryKeyPropertyName)
        }

        val selectValue = selectPropertiesList?.joinToString(",")?.ifEmpty { null }
        val whereValue = filterValue?.getRoomDataSourceValue(kotlinClass)?.ifEmpty { null }
        val orderValue = this.orderList?.joinToString(",")
            { x -> "${x.first} ${x.second.getRoomDataSourceValue()}"}
            ?.ifEmpty { null }
        val limitValue = topValue?.toString()
        val offsetValue = skipValue?.toString()

        // Присоединить таблицы
        var joinValue = getQueryJoins(kotlinClass, tableAliases)

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
        var result = ""
        val paramNameValue = evaluateParamNameAsString(paramName)
        val filterTypeValue = filterType.getRoomDataSourceValue()
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

        if (prefixIndex > 0) {
            val prefix = paramName.substring(0, prefixIndex)
            
            fieldName = paramName.substring(prefixIndex + 1)

            if (!tableAliases.contains(prefix)) tableAliases.add(prefix)

            val aliasIndex = tableAliases.indexOf(prefix)

            returnValue = "table$aliasIndex."
        }

        // Получить имя из аннотации почему-то не предоставляется возможными.
        // Вот почему из аннтоации ColumnInfo удалено значение name.
        // Хочу чтобы в будущем это стало возможным...
        /** @sample
            val kProperty = kotlinClass.declaredMemberProperties.firstOrNull { it.name == fieldName }

            returnValue += if (kProperty != null) {
                val annotation =
                    kProperty.findAnnotation<ColumnInfo>() ?: kProperty.getter.findAnnotation()

                annotation?.name ?: fieldName
            } else {
                fieldName
            }
         */
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

    private fun getQueryJoins(kotlinClass: KClass<*>, queryPathsList: List<String>, parentAlias: String? = null): String {
        val usedPrefixes = mutableListOf<String>()
        val thisInfo = dataBaseManager.getDataBaseInfoForTypeName(kotlinClass.simpleName)!!
        var returnValue = ""

        queryPathsList.forEach { queryPath ->
            val indexOfDot = queryPath.indexOfFirst { it == '.' }
            var propertyName = queryPath

            if (indexOfDot > 0) {
                propertyName = queryPath.substring(0, indexOfDot)

                // Нужно для добавления промежуточных значений.
                // Если вдруг есть сразу ограничение Мастер1.Мастер2.Мастер3....
                if (!tableAliases.contains(propertyName)) tableAliases.add(propertyName)

                // Если такой мастер уже попадался, то пропускаем.
                if (usedPrefixes.contains(propertyName)) return@forEach // continue

                usedPrefixes.add(propertyName)
            }

            val masterInfo = thisInfo.getMasterInfo(propertyName)

            if (masterInfo != null) {
                val propertyInfo = dataBaseManager.getDataBaseInfoForTypeName(masterInfo.kotlinClass.simpleName)

                if (propertyInfo != null) {
                    val tableIndex = tableAliases.indexOf(propertyName)
                    val tableAlias = "table$tableIndex"
                    val parentAliasValue = parentAlias ?: thisInfo.tableName

                    returnValue += "LEFT JOIN ${propertyInfo.tableName} AS $tableAlias " +
                            "ON $parentAliasValue.${masterInfo.relationProperty} = $tableAlias.$primaryKeyPropertyName\n"

                    val prefixToSearch = "$propertyName."
                    val children = queryPathsList
                        .filter { it.startsWith(prefixToSearch) }
                        .map { it.substring(prefixToSearch.length)}

                    if (children.any()) {
                        returnValue += getQueryJoins(masterInfo.kotlinClass, children, tableAlias)
                    }
                }
            }
        }

        return returnValue
    }
}
package com.flexberry.androidodataofflinesample.data.network.datasource.odata

import kotlin.reflect.KClass

/**
 * Информация о типе, используемом в OData.
 *
 * @param T Тип объекта.
 * @param kotlinClass Класс объекта. Например NetworkApplicationUser::class.
 * @param namespace Пространоство имен в OData. Например EmberFlexberryDummy.
 * @param odataTypeName Имя типа данных в OData. Например ApplicationUser.
 * @param isEnum Является ли тип перечислимым.
 * @param details Список свойств типа, которые являются детейлами.
 * @property typeName Имя типа объекта. Например NetworkApplicationUser.
 * @property fullOdataTypeName Имя типа для обращения к OData. Например EmberFlexberryDummyApplicationUser.
 * @property enumFilterTypeName Имя типа для обращения к Enum свойству. Например EmberFlexberryDummy.VoteType.
 */
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

    /**
     * Содержит ли тип указанный детейл.
     *
     * @param detailName Имя детейла.
     * @return True если содержит, иначе False.
     */
    fun hasDetail(detailName: String?): Boolean {
        return details?.contains(detailName) ?: false
    }
}
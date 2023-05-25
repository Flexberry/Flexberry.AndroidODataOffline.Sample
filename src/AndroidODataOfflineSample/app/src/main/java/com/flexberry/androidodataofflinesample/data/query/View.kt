package com.flexberry.androidodataofflinesample.data.query

/**
 * Представление для сущности или объекта данных.
 */
class View(
    /**
     * Имя представления.
     */
    val name: String = "",

    /**
     * Строковое представление. Имена свойств через запятую.
     */
    val stringedView: String = "",

    /**
     * Детейловые представления.
     */
    val detailViews: MutableMap<String, View> = mutableMapOf()
    ) {

    /**
     * Создать представление по готовому списку свойств.
     */
    constructor(
        /**
         * СПисок свойств.
         */
        listView: List<PropertyTreeNode>
    ) : this() {
        this.propertiesTreeValue = PropertyTree(listView)
    }

    /**
     * Информация о свойстве в дереве свойств.
     */
    class PropertyTreeNode(val name: String, val children: PropertyTree? = null)

    /**
     * Дерево свойств.
     */
    class PropertyTree(val listProperties: List<PropertyTreeNode>)

    /**
     * Список свойств из дерева свойств.
     */
    val propertiesTree get() = propertiesTreeValue.listProperties

    /**
     * Дерево свойств.
     */
    private var propertiesTreeValue = getTree(stringedView
        .replace("\n", "")
        .replace("\r", "")
        .replace("\t", "")
        .replace(" ", "")
        .split(",")
        .filter { it.isNotEmpty() }
        .map { it.trim() })

    /**
     * Добавить описание детейла к представлению.
     *
     * @param detailName Имя детейла.
     * @param view Представление детейла.
     * @return Текущее представление.
     */
    fun addDetail(detailName: String, view: View): View {
        detailViews[detailName] = view

        return this
    }

    /**
     * Сформировать дерево свойств по строковому представлению.
     *
     * @param list Список строковых свойств.
     * @return Дерево свойств.
     */
    private fun getTree(list: List<String>): PropertyTree {
        // Текущий список сформированных свойств.
        val currentList = mutableListOf<PropertyTreeNode>()
        // Использованные префиксы свойств. Например "Мастер1", если свойство было "Мастер1.Имя".
        val usedPrefix = mutableListOf<String>()

        list.forEach { nodeName ->
            // Индекс точки в имени свойства.
            val splitIndex = nodeName.indexOf(".")

            if (splitIndex > 0) {
                // Префикс свойства, имя мастера. Например "Мастер1", если свойство "Мастер1.Имя".
                val prefix = nodeName.substring(0, splitIndex)

                // Если такого мастера мы еще не встречали.
                if (!usedPrefix.contains(prefix)) {
                    // СПисок дочерних свойств мастера.
                    val childProperties = list
                        .filter { it.startsWith("$prefix.") }
                        .map { it.substring(splitIndex + 1) }

                    // Формируем дерево мастера.
                    currentList.add(PropertyTreeNode(prefix, getTree(childProperties)))

                    // Исключаем имя мастера дальнейшей обработки.
                    usedPrefix.add(prefix)
                }
            } else {
                // Добавляем немастеровое свойство.
                currentList.add(PropertyTreeNode(nodeName))
            }
        }

        return PropertyTree(currentList)
    }
}
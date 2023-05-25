package com.flexberry.androidodataofflinesample.data.query

class View(
    val name: String = "",
    val stringedView: String = "",
    val detailViews: MutableMap<String, View> = mutableMapOf()
    ) {

    constructor(
        listView: List<PropertyTreeNode>
    ) : this() {
        this.propertiesTreeValue = PropertyTree(listView)
    }

    class PropertyTreeNode(val name: String, val children: PropertyTree? = null)
    class PropertyTree(val listProperties: List<PropertyTreeNode>)

    val propertiesTree get() = propertiesTreeValue.listProperties

    private var propertiesTreeValue = getTree(stringedView
        .replace("\n", "")
        .replace("\r", "")
        .replace("\t", "")
        .replace(" ", "")
        .split(",")
        .filter { it.isNotEmpty() }
        .map { it.trim() })

    fun addDetail(detailName: String, view: View): View {
        detailViews[detailName] = view

        return this
    }

    private fun getTree(list: List<String>): PropertyTree {
        val currentList = mutableListOf<PropertyTreeNode>()
        val usedPrefix = mutableListOf<String>()

        list.forEach { nodeName ->
            val splitIndex = nodeName.indexOf(".")

            if (splitIndex > 0) {
                val prefix = nodeName.substring(0, splitIndex)

                if (!usedPrefix.contains(prefix)) {
                    val childProperties = list
                        .filter { it.startsWith("$prefix.") }
                        .map { it.substring(splitIndex + 1) }

                    currentList.add(PropertyTreeNode(prefix, getTree(childProperties)))

                    usedPrefix.add(prefix)
                }
            } else {
                currentList.add(PropertyTreeNode(nodeName))
            }
        }

        return PropertyTree(currentList)
    }
}
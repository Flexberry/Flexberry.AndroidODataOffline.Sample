package com.flexberry.androidodataofflinesample.data.query

/**
 * Тип ограничения.
 */
enum class FilterType() {
    /**
     * Равно.
     */
    Equal,

    /**
     * Неравно.
     */
    NotEqual,

    /**
     * Больше.
     */
    Greater,

    /**
     * Больше либо равно.
     */
    GreaterOrEqual,

    /**
     * Меньше.
     */
    Less,

    /**
     * Меньше либо равно.
     */
    LessOrEqual,

    /**
     * Имеет.
     */
    Has,

    /**
     * Содержит.
     */
    Contains,

    /**
     * Начинается со значения.
     */
    StartsWith,

    /**
     * Оканчивается со значением.
     */
    EndsWith,

    /**
     * И.
     */
    And,

    /**
     * Или.
     */
    Or,

    /**
     * Не.
     */
    Not
}

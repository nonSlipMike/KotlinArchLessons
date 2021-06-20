package ru.mts.lessons.common

object UserTable {
    const val TABLE = "users"
    const val CREATE_SCRIPT: String =
        "create table $TABLE (${COLUMN.ID} integer primary key autoincrement,${COLUMN.NAME} text, ${COLUMN.EMAIL} text );"

    object COLUMN {
        const val ID = "_id"
        const val NAME = "name"
        const val EMAIL = "email"
    }
}
package com.github.maxos.legacyBookshelves.database.table

import org.jetbrains.exposed.v1.core.Table


const val TABLE_NAME = "shelves"

object ShelfTable: Table(TABLE_NAME) {
	val location = text("location").uniqueIndex()
	val items = text("items")

	override val primaryKey = PrimaryKey(location)

}
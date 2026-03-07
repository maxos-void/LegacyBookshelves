package com.github.maxos.legacyBookshelves.database

import com.github.maxos.legacyBookshelves.LegacyBookshelves
import com.github.maxos.legacyBookshelves.database.table.ShelfTable
import com.github.maxos.legacyBookshelves.utils.log.FastLog
import com.github.maxos.legacyBookshelves.utils.log.LogType

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

import java.io.File

class DataBaseConnector(
	private val plugin: LegacyBookshelves
) {
	companion object {
		private const val CHILD = "database"
		private const val PROTOCOL = "jdbc"
		private const val SUBPROTOCOL = "h2"
		private const val MODE = "file"
		private const val PARAM = "DB_CLOSE_DELAY=-1"
		private const val DRIVER = "org.h2.Driver"
		private const val DB_NAME = "shelves_database"
	}

	init {
		try {
			connect()
		} catch (e: Exception) {
			FastLog.sendLog(LogType.ERR, e.localizedMessage)
		}

	}


	private fun connect() {
		val databaseFile = File(plugin.dataFolder, CHILD)
		if (!databaseFile.exists()) {
			databaseFile.mkdirs()
		}

		val filePath = "${databaseFile.absolutePath}/$DB_NAME"

		val dbUrl = "$PROTOCOL:$SUBPROTOCOL:$MODE:$filePath;$PARAM"
		Database.connect(
			url = dbUrl,
			driver = DRIVER
		)

		transaction {
			SchemaUtils.create(ShelfTable)
		}
		FastLog.sendLog(LogType.INFO, "§aБаза данных подключена успешно!")
	}
}
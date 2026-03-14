package com.github.maxos.legacyBookshelves.database


import com.github.maxos.legacyBookshelves.database.table.ShelfTable
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData
import com.github.maxos.legacyBookshelves.utils.log.FastLog.sendLog
import com.github.maxos.legacyBookshelves.utils.log.LogType
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.system.measureTimeMillis


class DataBaseManager(
) {

	fun saveShelves(data: Set<ShelfData>) {
		try {
			transaction {
				val saveTime = measureTimeMillis {
					ShelfTable.deleteAll()

					ShelfTable.batchInsert(data) { shelf ->
						this[ShelfTable.location] = shelf.location
						this[ShelfTable.items] = shelf.items
					}
				}
				//!sendLog(LogType.INFO, "Сохранили данные в БД! [$saveTime ms]")
			}
		} catch (e: Exception) {
			sendLog(LogType.ERR, e.message)
		}
	}

	fun loadShelves(): Set<ShelfData> {
		try {
			val data = hashSetOf<ShelfData>()

			transaction {
				val loadTime = measureTimeMillis {
					data.addAll(ShelfTable.selectAll().map { row ->
						ShelfData(
							row[ShelfTable.location],
							row[ShelfTable.items],
						)
					})
				}

				if (data.isEmpty()) sendLog(
					LogType.INFO, "База данных пуста, загружать нечего :3"
				)
				else sendLog(
					LogType.INFO, "Загрузили ${data.size} полок из базы данных [$loadTime ms]"
				)
			}
			return data

		} catch (e: Exception) {
			sendLog(LogType.ERR, e.message)
			return emptySet()
		}
	}
}
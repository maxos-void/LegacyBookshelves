package com.github.maxos.legacyBookshelves.database

import com.github.maxos.legacyBookshelves.database.service.DataPreparationService
import com.github.maxos.legacyBookshelves.scheduler.Scheduler.runAsyncTask
import com.github.maxos.legacyBookshelves.scheduler.Scheduler.runSyncTask
import com.github.maxos.legacyBookshelves.shelf.ShelfManager
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData

class DataBaseLoader(
	private val manager: ShelfManager,
	private val dbManager: DataBaseManager
) {

	fun loadData() {
		runAsyncTask {
			val data = dbManager.loadShelves()

			runSyncTask {
				loadShelves(data)
			}

		}
	}

	fun loadShelves(data: Set<ShelfData>) {
		val shelves = DataPreparationService
			.unloadData(data)

		shelves.forEach { (loc, items) ->
			manager.loadShelfFromDb(loc, items)
		}
	}

}
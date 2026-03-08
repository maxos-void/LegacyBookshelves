package com.github.maxos.legacyBookshelves.database.service

import com.github.maxos.legacyBookshelves.shelf.data.ShelfConverter
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData
import com.github.maxos.legacyBookshelves.shelf.data.ShelfInventoryData
import com.github.maxos.legacyBookshelves.utils.serialization.ItemSerializer.fromItemBase64
import com.github.maxos.legacyBookshelves.utils.serialization.LocationSerializer
import kotlinx.serialization.json.Json
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object DataPreparationService {

	fun collectData(shelves: Map<Block, Inventory>): Set<ShelfData> {

		val data = shelves.map { (block, inv) ->
			ShelfConverter(
				block.location,
				inv
			).data
		}.toSet()

		return data
	}

	fun unloadData(shelves: Set<ShelfData>): Map<Location, Map<Int, ItemStack>> {
		val deserializeMap = mutableMapOf<Location, Map<Int, ItemStack>>()

		shelves.forEach { (loc, items) ->

			val loc = LocationSerializer.fromLocationText(loc) ?: return@forEach
			val shelfInventoryData = Json.decodeFromString<ShelfInventoryData>(items)
			val items = shelfInventoryData.slots.mapValues { (_, base64) ->
				fromItemBase64(base64)
			}
			deserializeMap[loc] = items
		}

		return deserializeMap
	}
}
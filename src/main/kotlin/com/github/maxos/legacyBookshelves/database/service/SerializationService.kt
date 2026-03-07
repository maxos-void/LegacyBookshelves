package com.github.maxos.legacyBookshelves.database.service

import com.github.maxos.legacyBookshelves.shelf.data.ShelfData
import com.github.maxos.legacyBookshelves.utils.serialization.Base64Utils.fromItemBase64
import com.github.maxos.legacyBookshelves.utils.serialization.LocationSerializer
import com.github.maxos.legacyBookshelves.utils.serialization.model.InventorySerialized
import kotlinx.serialization.json.Json
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object SerializationService {

	fun shelfSerialization(shelves: Map<Block, Inventory>): Map<String, String> {
		val serializeMap = mutableMapOf<String, String>()

		for ((block, inv) in shelves.entries) {
			val shelfData = ShelfData(block.location, inv)
			serializeMap[shelfData.locationToJson] = shelfData.inventoryToJson()
		}

		return serializeMap
	}

	fun shelfDeserialization(shelves: Map<String, String>): Map<Location, Map<Int, ItemStack>> {
		val deserializeMap = mutableMapOf<Location, Map<Int, ItemStack>>()

		for ((locationJson, itemsJson) in shelves.entries) {
			val loc = LocationSerializer.fromLocationText(locationJson) ?: continue
			val inventoryData = Json.decodeFromString<InventorySerialized>(itemsJson)
			val items = inventoryData.slots.mapValues { (_, base64) ->
				fromItemBase64(base64)
			}
			deserializeMap[loc] = items
		}
		return deserializeMap
	}
}
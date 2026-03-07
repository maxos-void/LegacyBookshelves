package com.github.maxos.legacyBookshelves.shelf.data

import com.github.maxos.legacyBookshelves.utils.serialization.Base64Utils.fromItemBase64
import com.github.maxos.legacyBookshelves.utils.serialization.Base64Utils.toItemBase64
import com.github.maxos.legacyBookshelves.utils.serialization.LocationSerializer
import com.github.maxos.legacyBookshelves.utils.serialization.model.InventorySerialized
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.Inventory

class ShelfData(
	val location: Location,
	private val inventory: Inventory
) {

	fun inventoryToJson(): String {
		val slots = mutableMapOf<Int, String>()

		for (i in 0 until inventory.size) {
			val item = inventory.getItem(i)
			if (item?.type?.isAir == false) {
				slots[i] = toItemBase64(item)
			}
		}

		val inventoryData = InventorySerialized(slots)
		return Json.encodeToString(inventoryData)
	}

	val locationToJson = LocationSerializer.toSerializeLocation(location)

	//companion object {
	//	fun fromJson(location: Location, json: String): ShelfData {
	//		val inventoryData = Json.decodeFromString<InventorySerialized>(json)

	//		val items = inventoryData.slots.mapValues { (_, base64) ->
	//			fromItemBase64(base64)
	//		}

	//		return ShelfData(location, items)
	//	}
	//}
}
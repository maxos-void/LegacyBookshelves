package com.github.maxos.legacyBookshelves.shelf.data

import com.github.maxos.legacyBookshelves.utils.serialization.ItemSerializer.toItemBase64
import com.github.maxos.legacyBookshelves.utils.serialization.LocationSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Location
import org.bukkit.inventory.Inventory

class ShelfConverter(
	private val location: Location,
	private val inventory: Inventory
) {

	val data = ShelfData(
		LocationSerializer.toSerializeLocation(location),
		serializeInventory()
	)

	private fun serializeInventory(): String {
		val slots = mutableMapOf<Int, String>()

		for (i in 0 until inventory.size) {
			val item = inventory.getItem(i)
			if (item?.type?.isAir == false) {
				slots[i] = toItemBase64(item)
			}
		}

		val shelfInventoryData = ShelfInventoryData(slots)
		return Json.encodeToString(shelfInventoryData)
	}
}
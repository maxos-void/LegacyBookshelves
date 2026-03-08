package com.github.maxos.legacyBookshelves.shelf.data

import kotlinx.serialization.Serializable

@Serializable
data class ShelfInventoryData(
	val slots: Map<Int, String>
)
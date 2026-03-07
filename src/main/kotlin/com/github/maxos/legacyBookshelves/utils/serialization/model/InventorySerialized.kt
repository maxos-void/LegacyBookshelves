package com.github.maxos.legacyBookshelves.utils.serialization.model

import kotlinx.serialization.Serializable

@Serializable
data class InventorySerialized(
	val slots: Map<Int, String>
)
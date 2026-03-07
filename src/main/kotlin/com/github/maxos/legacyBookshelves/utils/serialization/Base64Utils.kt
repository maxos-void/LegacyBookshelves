package com.github.maxos.legacyBookshelves.utils.serialization

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Base64

object Base64Utils {

	fun toItemBase64(item: ItemStack): String {
		ByteArrayOutputStream().use { outputStream ->
			BukkitObjectOutputStream(outputStream).use { dataOutput ->
				dataOutput.writeObject(item.serializeAsBytes())
			}
			return Base64.getEncoder().encodeToString(outputStream.toByteArray())
		}
	}

	fun fromItemBase64(base64: String): ItemStack {
		return try {
			val data = Base64.getDecoder().decode(base64)
			ByteArrayInputStream(data).use { inputStream ->
				BukkitObjectInputStream(inputStream).use { dataInput ->
					val bytes = dataInput.readObject() as ByteArray
					ItemStack.deserializeBytes(bytes)
				}
			}
		} catch (e: Exception) {
			throw IOException("Не удалось десереализовать премет" + e.localizedMessage)
		}
	}

}
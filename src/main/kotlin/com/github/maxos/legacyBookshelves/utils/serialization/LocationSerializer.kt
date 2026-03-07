package com.github.maxos.legacyBookshelves.utils.serialization

import com.github.maxos.legacyBookshelves.utils.log.FastLog
import com.github.maxos.legacyBookshelves.utils.log.LogType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

object LocationSerializer {

	fun toSerializeLocation(location: Location): String {
		val world = location.world
		val x = location.x
		val y = location.y
		val z = location.z
		val pitch = location.pitch
		val yaw = location.yaw
		return "${world.name};$x;$y;$z;$pitch;$yaw"
	}

	fun fromLocationText(stringLoc: String): Location? {
		stringLoc.split(';').also {
			val worldName = it[0]
			val world = getWorld(worldName) ?: return null
			val x = getDouble(it[1]) ?: return null
			val y = getDouble(it[2]) ?: return null
			val z = getDouble(it[3]) ?: return null
			val pitch = getFloat(it[4]) ?: return null
			val yaw = getFloat(it[5]) ?: return null

			return Location(world, x, y, z, yaw, pitch)
		}
	}

	private fun getWorld(worldName: String): World? {
		val world = Bukkit.getWorld(worldName)
		if (world == null) FastLog.sendLog(
			LogType.ERR,
			"Мир $worldName не обнаружен для расстановки слипперов!"
		)
		return world
	}

	private fun getDouble(num: String): Double? {
		val double = num.toDoubleOrNull()
		if (double == null) FastLog.sendLog(
			LogType.ERR,
			"Ошибка координат, труп пропущен"
		)
		return double
	}

	private fun getFloat(num: String): Float? {
		val float = num.toFloatOrNull()
		if (float == null) FastLog.sendLog(
			LogType.ERR,
			"Ошибка координат, труп пропущен"
		)
		return float
	}

}
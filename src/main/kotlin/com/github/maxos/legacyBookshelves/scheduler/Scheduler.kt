package com.github.maxos.legacyBookshelves.scheduler

import com.github.maxos.legacyBookshelves.LegacyBookshelves
import org.bukkit.Bukkit

object Scheduler {

	private var plugin: LegacyBookshelves? = null
	private val scheduler = Bukkit.getScheduler()

	fun initialization(plugin: LegacyBookshelves) {
		this.plugin = plugin
	}

	fun runSyncTask(operation: Runnable): Int {
		val taskId = scheduler.runTask(plugin ?: return -1,
			operation
		).taskId
		return taskId
	}

	fun runAsyncTask(operation: Runnable): Int {
		val taskId = scheduler.runTaskAsynchronously(plugin ?: return -1,
			operation
		).taskId
		return taskId
	}

	fun runAsyncTaskTimer(time: Int, operation: Runnable): Int {
		val timeToSecond = time * 20L
		val taskId = scheduler.runTaskTimerAsynchronously(plugin ?: return -1,
			operation, timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun runSyncTaskTimer(time: Int, operation: Runnable): Int {
		val timeToSecond = time * 20L
		val taskId = scheduler.runTaskTimer(plugin ?: return -1,
			operation, timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun stopTask(id: Int) {
		scheduler.cancelTask(id)
	}

	fun stopAllTasks() {
		scheduler.cancelTasks(plugin ?: return)
	}

}
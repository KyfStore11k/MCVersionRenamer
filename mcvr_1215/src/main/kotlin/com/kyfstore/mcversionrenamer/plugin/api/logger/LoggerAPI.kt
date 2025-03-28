package com.kyfstore.mcversionrenamer.libapi.core.plugin.api.logger

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.customlibs.async.logger.AsyncLogger

class LoggerAPI {
    private lateinit var logger: AsyncLogger

    var isInitialized: Boolean = false

    fun onEnable() {
        this.isInitialized = true
        logger = MCVersionRenamer.LOGGER;
    }

    fun getLogger(): AsyncLogger {
        return logger
    }
}
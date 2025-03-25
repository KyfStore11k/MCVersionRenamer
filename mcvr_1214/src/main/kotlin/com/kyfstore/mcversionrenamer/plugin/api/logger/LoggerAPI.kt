package com.kyfstore.mcversionrenamer.libapi.core.plugin.api.logger

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerAPI {
    private lateinit var logger: Logger

    var isInitialized: Boolean = false

    fun onEnable() {
        this.isInitialized = true
        logger = LoggerFactory.getLogger(MCVersionRenamer::class.java)
    }

    fun getLogger(): Logger {
        return logger
    }
}
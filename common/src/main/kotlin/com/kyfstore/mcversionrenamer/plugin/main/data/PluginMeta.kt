package com.kyfstore.mcversionrenamer.plugin.main.data

data class PluginMeta(
    val id: String,
    val mainClass: String,
    val dependsOn: List<String>
)

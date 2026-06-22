package com.kyfstore.mcversionrenamer.plugin.main.control

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.plugin.api.PluginInitializer
import com.kyfstore.mcversionrenamer.plugin.api.PluginObject
import com.kyfstore.mcversionrenamer.plugin.main.data.PluginMeta
import com.kyfstore.mcversionrenamer.plugin.main.data.PublicPluginRegistry
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

class PluginFileManager {

    companion object {
        private val discoveredPlugins =
            mutableMapOf<String, Pair<File, PluginMeta>>()

        private val loadedPlugins =
            mutableMapOf<String, PluginObject>()

        @JvmStatic
        fun checkPluginDirectoryExists() {
            if (!PublicPluginRegistry.pluginDirectory.exists()) {
                PublicPluginRegistry.pluginDirectory.mkdirs()
            }
        }

        @JvmStatic
        fun listAndSavePlugins() {
            discoveredPlugins.clear()
            loadedPlugins.clear()

            val pluginFiles =
                PublicPluginRegistry.pluginDirectory
                    .listFiles()
                    ?.filter { it.extension == "jar" }
                    ?: emptyList()

            for (file in pluginFiles) {
                val meta = readPluginMeta(file) ?: continue
                discoveredPlugins[meta.id] = file to meta
            }

            loadAllPlugins()
        }

        private fun readPluginMeta(jarFile: File): PluginMeta? {
            return try {
                JarFile(jarFile).use { jar ->
                    val manifest = jar.manifest ?: return null
                    val attrs = manifest.mainAttributes

                    val id = attrs.getValue("Plugin-Id")
                    val main = attrs.getValue("Plugin-Main")
                    val dependsRaw = attrs.getValue("Plugin-Depends")

                    if (id.isNullOrBlank() || main.isNullOrBlank()) {
                        MCVersionRenamer.LOGGER.error(
                            "Missing Plugin-Id or Plugin-Main in ${jarFile.name}"
                        )
                        return null
                    }

                    val depends = dependsRaw
                        ?.split(",")
                        ?.map { it.trim() }
                        ?.filter { it.isNotEmpty() }
                        ?: emptyList()

                    PluginMeta(
                        id = id,
                        mainClass = main,
                        dependsOn = depends
                    )
                }
            } catch (e: Exception) {
                MCVersionRenamer.LOGGER.error(
                    "Failed reading manifest from ${jarFile.name}: ${e.message}"
                )
                null
            }
        }

        private fun resolveLoadOrder(): List<String> {

            val graph = discoveredPlugins.mapValues { it.value.second.dependsOn }

            val visited = mutableSetOf<String>()
            val visiting = mutableSetOf<String>()
            val result = mutableListOf<String>()

            fun dfs(id: String) {
                if (id in visiting) {
                    throw IllegalStateException("Circular dependency detected at $id")
                }
                if (id in visited) return

                visiting += id

                graph[id]?.forEach { dep ->
                    if (discoveredPlugins.containsKey(dep)) {
                        dfs(dep)
                    } else {
                        MCVersionRenamer.LOGGER.warn(
                            "Missing dependency '$dep' required by '$id'"
                        )
                    }
                }

                visiting -= id
                visited += id
                result += id
            }

            graph.keys.forEach { dfs(it) }

            return result
        }

        private fun loadAllPlugins() {

            val order = resolveLoadOrder()

            for (id in order) {

                val (file, meta) = discoveredPlugins[id]!!

                val plugin = createPluginInstance(file, meta)

                if (plugin != null) {
                    loadedPlugins[id] = plugin
                    MCVersionRenamer.LOGGER.info("Loaded plugin: $id")
                }
            }
        }

        private fun createPluginInstance(
            jarFile: File,
            meta: PluginMeta
        ): PluginObject? {

            return try {

                val url = jarFile.toURI().toURL()

                val classLoader = URLClassLoader(
                    arrayOf(url),
                    this::class.java.classLoader
                )

                val clazz = classLoader.loadClass(meta.mainClass)

                if (!PluginInitializer::class.java.isAssignableFrom(clazz)) {
                    MCVersionRenamer.LOGGER.error(
                        "Class ${meta.mainClass} does not extend PluginInitializer"
                    )
                    return null
                }

                val instance =
                    clazz.getDeclaredConstructor().newInstance() as PluginInitializer

                instance.api = PluginBridge

                instance.onInitialize()

                instance.main?.let {
                    PluginObject(
                        id = meta.id,
                        main = it,
                        jarFilePath = jarFile.absolutePath
                    )
                }

            } catch (e: Exception) {
                MCVersionRenamer.LOGGER.error(
                    "Failed loading plugin ${jarFile.name}: ${e.message}"
                )
                null
            }
        }
    }
}
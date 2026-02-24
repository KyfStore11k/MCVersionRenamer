package com.kyfstore.mcversionrenamer.plugin.main.control

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.plugin.api.PluginInitializer
import com.kyfstore.mcversionrenamer.plugin.api.PluginObject
import com.kyfstore.mcversionrenamer.plugin.main.data.PublicPluginRegistry
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarFile

class PluginFileManager {

    companion object {
        @JvmStatic
        fun checkPluginDirectoryExists() {
            if (!PublicPluginRegistry.pluginDirectory.exists()) {
                PublicPluginRegistry.pluginDirectory.mkdirs()
            }
        }

        @JvmStatic
        fun listAndSavePlugins() {
            val pluginFiles = PublicPluginRegistry.pluginDirectory.listFiles()?.filter { it.extension == "jar" } ?: emptyList<File>()
            pluginFiles.forEach { file ->
                loadPluginFromJar(file)
            }
        }

        private fun loadPluginFromJar(jarFile: File) {
            try {
                if (hasManifest(jarFile)) {
                    createPluginObjectFromJar(jarFile)
                } else {
                    MCVersionRenamer.LOGGER.error("No valid manifest found in ${jarFile.name}")
                }
            } catch (e: Exception) {
                MCVersionRenamer.LOGGER.error("Error loading plugin from JAR file ${jarFile.name}: ${e.message}")
            }
        }

        private fun hasManifest(jarFile: File): Boolean {
            return try {
                JarFile(jarFile).use { jar ->
                    jar.getJarEntry("META-INF/MANIFEST.MF") != null
                }
            } catch (e: Exception) {
                MCVersionRenamer.LOGGER.error("Error reading JAR file ${jarFile.name}: ${e.message}")
                false
            }
        }

        private fun createPluginObjectFromJar(jarFile: File): PluginObject? {
            try {
                val jar = JarFile(jarFile)
                val manifest = jar.manifest
                val pluginInitializerClassName = manifest.mainAttributes.getValue(Attributes.Name("Plugin-Main"))

                if (pluginInitializerClassName.isNullOrEmpty()) {
                    MCVersionRenamer.LOGGER.error("Plugin-Main attribute missing in the manifest of ${jarFile.name}")
                    return null
                }

                val jarUrl = jarFile.toURI().toURL()
                val classLoader = URLClassLoader(arrayOf(jarUrl), this::class.java.classLoader)

                val initializerClass = classLoader.loadClass(pluginInitializerClassName)

                if (!PluginInitializer::class.java.isAssignableFrom(initializerClass)) {
                    MCVersionRenamer.LOGGER.error("Class ${pluginInitializerClassName} does not extend PluginInitializer.")
                    return null
                }

                val pluginInitializer = initializerClass.getDeclaredConstructor().newInstance() as PluginInitializer

                pluginInitializer.onInitialize()

                return pluginInitializer.main?.let {
                    PluginObject(
                        id = jarFile.nameWithoutExtension,
                        main = it,
                        jarFilePath = jarFile.absolutePath
                    )
                }

            } catch (e: Exception) {
                MCVersionRenamer.LOGGER.error("Error creating PluginObject from JAR file ${jarFile.name}: ${e.toString()}")
                return null
            }
        }
    }
}

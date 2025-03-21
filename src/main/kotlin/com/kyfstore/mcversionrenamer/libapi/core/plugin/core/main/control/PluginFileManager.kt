package com.kyfstore.mcversionrenamer.libapi.core.plugin.core.main.control

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.libapi.core.plugin.api.PluginInitializer
import com.kyfstore.mcversionrenamer.libapi.core.plugin.api.PluginObject
import com.kyfstore.mcversionrenamer.libapi.core.plugin.api.PluginMain
import com.kyfstore.mcversionrenamer.libapi.core.plugin.core.main.data.PublicPluginRegistry
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarFile

class PluginFileManager {

    companion object {

        // Ensure that the plugin directory exists
        @JvmStatic
        fun checkPluginDirectoryExists() {
            if (!PublicPluginRegistry.pluginDirectory.exists()) {
                PublicPluginRegistry.pluginDirectory.mkdirs()
            }
        }

        // Load all plugins from the directory and verify their manifests
        @JvmStatic
        fun listAndSavePlugins() {
            val pluginFiles = PublicPluginRegistry.pluginDirectory.listFiles()?.filter { it.extension == "jar" } ?: emptyList<File>()
            pluginFiles.forEach { file ->
                loadPluginFromJar(file)
            }
        }

        // Load a plugin from the JAR file
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

        // Check if the JAR file has a valid manifest
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

        // Create a PluginObject from a JAR file
        private fun createPluginObjectFromJar(jarFile: File): PluginObject? {
            try {
                val jar = JarFile(jarFile)
                val manifest = jar.manifest
                val pluginInitializerClassName = manifest.mainAttributes.getValue(Attributes.Name("Plugin-Main"))

                // Make sure that we have the "Plugin-Main" attribute in the manifest
                if (pluginInitializerClassName.isNullOrEmpty()) {
                    MCVersionRenamer.LOGGER.error("Plugin-Main attribute missing in the manifest of ${jarFile.name}")
                    return null
                }

                val jarUrl = jarFile.toURI().toURL()
                val classLoader = URLClassLoader(arrayOf(jarUrl), this::class.java.classLoader)

                // Load the PluginInitializer class dynamically
                val initializerClass = classLoader.loadClass(pluginInitializerClassName)

                // Ensure the class is a subclass of PluginInitializer
                if (!PluginInitializer::class.java.isAssignableFrom(initializerClass)) {
                    MCVersionRenamer.LOGGER.error("Class ${pluginInitializerClassName} does not extend PluginInitializer.")
                    return null
                }

                // Instantiate the PluginInitializer class
                val pluginInitializer = initializerClass.getDeclaredConstructor().newInstance() as PluginInitializer

                // Now call onInitialize() to register the PluginObject
                pluginInitializer.onInitialize()

                // Return the PluginObject created by the PluginInitializer
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

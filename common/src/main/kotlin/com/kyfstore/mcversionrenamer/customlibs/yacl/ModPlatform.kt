package com.kyfstore.mcversionrenamer.customlibs.yacl

object ModPlatform {
    var loadConfig: () -> Unit = { }
    var saveConfig: () -> Unit = { }

    var isPluginsEnabled: () -> Boolean = { false }

    var createConfigScreen: (Any) -> Any? = { _ -> null }

    fun <S : Any> setCreateConfigScreen(screenFactory: java.util.function.Function<S, S>) {
        this.createConfigScreen = { parent -> screenFactory.apply(parent as S) }
    }
}
package com.multi.producthunt.android.screen.settings.di

import com.multi.producthunt.android.screen.settings.SettingsScreenViewModel
import com.multi.producthunt.android.screen.timeline.TimelineScreenViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val settingsModule = DI.Module(name = "settings") {
    bindProvider { SettingsScreenViewModel(instance()) }
}
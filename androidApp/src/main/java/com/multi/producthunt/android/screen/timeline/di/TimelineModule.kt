package com.multi.producthunt.android.screen.timeline.di

import com.multi.producthunt.android.screen.timeline.TimelineScreenViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val timelineModule = DI.Module(name = "timeline") {
    bindProvider { TimelineScreenViewModel(instance()) }
}
package com.multi.producthunt.android

import android.app.Application
import com.multi.producthunt.android.di.koin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.kodein.di.DI
import org.kodein.di.DIAware

class ProductHuntApp : Application(), DIAware {
    override val di: DI
        get() = koin

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
    }
}
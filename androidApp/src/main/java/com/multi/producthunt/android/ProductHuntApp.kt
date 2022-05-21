package com.multi.producthunt.android

import android.app.Application
import com.multi.producthunt.android.screen.home.di.homeModule
import com.multi.producthunt.android.screen.login.di.loginModule
import com.multi.producthunt.android.screen.settings.di.settingsModule
import com.multi.producthunt.android.screen.timeline.di.timelineModule
import com.multi.producthunt.domain.di.domainModule
import com.multi.producthunt.network.di.networkModule
import com.multi.producthunt.utils.di.utilsModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class ProductHuntApp : Application(), DIAware {
    override lateinit var di: DI

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())

        di = DI {
            import(networkModule)
            import(domainModule)
            import(homeModule)
            import(timelineModule)
            import(settingsModule)
            import(loginModule)
            import(androidXModule(this@ProductHuntApp))
            import(utilsModule)
        }

    }
}
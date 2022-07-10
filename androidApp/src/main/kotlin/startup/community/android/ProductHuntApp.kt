package startup.community.android

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindSingleton
import startup.community.android.screen.di.viewModelModule
import startup.community.shared.domain.di.domainModule
import startup.community.shared.network.di.networkModule
import startup.community.shared.utils.KMMPreference

class ProductHuntApp : Application(), DIAware {
    override lateinit var di: DI

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        di = DI {
            import(androidXModule(this@ProductHuntApp))
            import(networkModule)
            import(domainModule)
            import(viewModelModule)

            bindSingleton {
                KMMPreference(this@ProductHuntApp)
            }
        }
    }
}
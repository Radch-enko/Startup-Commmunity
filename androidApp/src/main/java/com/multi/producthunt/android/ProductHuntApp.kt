package com.multi.producthunt.android

import android.app.Application
import com.multi.producthunt.di.koin
import org.kodein.di.DI

class ProductHuntApp : Application() {

    fun getDI(): DI {
        return koin.di
    }
}
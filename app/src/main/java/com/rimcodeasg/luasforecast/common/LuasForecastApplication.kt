package com.rimcodeasg.luasforecast.common

import android.app.Application
import com.rimcodeasg.luasforecast.data.repositories.LuasForecastRepository
import com.rimcodeasg.luasforecast.data.retrofit.RetrofitSource
import com.rimcodeasg.luasforecast.di.ViewModelFactory
import com.rimcodeasg.luasforecast.di.bindViewModel
import com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast.LuasForecastViewModel
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class LuasForecastApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@LuasForecastApplication))

        bind() from singleton { LuasForecastRepository(instance()) }
        bind() from singleton { RetrofitSource() }

        bind<ViewModelFactory>() with singleton  { ViewModelFactory(kodein.direct) }

        bindViewModel<LuasForecastViewModel>() with provider {
            LuasForecastViewModel(
                instance()
            )
        }

        bindViewModel<TramDetailsViewModel>() with provider {
            TramDetailsViewModel()
        }
    }

}
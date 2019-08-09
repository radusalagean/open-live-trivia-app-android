package com.busytrack.openlivetrivia.application

import android.app.Application
import com.busytrack.openlivetrivia.di.application.ApplicationComponent
import com.busytrack.openlivetrivia.di.application.ApplicationModule
import com.busytrack.openlivetrivia.di.application.DaggerApplicationComponent
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

open class OpenLiveTriviaApp : Application() {
    /**
     * Dagger2 Application Component initialization
     */
    val applicationComponent: ApplicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()

    override fun onCreate() {
        super.onCreate()
        // Set up the default font for the entire app UI
        ViewPump.init(ViewPump.builder()
            .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NunitoSans-Regular.ttf")
                .build()))
            .build())
    }
}
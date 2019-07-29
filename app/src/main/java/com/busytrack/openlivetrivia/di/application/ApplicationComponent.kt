package com.busytrack.openlivetrivia.di.application

import com.busytrack.openlivetrivia.di.activity.ActivityComponent
import com.busytrack.openlivetrivia.di.activity.ActivityModule
import com.busytrack.openlivetrivia.di.application.auth.AuthenticationModule
import com.busytrack.openlivetrivia.di.application.auth.AuthorizationModule
import com.busytrack.openlivetrivia.di.application.network.NetworkModule
import com.busytrack.openlivetrivia.di.application.network.OpenLiveTriviaApiModule
import com.busytrack.openlivetrivia.di.application.persistence.PersistenceModule
import com.busytrack.openlivetrivia.di.application.socket.SocketModule
import com.busytrack.openlivetrivia.di.service.ServiceComponent
import com.busytrack.openlivetrivia.di.service.ServiceModule
import dagger.Component

@ApplicationScope
@Component(modules = [
    ApplicationModule::class,
    AuthenticationModule::class,
    AuthorizationModule::class,
    OpenLiveTriviaApiModule::class,
    SocketModule::class,
    NetworkModule::class,
    PersistenceModule::class
])
interface ApplicationComponent {
    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent
    fun newServiceComponent(serviceModule: ServiceModule): ServiceComponent
}
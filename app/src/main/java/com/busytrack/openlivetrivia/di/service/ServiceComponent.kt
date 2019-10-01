package com.busytrack.openlivetrivia.di.service

import dagger.Subcomponent

@ServiceScope
@Subcomponent(modules = [
    ServiceModule::class
])
interface ServiceComponent {

}
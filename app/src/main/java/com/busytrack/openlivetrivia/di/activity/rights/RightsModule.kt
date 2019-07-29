package com.busytrack.openlivetrivia.di.activity.rights

import android.content.Context
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.rights.RightsManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RightsModule {
    @Provides
    @ActivityScope
    fun provideRightsManager(
        @Named(NAMED_ACTIVITY_CONTEXT) context: Context,
        authenticationManager: AuthenticationManager,
        dialogManager: DialogManager
    ): RightsManager = RightsManager(context, authenticationManager, dialogManager)
}
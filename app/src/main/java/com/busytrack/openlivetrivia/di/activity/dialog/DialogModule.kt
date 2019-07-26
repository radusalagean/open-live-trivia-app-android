package com.busytrack.openlivetrivia.di.activity.dialog

import android.content.Context
import com.busytrack.openlivetrivia.di.NAMED_ACTIVITY_CONTEXT
import com.busytrack.openlivetrivia.di.activity.ActivityScope
import com.busytrack.openlivetrivia.dialog.DialogManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DialogModule {
    @Provides
    @ActivityScope
    fun provideDialogManager(
        @Named(NAMED_ACTIVITY_CONTEXT) context: Context
    ): DialogManager = DialogManager(context)
}
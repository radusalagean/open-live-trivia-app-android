package com.busytrack.openlivetrivia.activity

import android.os.Bundle
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.SignInResultContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.infobar.InfoBarManager
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    override lateinit var authenticationManager: AuthenticationManager

    @Inject
    override lateinit var infoBarManager: InfoBarManager

    @Inject
    override lateinit var signInResultContract: SignInResultContract

    // Lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Activity contract implementation

    override fun getFragmentContainerId() = R.id.nav_host_fragment

    // BaseActivity implementation

    override fun injectDependencies() {
        activityComponent.inject(this)
    }
}

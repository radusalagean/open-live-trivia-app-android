package com.busytrack.openlivetrivia.activity

import android.os.Bundle
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.SignInResultContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.infobar.InfoBarManager
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
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
        addDefaultFragmentIfNecessary()
    }

    // Activity contract implementation

    override fun getFragmentContainerId() = R.id.fragment_container

    // BaseActivity implementation

    override fun getDefaultFragment() = AuthenticationFragment.newInstance()

    override fun injectDependencies() {
        activityComponent.inject(this)
    }
}

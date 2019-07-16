package com.busytrack.openlivetrivia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.screen.authentication.AuthenticationFragment
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var authenticationManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addDefaultFragmentIfNecessary()
    }

    // Activity contract implementation

    override fun getFragmentContainerId() = R.id.fragment_container

    // BaseActivity implementation

    override fun handleGoogleSignInResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            authenticationManager.handleGoogleSignInSuccess(data)
        } else {
            authenticationManager.handleGoogleSignInFailure(resultCode)
        }
    }

    override fun getDefaultFragment() = AuthenticationFragment.newInstance()
}

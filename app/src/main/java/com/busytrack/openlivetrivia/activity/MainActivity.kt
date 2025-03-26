package com.busytrack.openlivetrivia.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.eventbus.EventBus
import com.busytrack.openlivetrivia.infobar.InfoBarManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    override lateinit var authenticationManager: AuthenticationManager

    @Inject
    override lateinit var infoBarManager: InfoBarManager

    @Inject
    override lateinit var eventBus: EventBus

    // Lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventBus.eventFlow.collect { event ->
                    when (event) {
                        EventBus.Event.RESTART_ACTIVITY -> restartActivity()
                    }
                }
            }
        }
    }

    // Activity contract implementation

    override fun getFragmentContainerId() = R.id.nav_host_fragment

    // BaseActivity implementation

    override fun injectDependencies() {
        activityComponent.inject(this)
    }

    private fun restartActivity() {
        finish()
        startActivity(Intent(this@MainActivity, MainActivity::class.java))
    }
}

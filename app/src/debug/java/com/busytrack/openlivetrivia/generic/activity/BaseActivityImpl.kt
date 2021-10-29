package com.busytrack.openlivetrivia.generic.activity

import android.os.Bundle
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.SignInResultContract
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.fragment.BaseFragmentImpl
import com.busytrack.openlivetrivia.infobar.InfoBarManager
import org.mockito.Mockito.mock

/**
 * Barebone implementation for [BaseActivity] with mocked dependencies.
 * Used for unit testing purposes.
 *
 * Note: This class is in the debug variant, and not in the test because it needs to be referenced
 * in the Android Manifest file.
 */
class BaseActivityImpl : BaseActivity() {

    override var authenticationManager: AuthenticationManager =
        mock(AuthenticationManager::class.java)
    override var infoBarManager: InfoBarManager =
        mock(InfoBarManager::class.java)
    override var signInResultContract: SignInResultContract =
        mock(SignInResultContract::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_test)
        addDefaultFragmentIfNecessary()
    }

    override fun getDefaultFragment(): BaseFragment {
        return BaseFragmentImpl()
    }

    override fun getFragmentContainerId(): Int {
        return R.id.fragment_container
    }

    fun showTestFragment(fragment: BaseFragment) {
        showFragment(fragment)
    }
}
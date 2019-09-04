package com.busytrack.openlivetrivia.screen.authentication

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.BuildConfig
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.SystemInfoModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AuthenticationPresenter(
    model: AuthenticationMvp.Model,
    activityContract: ActivityContract,
    private val authenticationManager: AuthenticationManager,
    private val authorizationManager: AuthorizationManager,
    private val dialogManager: DialogManager
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model, activityContract),
    AuthenticationMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, AuthenticationViewModel::class.java)
    }

    override fun checkServerCompatibility() {
        refreshing = true
        disposer.add(model.getSystemInfo()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<SystemInfoModel>() {
                override fun onNext(t: SystemInfoModel) {
                    if (BuildConfig.VERSION_CODE < t.minAppVersionCode) {
                        // The server is not compatible with this app version, show error dialog and exit
                        dialogManager.showFatalAlertDialog(
                            R.string.dialog_title_incompatible_version,
                            R.string.dialog_message_incompatible_version
                        )
                        refreshing = false
                        return
                    } else if (BuildConfig.VERSION_CODE < t.latestAppVersionCode) {
                        // The server is still compatible with this app version, but a newer app version is available
                        activityContract.showWarningMessage(R.string.message_app_update_available)
                    }
                    onServerCompatibilityChecked()
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    activityContract.showWarningMessage(R.string.message_failed_to_check_server_compatibility)
                    onServerCompatibilityChecked()
                }

                override fun onComplete() {}
            }))
    }

    override fun firebaseLogIn() {
        authenticationManager.signIn()
        refreshing = true
    }

    override fun firebaseLogOut() {
        authenticationManager.signOut(true)
    }

    override fun login() {
        refreshing = true
        disposer.add(model.login()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<UserModel>() {
                override fun onNext(t: UserModel) {
                    authenticationManager.setAuthenticatedUser(t)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    if (e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        // The selected Google account is not yet registered in the app
                        refreshing = false
                        view?.showRegisterPage()
                    } else if (authenticationManager.getAuthenticatedUser() != null) {
                        // Other error, if user info was cached,
                        // use the already saved info and show a warning message
                        activityContract.showWarningMessage(R.string.message_unable_to_authenticate)
                        showMainMenuScreen()
                    } else {
                        // fallback
                        refreshing = false
                        activityContract.showErrorMessage(R.string.message_failed_to_log_in, e.message)
                    }
                }

                override fun onComplete() {
                    showMainMenuScreen()
                }
            })
        )
    }

    override fun register(username: String) {
        refreshing = true
        val registerModel = OutgoingRegisterModel(username)
        disposer.add(
            model.registerUser(registerModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : ReactiveObserver<UserModel>(this) {
                    override fun onNext(t: UserModel) {
                        authenticationManager.setAuthenticatedUser(t)
                        activityContract.showInfoMessage(R.string.message_registered)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        activityContract.showErrorMessage(R.string.general_error_message, e.message)
                    }

                    override fun onComplete() {
                        showMainMenuScreen()
                    }
                })
        )
    }

    override fun checkUsernameAvailability(username: String) {
        disposer.add(
            model.checkUsernameAvailability(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        if (e is HttpException && e.code() == HttpURLConnection.HTTP_CONFLICT) {
                            // The username is already registered in the app
                            view?.setUsernameAvailability(false)
                        }
                    }

                    override fun onComplete() {
                        view?.setUsernameAvailability(true)
                    }
                })
        )
    }

    private fun showMainMenuScreen() {
        view?.removeFragment()
        activityContract.showMainMenuScreen()
    }

    private fun onServerCompatibilityChecked() {
        if (authorizationManager.isUserAuthenticated()) {
            // If a Google account was previously selected, authenticate with the backend
            login()
        } else {
            refreshing = false
        }
    }

    // EspressoGlobalIdlingResource implementation

    override var idlingResourceInitialized: Boolean = true
}
package com.busytrack.openlivetrivia.screen.authentication

import androidx.fragment.app.Fragment
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetrivia.generic.observer.ReactiveObserver
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AuthenticationPresenter(
    model: AuthenticationMvp.Model,
    activityContract: ActivityContract,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model, activityContract),
    AuthenticationMvp.Presenter {

    override fun initViewModel(fragment: Fragment) {
        model.initViewModel(fragment, AuthenticationViewModel::class.java)
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
            .subscribeWith(object : ReactiveObserver<UserModel>(this) {
                override fun onNext(t: UserModel) {
                    authenticationManager.setAuthenticatedUser(t)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    if (e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        // The selected Google account is not yet registered in the app
                        view?.showRegisterPage()
                    } else if (authenticationManager.getAuthenticatedUser() != null) {
                        // Other error, if user info was cached,
                        // use the already saved info and show a warning message
                        activityContract.showWarningMessage(R.string.message_unable_to_authenticate)
                        showMainMenuScreen()
                    } else {
                        // fallback
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
}
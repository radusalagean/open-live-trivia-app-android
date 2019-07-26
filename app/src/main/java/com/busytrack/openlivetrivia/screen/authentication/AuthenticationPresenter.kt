package com.busytrack.openlivetrivia.screen.authentication

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
    private val activityContract: ActivityContract,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model),
    AuthenticationMvp.Presenter {

    override fun firebaseLogIn() {
        authenticationManager.signIn()
        refreshing = true
    }

    override fun firebaseLogOut() {
        authenticationManager.signOut(true)
    }

    override fun login() {
        refreshing = true
        compositeDisposable.add(model.login()
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
        compositeDisposable.add(
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
        compositeDisposable.add(
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
package com.busytrack.openlivetrivia.screen.authentication

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.generic.mvp.BasePresenter
import com.busytrack.openlivetriviainterface.rest.model.OutgoingRegisterModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AuthenticationPresenter(
    model: AuthenticationMvp.Model,
    private val authenticationManager: AuthenticationManager
) : BasePresenter<AuthenticationMvp.View, AuthenticationMvp.Model>(model),
    AuthenticationMvp.Presenter {

    override fun firebaseLogIn() {
        authenticationManager.signIn()
        refreshing = true
    }

    override fun login() {
        refreshing = true
        compositeDisposable.add(model.login()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<UserModel>() {
                override fun onNext(t: UserModel) {
                    Timber.d("onNext($t)")
                }

                override fun onError(e: Throwable) {
                    Timber.e("onError($e)")
                    refreshing = false
                    if (e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        // The selected Google account is not yet registered in the app
                        view?.showRegisterPage()
                    }
                }

                override fun onComplete() {
                    Timber.d("onComplete()")
                    // TODO Show Home Screen
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
                .subscribeWith(object : DisposableObserver<UserModel>() {
                    override fun onNext(t: UserModel) {
                        Timber.d("onNext($t)")
                    }

                    override fun onError(e: Throwable) {
                        Timber.e("onError($e)")
                        refreshing = false
                    }

                    override fun onComplete() {
                        Timber.d("onComplete()")
                        // TODO Show Home Screen
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
                        Timber.e("onError($e)")
                        if (e is HttpException && e.code() == HttpURLConnection.HTTP_CONFLICT) {
                            // The username is already registered in the app
                            view?.setUsernameAvailability(false)
                        }
                    }

                    override fun onComplete() {
                        Timber.d("onComplete()")
                        view?.setUsernameAvailability(true)
                    }
                })
        )
    }
}
package com.busytrack.openlivetrivia.network.authenticator

import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.generic.eventbus.EventBus
import com.google.common.net.HttpHeaders.AUTHORIZATION
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class OpenLiveTriviaAuthenticator(
    private val authenticationManager: AuthenticationManager,
    private val authorizationManager: AuthorizationManager,
    private val eventBus: EventBus
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        fun buildAdjustedRequest(newToken: String) =
            response.request.newBuilder()
                .header(AUTHORIZATION, newToken)
                .build()

        return authorizationManager.getIdToken(forceRefresh = true).let { idToken ->
            if (idToken.isNullOrBlank()) {
                authenticationManager.signOut()
                eventBus.emit(EventBus.Event.RESTART_ACTIVITY)
                null
            } else {
                buildAdjustedRequest(idToken)
            }
        }
    }
}
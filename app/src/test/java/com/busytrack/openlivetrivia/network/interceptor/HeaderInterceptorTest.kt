package com.busytrack.openlivetrivia.network.interceptor

import com.busytrack.openlivetrivia.auth.AuthorizationManager
import com.busytrack.openlivetrivia.test.getOpenLiveTriviaTestApiService
import com.busytrack.openlivetriviainterface.rest.OpenLiveTriviaApiService
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class HeaderInterceptorTest {

    @InjectMocks
    private lateinit var headerInterceptor: HeaderInterceptor

    @Mock
    private lateinit var authorizationManager: AuthorizationManager

    private lateinit var server: MockWebServer
    private lateinit var apiService: OpenLiveTriviaApiService

    private val successResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        server = MockWebServer()
        apiService = getOpenLiveTriviaTestApiService(
            server = server,
            client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build()
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun should_addAuthorizationHeader_when_interceptWithAuthenticatedUser() {
        // User is authenticated
        doReturn(true).`when`(authorizationManager).isUserAuthenticated()

        // Prepare the token
        val token = "test_token"
        doReturn(token).`when`(authorizationManager).getIdToken()

        // Prepare the response
        server.enqueue(successResponse)

        // Call any api method and check if the header is received by the server
        apiService.checkUsernameAvailability("any").test().run {
            assertNoErrors()
            val request = server.takeRequest()
            assertThat(request.headers.get("authorization")).isEqualTo(token)
        }
    }

    @Test
    fun should_notAddAuthorizationHeader_when_interceptWithUnauthenticatedUser() {
        // User is authenticated
        doReturn(false).`when`(authorizationManager).isUserAuthenticated()

        // Prepare the response
        server.enqueue(successResponse)

        // Call any api method and check if the header is received by the server
        apiService.checkUsernameAvailability("any").test().run {
            assertNoErrors()
            val request = server.takeRequest()
            assertThat(request.headers.get("authorization")).isNull()
        }
    }
}
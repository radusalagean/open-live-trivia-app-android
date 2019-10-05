package com.busytrack.openlivetriviainterface.rest

import com.busytrack.openlivetriviainterface.rest.NetworkTestUtil.getJson
import com.busytrack.openlivetriviainterface.rest.model.*
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.*

/**
 * Test the requests sent to the server and the parsing of the responses
 *  expected to be returned by the server
 */
class OpenLiveTriviaApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var apiService: OpenLiveTriviaApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        initOpenLiveTriviaTestApiService()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun registerUser() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody(getJson("user_register"))
        )

        apiService.registerUser(OutgoingRegisterModel("Some user")).test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("POST")
            assertThat(request.path).isEqualTo("/user/register")
            // Check the response
            assertValue(
                UserModel(
                    userId = "5d1f2052a93b8d38b87750d3",
                    username = "Some user",
                    rights = UserRightsLevel.REGULAR,
                    coins = 100.0,
                    playing = false,
                    joined = Date(1562323304203)
                )
            )
        }
    }

    @Test
    fun login() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("user_login"))
        )

        apiService.login().test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("POST")
            assertThat(request.path).isEqualTo("/user/login")
            // Check the response
            assertValue(
                UserModel(
                    userId = "5d1f2052a93b8d38b87750d3",
                    username = "Some user",
                    rights = UserRightsLevel.REGULAR,
                    coins = 100.0,
                    playing = false,
                    joined = Date(1562323304203)
                )
            )
        }
    }

    @Test
    fun deleteUser() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("user_delete"))
        )

        apiService.deleteUser().test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("DELETE")
            assertThat(request.path).isEqualTo("/user/delete")
            // Check the response
            assertValue(
                MessageModel("Account removed successfully")
            )
        }
    }

    @Test
    fun checkUsernameAvailability() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CONFLICT)
        )

        apiService.checkUsernameAvailability("Some user").test().run {
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/user/availability/Some%20user")
            assertNotComplete() // Errors due to 409 CONFLICT
        }
    }

    @Test
    fun updateUserRights() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("user_rights"))
        )

        apiService.updateUserRights(
            userId = "5d1f2052a93b8d38b87750d3",
            rightsLevel = 1
        ).test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("PUT")
            assertThat(request.path).isEqualTo("/user/rights/5d1f2052a93b8d38b87750d3/1")
            // Check the response
            assertValue(
                MessageModel("Some user's rights changed to type 1")
            )
        }
    }

    @Test
    fun getLeaderboard() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("user_leaderboard"))
        )

        apiService.getLeaderboard(page = 2).test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/user/leaderboard?page=2")
            // Check the response
            assertValue(
                PaginatedResponseModel(
                    page = 2,
                    pages = 2,
                    itemsCount = 22,
                    perPage = 20,
                    items = listOf(
                        UserModel(
                            userId = "5d1f2968a93b8d38b87750d4",
                            rights = UserRightsLevel.ADMIN,
                            coins = 100.0,
                            username = "Some user",
                            lastSeen = Date(1562323304203),
                            playing = false,
                            joined = Date(1562323304203)
                        ),
                        UserModel(
                            userId = "5d1f2968a93b8d38b87750d5",
                            rights = UserRightsLevel.REGULAR,
                            coins = 1050.2,
                            username = "Some other user",
                            lastSeen = Date(1562323484000),
                            playing = true,
                            joined = Date(1562323484000)
                        )
                    )
                )
            )
        }
    }

    @Test
    fun getMe() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("user_me"))
        )

        apiService.getMe().test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/user/me")
            // Check the response
            assertValue(
                UserModel(
                    userId = "5d1f2052a93b8d38b87750d3",
                    username = "Some user",
                    rights = UserRightsLevel.REGULAR,
                    coins = 100.0,
                    playing = false,
                    joined = Date(1562323304203)
                )
            )
        }
    }

    @Test
    fun getReportedEntries() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("reported_entry_get_reports"))
        )

        apiService.getReportedEntries(page = 3, banned = false).test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/reported_entry/get_reports?banned=false&page=3")
            // Check the response
            assertValue(
                PaginatedResponseModel(
                    page = 3,
                    pages = 3,
                    itemsCount = 21,
                    perPage = 10,
                    items = listOf(
                        EntryReportModel(
                            reporters = listOf(
                                UserModel(
                                    userId = "5d1f2968a93b8d38b87750d4",
                                    username = "Some user"
                                )
                            ),
                            banned = false,
                            reportId = "5d1f379a78c6e7342c49488e",
                            lastReported = Date(1562326938216),
                            entryId = 34770,
                            category = "category",
                            clue = "Clue text",
                            answer = "Answer text"
                        )
                    )
                )
            )
        }
    }

    @Test
    fun banReportedEntry() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("reported_entry_ban"))
        )

        apiService.banReportedEntry("5d1f2968a93b8d38b87750d4").test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("PUT")
            assertThat(request.path).isEqualTo("/reported_entry/ban/5d1f2968a93b8d38b87750d4")
            // Check the response
            assertValue(
                MessageModel("The entry has been banned successfully")
            )
        }
    }

    @Test
    fun unbanReportedEntry() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("reported_entry_unban"))
        )

        apiService.unbanReportedEntry("5d1f2968a93b8d38b87750d4").test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("PUT")
            assertThat(request.path).isEqualTo("/reported_entry/unban/5d1f2968a93b8d38b87750d4")
            // Check the response
            assertValue(
                MessageModel("The entry has been unbanned successfully")
            )
        }
    }

    @Test
    fun dismissReportedEntry() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("reported_entry_dismiss"))
        )

        apiService.dismissReportedEntry("5d1f2968a93b8d38b87750d4").test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("PUT")
            assertThat(request.path).isEqualTo("/reported_entry/dismiss/5d1f2968a93b8d38b87750d4")
            // Check the response
            assertValue(
                MessageModel("Entry report dismissed successfully")
            )
        }
    }

    @Test
    fun disconnectEveryone() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("system_disconnect_everyone"))
        )

        apiService.disconnectEveryone().test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("POST")
            assertThat(request.path).isEqualTo("/system/disconnect_everyone")
            // Check the response
            assertValue(
                MessageModel("Sent the disconnect signal to 10 clients")
            )
        }
    }

    @Test
    fun getSystemInfo() {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("system_info"))
        )

        apiService.getSystemInfo().test().run {
            assertNoErrors()
            // Check the request
            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/system/info")
            // Check the response
            assertValue(
                SystemInfoModel(
                    serverVersion = "1.0.0",
                    minAppVersionCode = 1,
                    latestAppVersionCode = 2,
                    isTriviaServiceRunning = true
                )
            )
        }
    }

    /**
     * Returns the Retrofit API service used for unit testing
     */
    private fun initOpenLiveTriviaTestApiService() {

        apiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(OpenLiveTriviaApiService::class.java)
    }
}
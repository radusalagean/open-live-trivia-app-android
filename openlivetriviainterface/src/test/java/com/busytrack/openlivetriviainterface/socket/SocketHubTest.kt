package com.busytrack.openlivetriviainterface.socket

import android.os.Handler
import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetriviainterface.rest.NetworkTestUtil.getJson
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.event.SocketEventListener
import com.busytrack.openlivetriviainterface.socket.event.SocketIncomingEvent
import com.busytrack.openlivetriviainterface.socket.model.*
import com.nhaarman.mockitokotlin2.*
import io.socket.client.Socket
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
class SocketHubTest {

    @Mock
    private lateinit var socket: Socket

    @Mock
    private lateinit var socketEventListener: SocketEventListener

    private lateinit var socketHub: SocketHub

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        socketHub = SocketHub(socket)
        // Register a mock event listener
        socketHub.registerEventListener(
            socketEventListener,
            Handler(Looper.myLooper())
        )
    }

    @After
    fun tearDown() {
        socketHub.clearEventListeners()
    }

    @Test
    fun should_notReachCallback_when_socketEventReceived_with_noRegisteredListener() {
        // Clear registered listener
        socketHub.unregisterEventListener(socketEventListener)

        // Trigger any event
        socketHub.onEventReceived(SocketIncomingEvent.CONNECT)

        // Verify
        verifyZeroInteractions(socketEventListener)
    }

    @Test
    fun should_reachCallback_when_connectEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("connect"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.CONNECT)

        // Is the callback reached?
        verify(socketEventListener).onConnected()
    }

    @Test
    fun should_reachCallback_when_disconnectEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("disconnect"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.DISCONNECT)

        // Is the callback reached?
        verify(socketEventListener).onDisconnected()
    }

    @Test
    fun should_reachCallback_when_connectingEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("connecting"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.CONNECTING)

        // Is the callback reached?
        verify(socketEventListener).onConnecting()
    }

    @Test
    fun should_reachCallback_when_connectErrorEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("connect_error"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.CONNECT_ERROR)

        // Is the callback reached?
        verify(socketEventListener).onConnectionError()
    }

    @Test
    fun should_reachCallback_when_connectTimeoutEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("connect_timeout"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.CONNECT_TIMEOUT)

        // Is the callback reached?
        verify(socketEventListener).onConnectionTimeout()
    }

    @Test
    fun should_reachCallback_when_authenticatedEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("authenticated"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.AUTHENTICATED)

        // Is the callback reached?
        verify(socketEventListener).onAuthenticated()
    }

    @Test
    fun should_reachCallback_when_unauthorizedEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("unauthorized"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.UNAUTHORIZED,
            getJson("socket_event_unauthorized")
        )

        // Is the callback reached?
        verify(socketEventListener).onUnauthorized(
            UnauthorizedModel("User id not found for the specified token, you need to register first")
        )
    }

    @Test
    fun should_reachCallback_when_welcomeEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("WELCOME"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.WELCOME,
            getJson("socket_event_welcome")
        )

        // Is the callback reached?
        verify(socketEventListener).onWelcome(
            GameStateModel(
                gameState = GameState.SPLIT,
                userCoins = 100.0,
                entryId = 121885,
                category = "we governed that state",
                clue = "Pat Brown,Pete Wilson",
                answer = "C____o___a",
                currentValue = 14.0,
                elapsedSplitSeconds = 7,
                totalSplitSeconds = 15,
                freeAttemptsLeft = 2,
                entryReported = false,
                players = 1,
                attempts = listOf(
                    AttemptModel(
                        id = 1,
                        userId = "5d1f2052a93b8d38b87750d3",
                        username = "Radu",
                        message = "test attempt",
                        correct = false,
                        correctAnswer = null
                    )
                )
            )
        )
    }

    @Test
    fun should_reachCallback_when_peerJoinEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("PEER_JOIN"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.PEER_JOIN,
            getJson("socket_event_peer_join")
        )

        // Is the callback reached?
        verify(socketEventListener).onPeerJoin(
            PresenceModel(
                userId = "5d1f2052a93b8d38b87750d3",
                username = "Radu"
            )
        )
    }

    @Test
    fun should_reachCallback_when_peerAttemptEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("PEER_ATTEMPT"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.PEER_ATTEMPT,
            getJson("socket_event_peer_attempt")
        )

        // Is the callback reached?
        verify(socketEventListener).onPeerAttempt(
            AttemptModel(
                id = 1,
                userId = "5d1f2052a93b8d38b87750d3",
                username = "Radu",
                message = "test attempt",
                correct = false,
                correctAnswer = null
            )
        )
    }

    @Test
    fun should_reachCallback_when_insufficientFundsEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("INSUFFICIENT_FUNDS"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.INSUFFICIENT_FUNDS)

        // Is the callback reached?
        verify(socketEventListener).onInsufficientFunds()
    }

    @Test
    fun should_reachCallback_when_coinDiffEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("COIN_DIFF"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.COIN_DIFF,
            getJson("socket_event_coin_diff")
        )

        // Is the callback reached?
        verify(socketEventListener).onCoinDiff(
            CoinDiffModel(
                coinDiff = 6.0
            )
        )
    }

    @Test
    fun should_reachCallback_when_peerReactionEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("PEER_REACTION"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.PEER_REACTION,
            getJson("socket_event_peer_reaction")
        )

        // Is the callback reached?
        verify(socketEventListener).onPeerReaction(
            ReactionModel(
                userId = "5d1f2052a93b8d38b87750d3",
                username = "Radu",
                emoji = "\uD83D\uDC7E"
            )
        )
    }

    @Test
    fun should_reachCallback_when_roundEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("ROUND"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.ROUND,
            getJson("socket_event_round")
        )

        // Is the callback reached?
        verify(socketEventListener).onRound(
            RoundModel(
                entryId = 121885,
                category = "we governed that state",
                clue = "Pat Brown,Pete Wilson",
                answer = "__________",
                currentValue = 20.0
            )
        )
    }

    @Test
    fun should_reachCallback_when_splitEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("SPLIT"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.SPLIT,
            getJson("socket_event_split")
        )

        // Is the callback reached?
        verify(socketEventListener).onSplit(
            SplitModel(
                answer = "C_li_ornia",
                currentValue = 4.0
            )
        )
    }

    @Test
    fun should_reachCallback_when_revealEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("REVEAL"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.REVEAL,
            getJson("socket_event_reveal")
        )

        // Is the callback reached?
        verify(socketEventListener).onReveal(
            RevealModel(
                answer = "California"
            )
        )
    }

    @Test
    fun should_reachCallback_when_entryReportedOkEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("ENTRY_REPORTED_OK"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.ENTRY_REPORTED_OK)

        // Is the callback reached?
        verify(socketEventListener).onEntryReportedOk()
    }

    @Test
    fun should_reachCallback_when_entryReportedErrorEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("ENTRY_REPORTED_ERROR"), any())

        // Trigger event
        socketHub.onEventReceived(SocketIncomingEvent.ENTRY_REPORTED_ERROR)

        // Is the callback reached?
        verify(socketEventListener).onEntryReportedError()
    }

    @Test
    fun should_reachCallback_when_playerListEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("PLAYER_LIST"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.PLAYER_LIST,
            getJson("socket_event_player_list")
        )

        // Is the callback reached?
        verify(socketEventListener).onPlayerList(
            PlayerListModel(
                listOf(
                    UserModel(
                        userId = "5d1f2052a93b8d38b87750d3",
                        username = "Radu",
                        rights = UserRightsLevel.REGULAR,
                        coins = 106.0,
                        joined = Date(1562323304203)
                    )
                )
            )
        )
    }

    @Test
    fun should_reachCallback_when_peerLeftEventReceived_with_registeredListener() {
        // Is the event registered?
        verify(socket).on(eq("PEER_LEFT"), any())

        // Trigger event
        socketHub.onEventReceived(
            SocketIncomingEvent.PEER_LEFT,
            getJson("socket_event_peer_left")
        )

        // Is the callback reached?
        verify(socketEventListener).onPeerLeft(
            PresenceModel(
                userId = "5d1f2052a93b8d38b87750d3",
                username = "Radu"
            )
        )
    }

    @Test
    fun should_connectSocket_when_connectIsCalled() {
        // Call connect
        socketHub.connect()

        // Verify
        verify(socket).connect()
    }

    @Test
    fun should_disconnectAndCloseSocket_when_disconnectIsCalled() {
        // Call disconnect
        socketHub.disconnect()

        // Verify
        inOrder(socket) {
            verify(socket).disconnect()
            verify(socket).close()
        }
    }

    @Test
    fun should_emitAuthenticationEvent_when_authenticateIsCalled() {
        // Prepare
        val idToken = "test_token"

        // Call
        socketHub.authenticate(idToken)

        // Verify
        verify(socket).emit(eq("authentication"), any())
    }

    @Test
    fun should_emitAttemptEvent_when_attemptIsCalled() {
        // Prepare
        val message = "test message"

        // Call
        socketHub.attempt(message)

        // Verify
        verify(socket).emit(eq("ATTEMPT"), any())
    }

    @Test
    fun should_emitReactEvent_when_reactIsCalled() {
        // Prepare
        val emoji = "\uD83D\uDC7E"

        // Call
        socketHub.react(emoji)

        // Verify
        verify(socket).emit(eq("REACTION"), any())
    }

    @Test
    fun should_emitReportEntryEvent_when_reportCurrentEntryIsCalled() {
        // Call
        socketHub.reportCurrentEntry()

        // Verify
        verify(socket).emit(eq("REPORT_ENTRY"))
    }

    @Test
    fun should_emitRequestPlayerListEvent_when_requestPlayerListIsCalled() {
        // Call
        socketHub.requestPlayerList()

        // Verify
        verify(socket).emit(eq("REQUEST_PLAYER_LIST"))
    }
}
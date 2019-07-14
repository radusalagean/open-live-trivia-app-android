package com.busytrack.openlivetrivia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetriviainterface.TriviaInterfaceHub
import com.busytrack.openlivetriviainterface.socket.event.SocketEventListener
import com.busytrack.openlivetriviainterface.socket.model.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private var idToken: String? = null

    private val triviaInterfaceHub = TriviaInterfaceHub()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_SERVER_CLIENT_ID)
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        firebaseAuth = FirebaseAuth.getInstance()
        with(triviaInterfaceHub.socketHub) {
            registerEventListener(object: SocketEventListener {
                override fun onConnected() {
                    idToken?.let { authenticate(it) }
                }

                override fun onDisconnected() {

                }

                override fun onConnecting() {

                }

                override fun onConnectionError() {

                }

                override fun onConnectionTimeout() {

                }

                override fun onAuthenticated() {

                }

                override fun onUnauthorized(model: UnauthorizedModel) {
                    Timber.w("unauthorized reason: ${model.message}")
                }

                override fun onWelcome(model: GameStateModel) {
                    Timber.d(model.toString())
                }

                override fun onPeerJoin(model: PresenceModel) {

                }

                override fun onPeerAttempt(model: AttemptModel) {

                }

                override fun onCoinDiff(model: CoinDiffModel) {

                }

                override fun onPeerReaction(model: ReactionModel) {

                }

                override fun onRound(model: RoundModel) {

                }

                override fun onSplit(model: SplitModel) {

                }

                override fun onReveal(model: RevealModel) {

                }

                override fun onEntryReportedOk() {

                }

                override fun onEntryReportedError() {

                }

                override fun onPlayerList(model: PlayerListModel) {

                }

                override fun onPeerLeft(model: PresenceModel) {

                }
            })
        }
    }

    private fun handleAccount(account: FirebaseUser?) {
        Toast.makeText(this, "Account: ${account?.uid}", Toast.LENGTH_LONG).show()
        var time: Long = System.nanoTime()
        account?.getIdToken(false)?.addOnCompleteListener {
            try {
                idToken = it.result?.token
                val elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time)
                Timber.d("elapsed: $elapsed millis | ${account.uid} <> $idToken")
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val account = firebaseAuth.currentUser
        handleAccount(account)

        sign_in_button.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, 0)
        }
        sign_out.setOnClickListener {
            Timber.d("revoke access")
            firebaseAuth.signOut()
            googleSignInClient.revokeAccess()
        }
        socket_connect.setOnClickListener {
            Timber.d("socket.io connect")
            triviaInterfaceHub.socketHub.connect()
        }
        socket_disconnect.setOnClickListener {
            triviaInterfaceHub.socketHub.disconnect()
        }
        attempt.setOnClickListener {
            triviaInterfaceHub.socketHub.attempt("testAttempt")
        }
    }

    override fun onStop() {
        super.onStop()
        sign_in_button.setOnClickListener(null)
        sign_out.setOnClickListener(null)
        socket_connect.setOnClickListener(null)
        socket_disconnect.setOnClickListener(null)
        attempt.setOnClickListener(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            Timber.d("result code: $resultCode")
            if (resultCode == Activity.RESULT_OK) {
                // Google sign in was successful, authenticate with firebase
                try {
                    val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseAuthWithGoogle(accountTask.result)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Timber.d("firebaseAuthWithGoogle: ${account?.id}")
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    handleAccount(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(this, "Failed to sign in", Toast.LENGTH_LONG).show()
                }
            }
    }
}

package com.busytrack.openlivetrivia.screen.game

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.extension.applyText
import com.busytrack.openlivetrivia.extension.setVisible
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_LONG
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_SHORT
import com.busytrack.openlivetriviainterface.USER_THUMBNAILS_EXTENSION
import com.busytrack.openlivetriviainterface.USER_THUMBNAILS_PATH
import com.busytrack.openlivetriviainterface.socket.model.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.layout_clue.*
import kotlinx.android.synthetic.main.layout_header_coins.*
import kotlinx.android.synthetic.main.layout_header_players.*
import kotlinx.android.synthetic.main.layout_header_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GameFragment : BaseFragment(), GameMvp.View, CoroutineScope, GameAttemptContract {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    @Inject
    lateinit var presenter: GameMvp.Presenter

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    private var attemptsAdapter: GameAttemptsAdapter? = null

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            button_send_attempt.isEnabled = !s.isNullOrBlank() && GameState.SPLIT == presenter.getGameState()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    // Lifecycle callbacks

    override fun onAttach(context: Context) {
        activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.initSocketConnection()
    }

    override fun onStop() {
        presenter.disposeSocketConnection()
        super.onStop()
    }

    // Game Attempt contract

    override fun onAttemptClicked(attempt: String) {
        edit_text_attempt.applyText(attempt)
    }

    // Mvp Implementation

    override fun onConnecting() {
        setLoadingState(true)
    }

    override fun onConnected() {

    }

    override fun onDisconnected() {
        // TODO Show reconnect / abort dialog
        Toast.makeText(context, "onDisconnected()", Toast.LENGTH_LONG).show()
    }

    override fun updateGameState(gameStateModel: GameStateModel) {
        text_view_my_coins.setCoins(gameStateModel.userCoins)
        text_view_online_players.playersCount = gameStateModel.players
        card_view_clue.visibility = View.VISIBLE
        text_view_clue_category.text = gameStateModel.category?.also {
            text_view_clue_category.visibility = View.VISIBLE
        }
        text_view_clue_coins.setCoins(gameStateModel.currentValue)
        text_view_clue.text = gameStateModel.clue
        text_view_answer.text = gameStateModel.answer
        timed_progress_bar_remaining_time.max = gameStateModel.totalSplitSeconds
        if (GameState.SPLIT == gameStateModel.gameState) {
            timed_progress_bar_remaining_time.setProgressAndStat(gameStateModel.elapsedSplitSeconds)
        }
        gameStateModel.attempts.lastOrNull()?.let {
            if (it.correct) {
                text_view_answer.text = it.correctAnswer
                text_view_answer.correct()
            } else if (GameState.TRANSITION == gameStateModel.gameState) {
                text_view_answer.reveal()
            }
        } ?: if (GameState.TRANSITION == gameStateModel.gameState) {
            text_view_answer.reveal()
        }
        attemptsAdapter?.apply {
            initializeAttempts(gameStateModel.attempts)
            recycler_view_attempts.scrollToPosition(itemCount - 1)
        }
        button_send_attempt.isEnabled = false
        setLoadingState(false)
    }

    override fun updateRound(roundModel: RoundModel) {
        Timber.w("updateRound")
        attemptsAdapter?.clearAttempts()
        card_view_clue.visibility = View.VISIBLE
        text_view_clue_category.text = roundModel.category?.also {
            text_view_clue_category.visibility = View.VISIBLE
        }
        text_view_clue_coins.setCoins(roundModel.currentValue)
        text_view_clue.text = roundModel.clue
        text_view_answer.resetState()
        text_view_answer.text = roundModel.answer
        timed_progress_bar_remaining_time.resetAndStart()
        button_send_attempt.isEnabled = !edit_text_attempt.text.isNullOrBlank()
    }

    override fun updateSplit(splitModel: SplitModel) {
        text_view_answer.text = splitModel.answer
        text_view_clue_coins.updateValue(splitModel.currentValue, COIN_ACCELERATE_SHORT)
        coin_view_clue_coins.accelerateShort()
        timed_progress_bar_remaining_time.resetAndStart()
    }

    override fun updateAttempt(attemptModel: AttemptModel) {
        attemptsAdapter?.apply {
            addAttempt(attemptModel)
            recycler_view_attempts.apply {
                if (attemptModel.userId == authenticationManager.getAuthenticatedUser()?.userId ||
                        !canScrollVertically(1)) {
                    scrollToPosition(itemCount - 1)
                }
            }
        }
        if (attemptModel.correct) {
            text_view_clue_coins.drain()
            coin_view_clue_coins.accelerateLong()
            text_view_answer.text = attemptModel.correctAnswer
            text_view_answer.correct()
            timed_progress_bar_remaining_time.hide()
            button_send_attempt.isEnabled = false
        }
    }

    override fun updateReveal(revealModel: RevealModel) {
        text_view_answer.text = revealModel.answer
        timed_progress_bar_remaining_time.hide()
        text_view_clue_coins.drain()
        coin_view_clue_coins.accelerateLong()
        text_view_answer.reveal()
        button_send_attempt.isEnabled = false
    }

    override fun updateCoinDiff(coinDiffModel: CoinDiffModel) {
        text_view_my_coins.computeDiff(
            coinDiffModel.coinDiff,
            if (coinDiffModel.coinDiff > 1.0) COIN_ACCELERATE_LONG else COIN_ACCELERATE_SHORT
        )
        with (coin_view_my_coins) {
            if (coinDiffModel.coinDiff > 1.0) accelerateLong() else accelerateShort()
        }
    }

    override fun updatePeerJoin(presenceModel: PresenceModel) {
        text_view_online_players.incrementCount()
    }

    override fun updatePeerLeft(presenceModel: PresenceModel) {
        text_view_online_players.decrementCount()
    }

    // BaseFragment implementation

    override fun initViews() {
        text_view_my_username.isSelected = true // Enables marquee if text is constrained
        authenticationManager.getAuthenticatedUser()?.let {
            text_view_my_username.text = it.username
            Glide.with(image_view_my_profile)
                .load(USER_THUMBNAILS_PATH + it.userId + USER_THUMBNAILS_EXTENSION)
                .circleCrop()
                .into(image_view_my_profile)
        }
        recycler_view_attempts.apply {
            adapter = GameAttemptsAdapter(
                this@GameFragment,
                arrayListOf(),
                authenticationManager.getAuthenticatedUser()!!.userId
            ).also { attemptsAdapter = it }
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true // Show new messages starting from the bottom
            }
        }
    }

    override fun disposeViews() {
        text_view_my_username.text = null
        Glide.with(image_view_my_profile).clear(image_view_my_profile)
        image_view_my_profile.setImageDrawable(null)
        recycler_view_attempts.apply {
            adapter = null
            layoutManager = null
        }
    }

    override fun registerListeners() {
        ViewCompat.setOnApplyWindowInsetsListener(game_root_view) { view, insets ->
            view.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
            insets
        }
        edit_text_attempt.addTextChangedListener(textWatcher)
        button_send_attempt.setOnClickListener {
            presenter.sendAttempt(edit_text_attempt.text.trim().toString())
            edit_text_attempt.text = null
        }
        recycler_view_attempts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Show the scroll helper fab of necessary
                game_fab_scroll_down.apply {
                    if (recycler_view_attempts.canScrollVertically(1)) {
                        show()
                    } else {
                        hide()
                    }
                }
            }
        })
        game_fab_scroll_down.setOnClickListener {
            attemptsAdapter?.apply {
                recycler_view_attempts.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    override fun unregisterListeners() {
        ViewCompat.setOnApplyWindowInsetsListener(game_root_view, null)
        edit_text_attempt.removeTextChangedListener(textWatcher)
        button_send_attempt.setOnClickListener(null)
        recycler_view_attempts.clearOnScrollListeners()
        game_fab_scroll_down.setOnClickListener(null)
    }

    override fun loadData() {

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    // Private

    private fun setLoadingState(loading: Boolean) {
        game_progress_bar_main.setVisible(loading)
        game_content_root_layout.setVisible(!loading)
    }

    companion object {
        fun newInstance() = GameFragment()
    }
}

package com.busytrack.openlivetrivia.screen.game

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetrivia.extension.applyText
import com.busytrack.openlivetrivia.extension.setVisibleHard
import com.busytrack.openlivetrivia.extension.setVisibleSoft
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.generic.fragment.BaseFragment
import com.busytrack.openlivetrivia.generic.mvp.BaseMvp
import com.busytrack.openlivetrivia.rights.RightsManager
import com.busytrack.openlivetrivia.sound.SoundManager
import com.busytrack.openlivetrivia.vibration.VibrationManager
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_LONG
import com.busytrack.openlivetrivia.view.COIN_ACCELERATE_SHORT
import com.busytrack.openlivetriviainterface.BuildConfig.COST_EXTRA_ANSWER
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.layout_base_entry.*
import kotlinx.android.synthetic.main.layout_header_coins.*
import kotlinx.android.synthetic.main.layout_header_players.*
import kotlinx.android.synthetic.main.layout_header_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GameFragment : BaseFragment(), GameMvp.View, CoroutineScope, GameAttemptContract,
    PopupMenu.OnMenuItemClickListener, GamePlayerContract
{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    @Inject
    lateinit var presenter: GameMvp.Presenter

    @Inject
    lateinit var authenticationManager: AuthenticationManager
    
    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var rightsManager: RightsManager

    @Inject
    lateinit var vibrationManager: VibrationManager

    @Inject
    lateinit var soundManager: SoundManager

    @Inject
    lateinit var activityContract: ActivityContract

    private var attemptsAdapter: GameAttemptsAdapter? = null
    private var playersAdapter: GamePlayersAdapter? = null
    private var entryPopupMenu: PopupMenu? = null

    private val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {}

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

        override fun onDrawerClosed(drawerView: View) {}

        override fun onDrawerOpened(drawerView: View) {
            if (playersAdapter?.itemCount == 0) {
                game_nav_swipe_refresh_layout.isRefreshing = true
            }
            presenter.requestPlayerList()
        }
    }

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

    // Popup menu

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (R.id.menu_report_entry == item?.itemId) {
            if (presenter.isEntryReported()) {
                activityContract.showWarningMessage(R.string.game_entry_already_reported)
            } else {
                presenter.reportEntry()
            }
            return true
        }
        return false
    }

    // User item contract

    override fun onPlayerLongClicked(userModel: UserModel) {
        rightsManager.triggerRightsChange(
            userModel,
            presenter::upgradeToMod,
            presenter::downgradeToRegular
        )
    }

    // Mvp Implementation

    override fun onConnecting() {
        setSocketLoadingState(true)
    }

    override fun onConnected() {

    }

    override fun onDisconnected() {
        activityContract.showErrorMessage(R.string.message_socket_connection_lost)
        popBackStack()
    }

    override fun onConnectionError() {
        activityContract.showErrorMessage(R.string.message_socket_connection_error)
        popBackStack()
    }

    override fun updateGameState(gameStateModel: GameStateModel) {
        text_view_my_coins.setCoins(gameStateModel.userCoins)
        text_view_online_players.playersCount = gameStateModel.players
        card_view_clue.visibility = View.VISIBLE
        text_view_clue_category.text = gameStateModel.category.also {
            text_view_clue_category.setVisibleHard(it != null)
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
        setSocketLoadingState(false)
    }

    override fun updateRound(roundModel: RoundModel) {
        entryPopupMenu?.dismiss()
        attemptsAdapter?.clearAttempts()
        card_view_clue.visibility = View.VISIBLE
        text_view_clue_category.text = roundModel.category.also {
            text_view_clue_category.setVisibleHard(it != null)
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
        timed_progress_bar_remaining_time.resetAndStart()
        soundManager.split()
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
        soundManager.attempt()
        if (attemptModel.correct) {
            text_view_clue_coins.drain()
            text_view_answer.text = attemptModel.correctAnswer
            text_view_answer.correct()
            timed_progress_bar_remaining_time.hide()
            button_send_attempt.isEnabled = false
            if (attemptModel.userId == authenticationManager.getAuthenticatedUser()?.userId) {
                vibrationManager.won()
                soundManager.won()
            } else {
                soundManager.lost()
            }
        }
    }

    override fun updateReveal(revealModel: RevealModel) {
        text_view_answer.text = revealModel.answer
        timed_progress_bar_remaining_time.hide()
        text_view_clue_coins.drain()
        text_view_answer.reveal()
        button_send_attempt.isEnabled = false
        soundManager.lost()
    }

    override fun updateCoinDiff(coinDiffModel: CoinDiffModel) {
        text_view_my_coins.computeDiff(
            coinDiffModel.coinDiff,
            if (coinDiffModel.coinDiff > COST_EXTRA_ANSWER) COIN_ACCELERATE_LONG else COIN_ACCELERATE_SHORT
        )
    }

    override fun updatePeerJoin(presenceModel: PresenceModel) {
        text_view_online_players.incrementCount()
    }

    override fun updatePeerLeft(presenceModel: PresenceModel) {
        text_view_online_players.decrementCount()
    }

    override fun updatePlayerList(playerListModel: PlayerListModel) {
        playersAdapter?.initializePlayers(playerListModel)
        game_nav_swipe_refresh_layout.isRefreshing = false
    }

    override fun onUserRightsChanged() {
        refreshPlayerListIfOpen()
    }

    // BaseFragment implementation

    override fun initViews() {
        // Select text views to enable marquee if text is constrained
        text_view_my_username.isSelected = true
        text_view_clue_category.isSelected = true
        authenticationManager.getAuthenticatedUser()?.let {
            text_view_my_username.text = it.username
            Glide.with(image_view_my_profile)
                .load(UserModel.getThumbnailPath(it.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
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
        game_nav_swipe_refresh_layout.apply {
            setOnRefreshListener {
                presenter.requestPlayerList()
            }
            setColorSchemeResources(R.color.colorAccent)
        }
        game_recycler_view_players.apply {
            adapter = GamePlayersAdapter(
                this@GameFragment,
                arrayListOf()
            ).also { playersAdapter = it }
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun disposeViews() {
        text_view_my_username.text = null
        context?.let { Glide.with(it.applicationContext).clear(image_view_my_profile) }
        image_view_my_profile.setImageDrawable(null)
        recycler_view_attempts.apply {
            adapter = null
            layoutManager = null
        }
        attemptsAdapter = null
        game_recycler_view_players.apply {
            adapter = null
            layoutManager = null
        }
        playersAdapter = null
    }

    override fun registerListeners() {
        game_drawer_layout.addDrawerListener(drawerListener)
        layout_header_players.setOnClickListener {
            game_drawer_layout.openDrawer(GravityCompat.START)
        }
        menu_entry.setOnClickListener {
            showEntryMenu()
        }
        edit_text_attempt.addTextChangedListener(textWatcher)
        edit_text_attempt.setOnEditorActionListener { v, actionId, event ->
            if (!button_send_attempt.isEnabled) return@setOnEditorActionListener false
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (actionId == EditorInfo.IME_NULL && event != null &&
                        event.action == KeyEvent.ACTION_DOWN)) {
                button_send_attempt.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
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
        game_drawer_layout.removeDrawerListener(drawerListener)
        layout_header_players.setOnClickListener(null)
        menu_entry.setOnClickListener(null)
        edit_text_attempt.removeTextChangedListener(textWatcher)
        edit_text_attempt.setOnEditorActionListener(null)
        button_send_attempt.setOnClickListener(null)
        recycler_view_attempts.clearOnScrollListeners()
        game_fab_scroll_down.setOnClickListener(null)
    }

    override fun loadData() {

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun onBackPressed(): Boolean {
        // Close the Nav Drawer if it's open
        if (game_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            game_drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }
        dialogManager.showAlertDialog(
            titleResId = R.string.game_quit_confirmation_title,
            messageResId = R.string.game_quit_confirmation_message,
            positiveButtonClickListener = { _, _ ->
                popBackStack()
            }
        )
        return true
    }

    override fun getInfoBarContainer(): ViewGroup = game_root_view

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Private

    private fun setSocketLoadingState(loading: Boolean) {
        game_progress_bar_main.setVisibleSoft(loading)
        game_content_root_layout.setVisibleSoft(!loading)
    }

    private fun showEntryMenu() {
        entryPopupMenu?.dismiss()
        entryPopupMenu = PopupMenu(context, menu_entry).apply {
            setOnMenuItemClickListener(this@GameFragment)
            inflate(R.menu.menu_entry)
            show()
        }
    }

    private fun refreshPlayerListIfOpen() {
        if (game_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            presenter.requestPlayerList()
        }
    }

    companion object {
        fun newInstance() = GameFragment()
    }
}

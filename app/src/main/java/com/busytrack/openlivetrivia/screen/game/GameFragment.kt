package com.busytrack.openlivetrivia.screen.game

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
import com.busytrack.openlivetrivia.databinding.FragmentGameBinding
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GameFragment : BaseFragment<FragmentGameBinding>(), GameMvp.View, CoroutineScope, GameAttemptContract,
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
                binding.gameNavSwipeRefreshLayout.isRefreshing = true
            }
            presenter.requestPlayerList()
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.buttonSendAttempt.isEnabled = !s.isNullOrBlank() &&
                    GameState.SPLIT == presenter.getGameState()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    // Lifecycle callbacks

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
        binding.editTextAttempt.applyText(attempt)
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
        binding.layoutHeaderCoins.textViewMyCoins.setCoins(gameStateModel.userCoins)
        binding.layoutHeaderPlayers.textViewOnlinePlayers.playersCount = gameStateModel.players
        binding.cardViewClue.visibility = View.VISIBLE
        binding.layoutBaseEntry.apply {
            textViewClueCategory.text = gameStateModel.category.also {
                textViewClueCategory.setVisibleHard(it != null)
            }
            textViewClueCoins.setCoins(gameStateModel.currentValue)
            textViewClue.text = gameStateModel.clue
            textViewAnswer.text = gameStateModel.answer
            timedProgressBarRemainingTime.max = gameStateModel.totalSplitSeconds
            if (GameState.SPLIT == gameStateModel.gameState) {
                timedProgressBarRemainingTime.setProgressAndStat(gameStateModel.elapsedSplitSeconds)
            }
            gameStateModel.attempts.lastOrNull()?.let {
                if (it.correct) {
                    textViewAnswer.text = it.correctAnswer
                    textViewAnswer.correct()
                } else if (GameState.TRANSITION == gameStateModel.gameState) {
                    textViewAnswer.reveal()
                }
            } ?: run {
                if (GameState.TRANSITION == gameStateModel.gameState) {
                    textViewAnswer.reveal()
                }
            }
        }
        attemptsAdapter?.apply {
            initializeAttempts(gameStateModel.attempts)
            binding.recyclerViewAttempts.scrollToPosition(itemCount - 1)
        }
        binding.buttonSendAttempt.isEnabled = false
        setSocketLoadingState(false)
    }

    override fun updateRound(roundModel: RoundModel) {
        entryPopupMenu?.dismiss()
        attemptsAdapter?.clearAttempts()
        binding.cardViewClue.visibility = View.VISIBLE
        binding.layoutBaseEntry.apply {
            textViewClueCategory.text = roundModel.category.also {
                textViewClueCategory.setVisibleHard(it != null)
            }
            textViewClueCoins.setCoins(roundModel.currentValue)
            textViewClue.text = roundModel.clue
            textViewAnswer.resetState()
            textViewAnswer.text = roundModel.answer
            timedProgressBarRemainingTime.resetAndStart()
        }
        binding.buttonSendAttempt.isEnabled =
            !binding.editTextAttempt.text.isNullOrBlank()
    }

    override fun updateSplit(splitModel: SplitModel) {
        binding.layoutBaseEntry.apply {
            textViewAnswer.text = splitModel.answer
            textViewClueCoins.updateValue(splitModel.currentValue, COIN_ACCELERATE_SHORT)
            timedProgressBarRemainingTime.resetAndStart()
        }
        soundManager.split()
    }

    override fun updateAttempt(attemptModel: AttemptModel) {
        attemptsAdapter?.apply {
            addAttempt(attemptModel)
            binding.recyclerViewAttempts.apply {
                if (attemptModel.userId == authenticationManager.getAuthenticatedUser()?.userId ||
                        !canScrollVertically(1)) {
                    scrollToPosition(itemCount - 1)
                }
            }
        }
        soundManager.attempt()
        if (attemptModel.correct) {
            binding.layoutBaseEntry.apply {
                textViewClueCoins.drain()
                textViewAnswer.text = attemptModel.correctAnswer
                textViewAnswer.correct()
                timedProgressBarRemainingTime.hide()
            }
            binding.buttonSendAttempt.isEnabled = false
            if (attemptModel.userId == authenticationManager.getAuthenticatedUser()?.userId) {
                vibrationManager.won()
                soundManager.won()
            } else {
                soundManager.lost()
            }
        }
    }

    override fun updateReveal(revealModel: RevealModel) {
        binding.layoutBaseEntry.apply {
            textViewAnswer.text = revealModel.answer
            timedProgressBarRemainingTime.hide()
            textViewClueCoins.drain()
            textViewAnswer.reveal()
        }
        binding.buttonSendAttempt.isEnabled = false
        soundManager.lost()
    }

    override fun updateCoinDiff(coinDiffModel: CoinDiffModel) {
        binding.layoutHeaderCoins.textViewMyCoins.computeDiff(
            coinDiffModel.coinDiff,
            if (coinDiffModel.coinDiff > COST_EXTRA_ANSWER) COIN_ACCELERATE_LONG else COIN_ACCELERATE_SHORT
        )
    }

    override fun updatePeerJoin(presenceModel: PresenceModel) {
        binding.layoutHeaderPlayers.textViewOnlinePlayers.incrementCount()
    }

    override fun updatePeerLeft(presenceModel: PresenceModel) {
        binding.layoutHeaderPlayers.textViewOnlinePlayers.decrementCount()
    }

    override fun updatePlayerList(playerListModel: PlayerListModel) {
        playersAdapter?.initializePlayers(playerListModel)
        binding.gameNavSwipeRefreshLayout.isRefreshing = false
    }

    override fun onUserRightsChanged() {
        refreshPlayerListIfOpen()
    }

    // BaseFragment implementation

    override fun inflateLayout(container: ViewGroup?): FragmentGameBinding {
        return FragmentGameBinding.inflate(
            layoutInflater, container, false
        )
    }

    override fun initViews() {
        // Select text views to enable marquee if text is constrained
        binding.layoutHeaderUser.textViewMyUsername.isSelected = true
        binding.layoutBaseEntry.textViewClueCategory.isSelected = true
        authenticationManager.getAuthenticatedUser()?.let {
            binding.layoutHeaderUser.textViewMyUsername.text = it.username
            Glide.with(binding.layoutHeaderUser.imageViewMyProfile)
                .load(UserModel.getThumbnailPath(it.userId))
                .placeholder(R.drawable.ic_account_circle_accent_24dp)
                .circleCrop()
                .into(binding.layoutHeaderUser.imageViewMyProfile)
        }
        binding.recyclerViewAttempts.apply {
            adapter = GameAttemptsAdapter(
                this@GameFragment,
                arrayListOf(),
                authenticationManager.getAuthenticatedUser()!!.userId
            ).also { attemptsAdapter = it }
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true // Show new messages starting from the bottom
            }
        }
        binding.gameNavSwipeRefreshLayout.apply {
            setOnRefreshListener {
                presenter.requestPlayerList()
            }
            setColorSchemeResources(R.color.colorAccent)
        }
        binding.gameRecyclerViewPlayers.apply {
            adapter = GamePlayersAdapter(
                this@GameFragment,
                arrayListOf()
            ).also { playersAdapter = it }
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun disposeViews() {
        binding.layoutHeaderUser.textViewMyUsername.text = null
        context?.let {
            Glide.with(it.applicationContext)
                .clear(binding.layoutHeaderUser.imageViewMyProfile)
        }
        binding.layoutHeaderUser.imageViewMyProfile.setImageDrawable(null)
        binding.recyclerViewAttempts.apply {
            adapter = null
            layoutManager = null
        }
        attemptsAdapter = null
        binding.gameRecyclerViewPlayers.apply {
            adapter = null
            layoutManager = null
        }
        playersAdapter = null
    }

    override fun registerListeners() {
        binding.gameDrawerLayout.addDrawerListener(drawerListener)
        binding.layoutHeaderPlayers.root.setOnClickListener {
            binding.gameDrawerLayout.openDrawer(GravityCompat.START)
        }
        binding.layoutBaseEntry.menuEntry.setOnClickListener {
            showEntryMenu()
        }
        binding.editTextAttempt.addTextChangedListener(textWatcher)
        binding.editTextAttempt.setOnEditorActionListener { v, actionId, event ->
            if (!binding.buttonSendAttempt.isEnabled) return@setOnEditorActionListener false
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (actionId == EditorInfo.IME_NULL && event != null &&
                        event.action == KeyEvent.ACTION_DOWN)) {
                binding.buttonSendAttempt.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.buttonSendAttempt.setOnClickListener {
            presenter.sendAttempt(binding.editTextAttempt.text.trim().toString())
            binding.editTextAttempt.text = null
        }
        binding.recyclerViewAttempts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Show the scroll helper fab of necessary
                binding.gameFabScrollDown.apply {
                    if (binding.recyclerViewAttempts.canScrollVertically(1)) {
                        show()
                    } else {
                        hide()
                    }
                }
            }
        })
        binding.gameFabScrollDown.setOnClickListener {
            attemptsAdapter?.apply {
                binding.recyclerViewAttempts.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    override fun unregisterListeners() {
        binding.apply {
            gameDrawerLayout.removeDrawerListener(drawerListener)
            layoutHeaderPlayers.root.setOnClickListener(null)
            layoutBaseEntry.menuEntry.setOnClickListener(null)
            editTextAttempt.removeTextChangedListener(textWatcher)
            editTextAttempt.setOnEditorActionListener(null)
            buttonSendAttempt.setOnClickListener(null)
            recyclerViewAttempts.clearOnScrollListeners()
            gameFabScrollDown.setOnClickListener(null)
        }
    }

    override fun loadData() {

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseMvp.View> getPresenter(): BaseMvp.Presenter<T> =
        presenter as BaseMvp.Presenter<T>

    override fun onBackPressed(): Boolean {
        // Close the Nav Drawer if it's open
        if (binding.gameDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.gameDrawerLayout.closeDrawer(GravityCompat.START)
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

    override fun getInfoBarContainer(): ViewGroup = binding.gameRootView

    override fun injectDependencies() {
        (this.context as BaseActivity).activityComponent.inject(this)
    }

    // Private

    private fun setSocketLoadingState(loading: Boolean) {
        binding.gameProgressBarMain.setVisibleSoft(loading)
        binding.gameContentRootLayout.setVisibleSoft(!loading)
    }

    private fun showEntryMenu() {
        entryPopupMenu?.dismiss()
        entryPopupMenu = PopupMenu(context, binding.layoutBaseEntry.menuEntry).apply {
            setOnMenuItemClickListener(this@GameFragment)
            inflate(R.menu.menu_entry)
            show()
        }
    }

    private fun refreshPlayerListIfOpen() {
        if (binding.gameDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            presenter.requestPlayerList()
        }
    }
}

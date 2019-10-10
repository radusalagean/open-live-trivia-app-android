package com.busytrack.openlivetrivia.screen.moderatereports

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.activity.ActivityContract
import com.busytrack.openlivetrivia.generic.scheduler.TrampolineSchedulerProvider
import com.busytrack.openlivetriviainterface.rest.model.EntryReportModel
import com.busytrack.openlivetriviainterface.rest.model.MessageModel
import com.busytrack.openlivetriviainterface.rest.model.PaginatedResponseModel
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
class ModerateReportsPresenterTest {

    @Mock
    private lateinit var model: ModerateReportsMvp.Model

    @Mock
    private lateinit var view: ModerateReportsMvp.View

    @Mock
    private lateinit var activityContract: ActivityContract

    @Mock
    private lateinit var viewModel: ModerateReportsViewModel

    private lateinit var moderateReportsPresenter: ModerateReportsPresenter

    private val entryReport = EntryReportModel(
        reportId = "5d1f379a78c6e7342c49488e",
        reporters = listOf(
            UserModel(
                userId = "5d1f2968a93b8d38b87750d4",
                username = "Some user"
            )
        ),
        banned = false,
        lastReported = Date(0),
        entryId = 34770,
        category = null,
        clue = null,
        answer = null
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        moderateReportsPresenter = ModerateReportsPresenter(
            model, activityContract, TrampolineSchedulerProvider()
        )
        moderateReportsPresenter.view = view
        doReturn(viewModel).`when`(model).viewModel
    }

    @After
    fun tearDown() {
    }

    @Test
    fun should_updateView_when_refreshingReportedEntriesFlagIsChanged() {
        // When
        moderateReportsPresenter.refreshingReportedEntries = true

        // Then
        verify(view).setReportedEntriesRefreshingIndicator(eq(true))
    }

    @Test
    fun should_updateView_when_refreshingBannedEntriesFlagIsChanged() {
        // When
        moderateReportsPresenter.refreshingBannedEntries = true

        // Then
        verify(view).setBannedEntriesRefreshingIndicator(eq(true))
    }

    @Test
    fun should_updateView_when_loadingMoreReportedEntriesFlagIsChanged() {
        // When
        moderateReportsPresenter.loadingMoreReportedEntries = true

        // Then
        verify(view).updateReportedEntriesLoadMoreState(eq(true))
    }

    @Test
    fun should_updateView_when_loadingMoreBannedEntriesFlagIsChanged() {
        // When
        moderateReportsPresenter.loadingMoreBannedEntries = true

        // Then
        verify(view).updateBannedEntriesLoadMoreState(eq(true))
    }

    @Test
    fun should_clearReportedEntriesFromViewModel_when_requestReportedEntriesWithInvalidateOption() {
        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).initReportedEntries()

        // When
        moderateReportsPresenter.requestReportedEntries(true)

        // Then
        verify(viewModel).clearReported()
    }

    @Test
    fun should_clearBannedEntriesFromViewModel_when_requestReportedEntriesWithInvalidateOption() {
        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).initBannedEntries()

        // When
        moderateReportsPresenter.requestBannedEntries(true)

        // Then
        verify(viewModel).clearBanned()
    }

    @Test
    fun should_updateViewWithCachedEntries_when_requestReportedEntries_with_availableEntriesInViewModel() {
        // Given
        val entries = mock<ArrayList<EntryReportModel>>()
        doReturn(entries).`when`(viewModel).reportedEntries

        // When
        moderateReportsPresenter.requestReportedEntries()

        // Then
        verify(view).updateReportedEntries(entries)
        verify(model, never()).initReportedEntries()
    }

    @Test
    fun should_updateViewWithCachedEntries_when_requestBannedEntries_with_availableEntriesInViewModel() {
        // Given
        val entries = mock<ArrayList<EntryReportModel>>()
        doReturn(entries).`when`(viewModel).bannedEntries

        // When
        moderateReportsPresenter.requestBannedEntries()

        // Then
        verify(view).updateBannedEntries(entries)
        verify(model, never()).initBannedEntries()
    }

    @Test
    fun should_updateViewWithEntries_when_requestReportedEntriesSucceeds() {
        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).initReportedEntries()

        // When
        moderateReportsPresenter.requestReportedEntries()

        // Then
        verify(view).updateReportedEntries(any())
    }

    @Test
    fun should_updateViewWithEntries_when_requestBannedEntriesSucceeds() {
        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).initBannedEntries()

        // When
        moderateReportsPresenter.requestBannedEntries()

        // Then
        verify(view).updateBannedEntries(any())
    }

    @Test
    fun should_showErrorMessage_when_requestReportedEntriesFails() {
        // Given
        doReturn(Observable.error<PaginatedResponseModel<*>>(Throwable()))
            .`when`(model).initReportedEntries()

        // When
        moderateReportsPresenter.requestReportedEntries()

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }

    @Test
    fun should_showErrorMessage_when_requestBannedEntriesFails() {
        // Given
        doReturn(Observable.error<PaginatedResponseModel<*>>(Throwable()))
            .`when`(model).initBannedEntries()

        // When
        moderateReportsPresenter.requestBannedEntries()

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }

    @Test
    fun should_ignoreRequest_when_onReportedEntriesScrollThresholdReachedAndIsCurrentlyLoadingContent() {
        // Given
        // Subscribe on Schedulers.io() and place a delay for the first call
        // This will be enough so when the second attempt is reached, the first one is still
        //  processing and there should be only one call to model.getNextReportedEntriesPage(),
        //  because the second attempt should be ignored when the first one is still processing
        doReturn(
            Observable.just(mock<PaginatedResponseModel<EntryReportModel>>())
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
        ).`when`(model).getNextReportedEntriesPage()
        doReturn(2).`when`(viewModel).nextAvailablePageReportedEntries

        // When
        moderateReportsPresenter.onReportedEntriesScrollThresholdReached() // Attempt 1

        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).getNextReportedEntriesPage()

        // When
        moderateReportsPresenter.onReportedEntriesScrollThresholdReached() // Attempt 2

        // Then
        verify(model).getNextReportedEntriesPage()
    }

    @Test
    fun should_ignoreRequest_when_onBannedEntriesScrollThresholdReachedAndIsCurrentlyLoadingContent() {
        // Given
        // Subscribe on Schedulers.io() and place a delay for the first call
        // This will be enough so when the second attempt is reached, the first one is still
        //  processing and there should be only one call to model.getNextBannedEntriesPage(),
        //  because the second attempt should be ignored when the first one is still processing
        doReturn(
            Observable.just(mock<PaginatedResponseModel<EntryReportModel>>())
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
        ).`when`(model).getNextBannedEntriesPage()
        doReturn(2).`when`(viewModel).nextAvailablePageBannedEntries

        // When
        moderateReportsPresenter.onBannedEntriesScrollThresholdReached() // Attempt 1

        // Given
        doReturn(Observable.just(mock<PaginatedResponseModel<EntryReportModel>>()))
            .`when`(model).getNextBannedEntriesPage()

        // When
        moderateReportsPresenter.onBannedEntriesScrollThresholdReached() // Attempt 2

        // Then
        verify(model).getNextBannedEntriesPage()
    }

    @Test
    fun should_ignoreRequest_when_onReportedEntriesScrollThresholdReachedAndNextPageIsNotAvailable() {
        // Given
        doReturn(null).`when`(viewModel).nextAvailablePageReportedEntries

        // When
        moderateReportsPresenter.onReportedEntriesScrollThresholdReached()

        // Then
        verify(model, never()).getNextReportedEntriesPage() // No request should be made
    }

    @Test
    fun should_ignoreRequest_when_onBannedEntriesScrollThresholdReachedAndNextPageIsNotAvailable() {
        // Given
        doReturn(null).`when`(viewModel).nextAvailablePageBannedEntries

        // When
        moderateReportsPresenter.onBannedEntriesScrollThresholdReached()

        // Then
        verify(model, never()).getNextBannedEntriesPage() // No request should be made
    }

    /**
     * Prepare given successful states for BAN, UNBAN, DISMISS
     */
    private fun prepareSuccessActionStates() {
        // Return Successful observables for action states
        doReturn(Observable.just(MessageModel(""))).`when`(model).banEntry(any())
        doReturn(Observable.just(MessageModel(""))).`when`(model).unbanEntry(any())
        doReturn(Observable.just(MessageModel(""))).`when`(model).dismissReport(any())
        // Return errors for model requests, as we do not care about that behavior here
        doReturn(Observable.error<PaginatedResponseModel<*>>(Throwable()))
            .`when`(model).initReportedEntries()
        doReturn(Observable.error<PaginatedResponseModel<*>>(Throwable()))
            .`when`(model).initBannedEntries()
    }

    /**
     * Prepare given fail states for BAN, UNBAN, DISMISS
     */
    private fun prepareFailedActionStates() {
        // Return errors for action states
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).banEntry(any())
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).unbanEntry(any())
        doReturn(Observable.error<MessageModel>(Throwable())).`when`(model).dismissReport(any())
    }

    /**
     * Verify if all lists are re-initialized
     */
    private fun verifyInitAllLists() {
        verify(viewModel).clearReported()
        verify(viewModel).clearBanned()
        verify(model).initReportedEntries()
        verify(model).initBannedEntries()
    }

    @Test
    fun should_initAllListsAndShowInfoMessage_when_banEntrySucceeds() {
        // Given
        prepareSuccessActionStates()

        // When
        moderateReportsPresenter.banEntry(entryReport)

        // Then
        verifyInitAllLists()
        verify(activityContract).showInfoMessage(eq(R.string.message_entry_banned))
    }

    @Test
    fun should_initAllListsAndShowInfoMessage_when_unbanEntrySucceeds() {
        // Given
        prepareSuccessActionStates()

        // When
        moderateReportsPresenter.unbanEntry(entryReport)

        // Then
        verifyInitAllLists()
        verify(activityContract).showInfoMessage(eq(R.string.message_entry_unbanned))
    }

    @Test
    fun should_initAllListsAndShowInfoMessage_when_dismissReportSucceeds() {
        // Given
        prepareSuccessActionStates()

        // When
        moderateReportsPresenter.dismissReport(entryReport)

        // Then
        verifyInitAllLists()
        verify(activityContract).showInfoMessage(eq(R.string.message_report_dismissed))
    }

    @Test
    fun should_showErrorMessage_when_banEntryFails() {
        // Given
        prepareFailedActionStates()

        // When
        moderateReportsPresenter.banEntry(entryReport)

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }

    @Test
    fun should_showErrorMessage_when_unbanEntryFails() {
        // Given
        prepareFailedActionStates()

        // When
        moderateReportsPresenter.unbanEntry(entryReport)

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }

    @Test
    fun should_showErrorMessage_when_dismissReportFails() {
        // Given
        prepareFailedActionStates()

        // When
        moderateReportsPresenter.dismissReport(entryReport)

        // Then
        verify(activityContract).showErrorMessage(eq(R.string.general_error_message), anyOrNull())
    }
}
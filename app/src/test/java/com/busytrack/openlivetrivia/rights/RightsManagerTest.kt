package com.busytrack.openlivetrivia.rights

import android.content.DialogInterface
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class RightsManagerTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var dialogManager: DialogManager
    private lateinit var dialogContract: DialogContract

    private lateinit var rightsManager: RightsManager

    @Before
    fun setUp() {
        rightsManager = RightsManager(
            ApplicationProvider.getApplicationContext(),
            mock(AuthenticationManager::class.java).also { authenticationManager = it },
            mock(DialogManager::class.java).also { dialogManager = it }
        )
        dialogContract = mock(DialogContract::class.java)
    }

    @Test
    fun regularUserAuthenticated_triggerRightsChange_nothingHappens() {
        doReturn(getUserModelMock(UserRightsLevel.REGULAR))
            .`when`(authenticationManager).getAuthenticatedUser()

        rightsManager.triggerRightsChange(
            getUserModelMock(UserRightsLevel.REGULAR),
            dialogContract::upgradeToModCallback,
            dialogContract::downgradeToRegularCallback
        )

        verifyZeroInteractions(dialogManager)
    }

    @Test
    fun moderatorAuthenticated_triggerRightsChange_nothingHappens() {
        doReturn(getUserModelMock(UserRightsLevel.MOD))
            .`when`(authenticationManager).getAuthenticatedUser()

        rightsManager.triggerRightsChange(
            getUserModelMock(UserRightsLevel.REGULAR),
            dialogContract::upgradeToModCallback,
            dialogContract::downgradeToRegularCallback
        )

        verifyZeroInteractions(dialogManager)
    }

    @Test
    fun adminAuthenticated_targetAdmin_triggerRightsChange_nothingHappens() {
        doReturn(getUserModelMock(UserRightsLevel.ADMIN))
            .`when`(authenticationManager).getAuthenticatedUser()

        rightsManager.triggerRightsChange(
            getUserModelMock(UserRightsLevel.ADMIN),
            dialogContract::upgradeToModCallback,
            dialogContract::downgradeToRegularCallback
        )

        verifyZeroInteractions(dialogManager)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun adminAuthenticated_targetModerator_triggerRightsChange_downgradeToRegular() {
        doReturn(getUserModelMock(UserRightsLevel.ADMIN))
            .`when`(authenticationManager).getAuthenticatedUser()
        // Short-circuit the DialogManager method call
        `when`(dialogManager.showListAlertDialog(
            com.nhaarman.mockitokotlin2.any(),
            com.nhaarman.mockitokotlin2.any(),
            com.nhaarman.mockitokotlin2.any()
        )).then {
            (it.arguments[2] as (DialogInterface, Int) -> Unit).invoke(
                mock(DialogInterface::class.java),
                0 // Position of the option in the list
            )
        }

        rightsManager.triggerRightsChange(
            getUserModelMock(UserRightsLevel.MOD),
            dialogContract::upgradeToModCallback,
            dialogContract::downgradeToRegularCallback
        )

        verify(dialogContract).downgradeToRegularCallback(com.nhaarman.mockitokotlin2.any())
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun adminAuthenticated_targetRegularUser_triggerRightsChange_upgradeToMod() {
        doReturn(getUserModelMock(UserRightsLevel.ADMIN))
            .`when`(authenticationManager).getAuthenticatedUser()
        // Short-circuit the DialogManager method call
        `when`(dialogManager.showListAlertDialog(
            com.nhaarman.mockitokotlin2.any(),
            com.nhaarman.mockitokotlin2.any(),
            com.nhaarman.mockitokotlin2.any()
        )).then {
            (it.arguments[2] as (DialogInterface, Int) -> Unit).invoke(
                mock(DialogInterface::class.java),
                0 // Position of the option in the list
            )
        }

        rightsManager.triggerRightsChange(
            getUserModelMock(UserRightsLevel.REGULAR),
            dialogContract::upgradeToModCallback,
            dialogContract::downgradeToRegularCallback
        )

        verify(dialogContract).upgradeToModCallback(com.nhaarman.mockitokotlin2.any())
    }

    private fun getUserModelMock(rights: UserRightsLevel) =
        UserModel(
            "id", "username", rights,
            null, null, false, null
        )

    interface DialogContract {
        fun upgradeToModCallback(userModel: UserModel)
        fun downgradeToRegularCallback(userModel: UserModel)
    }
}
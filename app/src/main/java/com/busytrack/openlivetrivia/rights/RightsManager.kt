package com.busytrack.openlivetrivia.rights

import android.content.Context
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.auth.AuthenticationManager
import com.busytrack.openlivetrivia.dialog.DialogManager
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel

class RightsManager(
    private val context: Context,
    private val authenticationManager: AuthenticationManager,
    private val dialogManager: DialogManager
) {

    /**
     * Called when the authenticated ADMIN successfully changes the rights level of another user
     */
    fun triggerRightsChange(
        userModel: UserModel,
        upgradeToModCallback: (userModel: UserModel) -> Unit,
        downgradeToRegularCallback: (userModel: UserModel) -> Unit
    ) {
        if (authenticationManager.getAuthenticatedUser()?.rights == UserRightsLevel.ADMIN &&
            userModel.rights != UserRightsLevel.ADMIN) {
            val rightsChangeString = context.getString(
                if (userModel.rights == UserRightsLevel.REGULAR) {
                    R.string.user_action_dialog_option_upgrade_to_moderator
                } else {
                    R.string.user_action_dialog_option_downgrade_to_regular_user
                }
            )
            val items = arrayOf(rightsChangeString)
            dialogManager.showListAlertDialog(
                R.string.user_action_dialog_title,
                items
            ) { _, which ->
                when(items[which]) {
                    rightsChangeString -> {
                        if (userModel.rights == UserRightsLevel.REGULAR) {
                            upgradeToModCallback(userModel)
                        } else {
                            downgradeToRegularCallback(userModel)
                        }
                    }
                }
            }
        }
    }
}
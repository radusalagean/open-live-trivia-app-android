package com.busytrack.openlivetriviainterface.socket.event

internal enum class SocketOutgoingEvent(
    val lowerCaseEvent: Boolean = false
) {

    AUTHENTICATION(true),
    ATTEMPT,
    REACTION,
    REPORT_ENTRY,
    REQUEST_PLAYER_LIST;

    val eventName: String
        get() {
            return if (lowerCaseEvent) name.toLowerCase() else name
        }
}
package vijay.app.dona.utils

import vijay.app.dona.R

class Constants {

    companion object {
        val APP_NAME = "DONA"

        enum class TaskCategory(val category: String) {
            TODAY("today"),
            COMPLETED("completed"),
            REPEAT("repeat")
        }

        const val SHARED_PREF = "com.vijay.dona.shared.preference"
        const val STRIKE_THROUGH_SPEED = "com.dona.strikeThroughSpeed"
        const val STRIKE_THROUGH_TEXT_SIZE = "com.dona.strikeThroughTextSize"
        const val STRIKE_THROUGH_LINE_WIDTH = "com.dona.strikeThroughLineWidth"
        const val DEFAULT_COLOR = "com.dona.circleDefaultColor"
        const val NOTIFICATION_ENABLE = "com.dona.notification"
        const val NOTIFICATION_CHANNEL_TAG = "Dona app alert"
        const val NOTIFICATION_CHANNEL_ID = "Dona app"

        const val CURRENT_DATE = "com.dona.date"

        var SETTING_TEXT_SIZE = 16F
        var SETTING_STRIKE_THROUGH_SPEED = 550L
        var SETTING_STRIKE_THROUGH_WIDTH = 4F
        var SETTING_DEFAULT_COLOUR = R.color.transparent_line


    }
}
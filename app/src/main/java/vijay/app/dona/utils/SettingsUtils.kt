package vijay.app.dona.utils


/*
*   This class will do all the calculation of finding the right
*       textSize, strike through Speed etc.
*
* */
class SettingsUtils {

    companion object {

        fun getTextSize(value: Int) : Float {
            val minimumValue = 14
            return (minimumValue + value).toFloat()
        }

        fun getStrikeThroughWidth(value: Int) : Float {
            val minimumWidth = 2
            return (minimumWidth + (value / 2)).toFloat()
        }

        fun getStrikeThroughSpeed(value: Int) : Long {
            val minimumTime = 1500
            return (minimumTime - (value * 101)).toLong()
        }

    }

}
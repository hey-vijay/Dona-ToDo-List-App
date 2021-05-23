package vijay.app.dona.utils

import android.content.Context
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object {

        private val TAG = TimeUtil::class.java.name

        val DATE_FORMAT = "dd-MM-yyyy"
        val EASY_DATE_FORMAT = "E, dd MMM yy"   //Tue, 02 Jan 18
        val TIME_FORMAT = "dd-MM-yyyy HH:mm a"	//02-01-2018 06:07 AM     this is 0-23 hour clock

        fun getCurrentDate() : String {
            val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val currentDate = Calendar.getInstance().time
            return simpleDateFormat.format(currentDate)
        }

        fun getEasyDateFormat(date: String) : String {
            if(date.isEmpty()) return ""
            try {
                val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                val newDate = formatter.parse(date) ?: return ""
                val newDateFormat = SimpleDateFormat(EASY_DATE_FORMAT, Locale.getDefault())
                return newDateFormat.format(newDate)
            } catch (e : Exception) {
                Log.d(TAG, "getEasyDateFormat: unable to parse: $date")
                return ""
            }
        }

        fun isDateChanged(context: Context): Boolean {
            val lastDate =
                SmartPreferences.getInstance(context).getValue(Constants.CURRENT_DATE, "")
            val currentDate = TimeUtil.getCurrentDate()
            Log.d(TAG, "dateChanged: lastDate $lastDate")
            Log.d(TAG, "dateChanged: currentDate $currentDate")
            if(lastDate != currentDate) {
                SmartPreferences.getInstance(context).saveValue(Constants.CURRENT_DATE, currentDate)
            }
            return lastDate != currentDate
        }


        /*
        * @params notifiedTime is the time at which first notification should arrive every day
        * this function return the time difference in Minutes between current time and notifiedTime
        *   so work manager can be delayed for this much amount.
        * */
        fun getNotificationDelayTime(notifiedTime: String) : Long {
            val TIME_FORMAT = "dd-MM-yyyy HH:mm"
            val DATE_FORMAT = "dd-MM-yyyy"
            val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val date: Date
            val c = Calendar.getInstance()
            val currentTime: Date = c.time
            c.add(Calendar.DATE, 1) // increase the day +1
            date = c.time // date
            val tomorrowDate = formatter.format(date)
            val notificationTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).parse("$tomorrowDate $notifiedTime") ?: return -1
            val diff: Long = notificationTime.time - currentTime.time
            val seconds = diff / 1000;
            val minutes = seconds / 60;
            Log.d(TAG, "getNotificationDelayTime: minutes ${minutes}\n")
            return minutes
        }

    }
}
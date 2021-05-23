package vijay.app.dona.utils

import android.content.Context
import android.content.SharedPreferences

class SmartPreferences private constructor(context:Context){

    init {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
    }

    companion object {
        private var smartPreferences: SmartPreferences? = null
        private lateinit var sharedPreferences: SharedPreferences

        fun getInstance(context: Context) : SmartPreferences {
            if(smartPreferences == null) {
                smartPreferences = SmartPreferences(context)
            }
            return smartPreferences as SmartPreferences
        }
    }

    fun saveValue(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun saveValue(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun saveValue(key: String, value: Int) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }
    fun saveValue(key: String, value: Float) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        prefsEditor.putFloat(key, value)
        prefsEditor.apply()
    }

    fun getValue(key:String, defaultValue: String) : String {
        return sharedPreferences.getString(key, defaultValue).toString()
    }

    fun getValue(key:String, defaultValue: Boolean) : Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Int) : Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Float) : Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

}
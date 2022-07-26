package app.com.mimiclejp.common.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class AppPreference(context: Context) {
    private val preference: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private val MAIN_URL = "mainurl"
    private val MEM_NO = "memno"
    private val LANDING_URL = "landingurl"
    private val NOTI_TYPE = "notitype"
    private val ADID = "adid"
    private val PUSH_TOKEN = "pushtoken"

    private operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> preference.edit().putString(key, value).apply()
            is Int -> preference.edit().putInt(key, value).apply()
            is Boolean -> preference.edit().putBoolean(key, value).apply()
            is Float -> preference.edit().putFloat(key, value).apply()
            is Long -> preference.edit().putLong(key, value).apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private operator inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preference.getString(key, defaultValue as? String) as T?
            Int::class -> preference.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preference.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preference.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preference.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun getMainUrl(): String? {
        return get(MAIN_URL)
    }

    fun setMainUrl(url: String) {
        set(MAIN_URL, url)
    }

    fun getMemNo(): String? {
        return get(MEM_NO)
    }

    fun setMemNo(memno: String) {
        set(MEM_NO, memno)
    }

    fun getLandingUrl(): String? {
        return get(LANDING_URL)
    }

    fun setLandingUrl(landingUrl: String) {
        set(LANDING_URL, landingUrl)
    }

    fun getNotiType(): String? {
        return get(NOTI_TYPE)
    }

    fun setNotiType(notiType: String) {
        set(NOTI_TYPE, notiType)
    }

    fun getAdid(): String? {
        return get(ADID)
    }

    fun setAdid(adid: String) {
        set(ADID, adid)
    }

    fun getPushToken(): String? {
        return get(PUSH_TOKEN)
    }

    fun setPushToken(pushToken: String) {
        set(PUSH_TOKEN, pushToken)
    }
}
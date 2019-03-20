package ae.bluecast.library.Utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {

    val PREF_FILE = "BOOKSHELF_APP"
    val USER_NAME = "USER_NAME"

    val IS_FIRST_TIME = "ae.gov.dsc.Utils.isFirstTime"
    val COLOR_CODE = "COLOR_CODE"

    fun edit(context: Context): SharedPreferences.Editor {
        return get(context).edit()
    }

    operator fun get(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
    }


    fun isFirstTime(context: Context): Boolean {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).getBoolean(IS_FIRST_TIME, true)
    }

    fun getColor(context: Context): String? {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).getString(IS_FIRST_TIME, "")
    }

    fun setColorCode(context: Context, colorCode: String) {
        val pref = edit(context)
        pref.putString(COLOR_CODE, colorCode)
        pref.apply()
    }

    fun disableFirstTime(context: Context) {
        val pref = edit(context)
        pref.putBoolean(IS_FIRST_TIME, false)
        pref.apply()
    }


    //===========ADDING VALUE==================
    //    SharedPreferences.Editor PREFE = PreferenceUtil.edit(StepService.this);
    //                PREFE.putInt(PreferenceUtil.PREF_ADD, 0);
    //                PREFE.apply();
}

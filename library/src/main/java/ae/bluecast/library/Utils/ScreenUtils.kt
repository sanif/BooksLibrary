package ae.bluecast.library.Utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

object ScreenUtils {

    fun getScreenRatioByValue(context: Context, itemWidth: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / itemWidth).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun dpTopx(context: Context, value: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            r.displayMetrics
        )
        return px.toInt()
    }

    @JvmStatic
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }
}

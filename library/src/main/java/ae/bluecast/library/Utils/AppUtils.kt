package ae.bluecast.library.Utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable

object AppUtils {

    fun getIcon(context: Context, drawable: Drawable): Drawable {

        val color = PreferenceUtils[context].getString(PreferenceUtils.COLOR_CODE, "")
        drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun getIcon(context: Context, drawable: Int): Drawable {

        val cDrawable = context.resources.getDrawable(drawable)
        val color = PreferenceUtils[context].getString(PreferenceUtils.COLOR_CODE, "")
        cDrawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
        return cDrawable
    }

    fun getColor(context: Context): Int {
        val color = PreferenceUtils[context].getString(PreferenceUtils.COLOR_CODE, "")
        return Color.parseColor(color)

    }
}
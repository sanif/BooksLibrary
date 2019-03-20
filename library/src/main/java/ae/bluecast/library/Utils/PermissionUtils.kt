package ae.bluecast.library.Utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

object PermissionUtils {

    fun hasPermissions(context: Activity?, askForPermission: Boolean, permissions: Array<String>): Boolean {
        var i = 0
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    i++
                }
            }
        }
        if (i == 0) {
            return true
        } else {
            if (askForPermission)
                ActivityCompat.requestPermissions(context!!, permissions, 99)
            return false

        }
    }

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        var i = 0
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    i++
                }
            }
        }
        return i == 0
    }
}

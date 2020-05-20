@file:Suppress("DEPRECATION")
package woogear.kwon.gccproject.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.ui.activity.PlaceInfoActivity
import woogear.kwon.gccproject.utils.Constants.EXTRA_PLACE_IS_SAVED
import woogear.kwon.gccproject.utils.Constants.EXTRA_PLACE_OBJECT

object CommonUtils {
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun isNetworkAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // VERSION_CODES.M = API 23
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            capabilities ?: return false

            result = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    fun callPlaceInfoActivity(place: Place?, isToSaved: Boolean, activity: Activity) {
        place?.let {
            val intent = Intent(activity, PlaceInfoActivity::class.java)
            intent.putExtra(EXTRA_PLACE_OBJECT, it)
            intent.putExtra(EXTRA_PLACE_IS_SAVED, isToSaved)
            activity.startActivity(intent)
        }
    }
}
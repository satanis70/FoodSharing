package familyapp.foodsharing

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog

class LoadingDialog(val activity: Activity) {
    var dialog: AlertDialog? = null
    fun startLoading(){
        val bilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        bilder.setView(inflater.inflate(R.layout.login_loading_dialog, null))
        bilder.setCancelable(false)
        dialog = bilder.create()
        dialog?.show()
    }

    fun dissmissDialog(){
        dialog?.dismiss()
    }

}
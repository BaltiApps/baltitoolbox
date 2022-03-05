package balti.module.baltitoolbox

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntDef

@SuppressLint("StaticFieldLeak")
object ToolboxHQ {

    internal lateinit var context: Context
    internal lateinit var application: Application
    internal lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context){
        this.context = context.applicationContext
        this.application = context.applicationContext as Application
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    val FileHandlers by lazy { balti.module.baltitoolbox.functions.FileHandlers }
    val GetResources by lazy { balti.module.baltitoolbox.functions.GetResources }
    val Misc by lazy { balti.module.baltitoolbox.functions.Misc }
    val AndroidUI by lazy { balti.module.baltitoolbox.functions.AndroidUI }
    val SharedPrefs by lazy { balti.module.baltitoolbox.functions.SharedPrefs }

    @SuppressLint("InlinedApi")
    @IntDef(
        NotificationManager.IMPORTANCE_MAX,
        NotificationManager.IMPORTANCE_HIGH,
        NotificationManager.IMPORTANCE_DEFAULT,
        NotificationManager.IMPORTANCE_LOW,
        NotificationManager.IMPORTANCE_MIN,
        NotificationManager.IMPORTANCE_NONE,
        NotificationManager.IMPORTANCE_UNSPECIFIED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NotificationImportance

}
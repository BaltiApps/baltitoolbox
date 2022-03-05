package balti.module.baltitoolbox.functions

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import balti.module.baltitoolbox.R
import balti.module.baltitoolbox.ToolboxHQ
import balti.module.baltitoolbox.functions.GetResources.getStringFromRes
import balti.module.baltitoolbox.jobHandlers.AsyncCoroutineTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.util.*

object Misc {

    private val application = ToolboxHQ.application

    /**
     * Run a function / block of code, surrounded by try-catch.
     * Returns result of the function if it executed successfully.
     *
     * Show an error dialog using [AndroidUI.showErrorDialog] if there was an error in the code block.
     * Returns the exception.
     *
     * @param f The code block to execute
     * @param showError If `true` then error dialog is shown `false` by default.
     * In any case, if there is an exception, it is returned.
     */
    fun tryIt(f: () -> Any?, showError: Boolean = false): Any? {
        return try { f() } catch (e: Exception) {
            if (showError) AndroidUI.showErrorDialog(e.message.toString())
            e
        }
    }

    /**
     * Run a block of code inside a try-catch block, returning result of the execution.
     * Ignore any error that might arise.
     */
    fun tryIt(f: () -> Any?): Any? = tryIt(f, false)

    /**
     * Run a function / block of code, surrounded by try-catch without returning any result.
     *
     * Show an error dialog using [AndroidUI.showErrorDialog] if there was an error in the code block.
     *
     * @param f The code block to execute
     * @param showError If `true` then error dialog is shown `false` by default.
     * The exception is not returned.
     */
    fun tryIt(f: () -> Unit, showError: Boolean = false) {
        try { f() } catch (e: Exception) {
            if (showError) AndroidUI.showErrorDialog(e.message.toString())
        }
    }

    /**
     * Run a block of code inside a try-catch block, does not return any result.
     * Ignore any error that might arise.
     */
    fun tryIt(f: () -> Unit) = tryIt(f, false)

    /**
     * Check if a [packageName] is installed on the device.
     */
    fun isPackageInstalled(packageName: String): Boolean{
        return try {
            application.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            true
        }
        catch (_: Exception){ false }
    }

    /**
     * Get the app name from a given [packageName].
     */
    fun getAppName(packageName: String): String {
        return if (isPackageInstalled(packageName)){
            application.packageManager.getApplicationLabel(
                    application.packageManager.getPackageInfo(
                            packageName,
                            PackageManager.GET_META_DATA
                    ).applicationInfo
            ).toString()
        }
        else ""
    }

    /**
     * Take a file size in long and convert it to a String in KB / MB / GB / TB.
     * Example if [fileSize] = 3453262
     * Returned is : "3.29 MB"
     *
     * @param fileSize File size in Bytes or in KB.
     * @param isSpaceInKB By default is `false`, set it to `true` if [fileSize] is in KB.
     * @param spaceBeforeUnit By default is `true`,
     * set to `false` to remove space between numeric value and unit.
     */
    fun getHumanReadableStorageSpace(
        fileSize: Long,
        isSpaceInKB: Boolean = false,
        spaceBeforeUnit: Boolean = true
    ): String {

        var unit = if (isSpaceInKB) "KB" else "B"

        var s = fileSize.toDouble()

        fun divide(annotation: String){
            if (s > 1024) {
                s /= 1024.0
                unit = annotation
            }
        }

        if (!isSpaceInKB) divide("KB")
        divide("MB")
        divide("GB")
        divide("TB")

        return String.format("%.2f", s) + (if (spaceBeforeUnit) " " else "") + unit
    }

    /**
     * Fire an intent to open a web page [url] in a browser.
     * If [url] is blank, does nothing.
     */
    fun openWebLink(url: String) {
        if (url != "") {
            application.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    /**
     * Takes a [packageName] and opens the Google Play Store page for the app.
     */
    fun playStoreLink(packageName: String){
        openWebLink("market://details?id=$packageName")
    }

    /**
     * Create a notification channel.
     * Inbuilt check - Runs only if current android version is above oreo.
     * Hence can be called in code anywhere, if android version is Nougat or below,
     * the method will simply not do anything.
     *
     * @param channelId String ID for the channel.
     * @param channelName String name of the channel.
     * @param importance Int notification channel importance.
     * @param channelDesc Optional String passed to [NotificationChannel.setDescription].
     * @param groupId Optional String group id passed to [NotificationChannel.setGroup].
     * @param silentChannel Optional Boolean, by default `true`.
     * If passed as false, notifications make sound.
     * @param customizingFunction Optional function in which the notification channel
     * can be further customised before creation.
     */
    fun makeNotificationChannel(
        channelId: String,
        channelName: String,
        @ToolboxHQ.NotificationImportance importance: Int,
        channelDesc: String? = null,
        groupId: String? = null,
        silentChannel: Boolean = true,
        customizingFunction: ((NotificationChannel) -> Unit)? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                channelDesc?.let { description = it }
                groupId?.let { group = it }
                if (silentChannel) setSound(null, null)
            }
            customizingFunction?.invoke(channel)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Keep reading from a [BufferedReader] as long as it does not reach the end (exhausted)
     * or [loopFunction] does not return `true`.
     *
     * @param reader BufferedReader instance to read from.
     * @param loopFunction Function which takes a String line read from the buffer,
     * one line at a time and does something.
     * Must return a Boolean. If it is `true` then the buffer is no longer read.
     * @param onManualBreakFunction Optional function to be executed if [loopFunction] returns `true`.
     * Will not be executed if the buffer is naturally read and exhausted.
     */
    fun iterateBufferedReader(reader: BufferedReader, loopFunction: (line: String) -> Boolean,
                              onManualBreakFunction: (() -> Unit)? = null){
        var doBreak = false
        while (true){
            val line : String? = reader.readLine()
            if (line == null) break
            else {
                doBreak = loopFunction(line.trim())
                if (doBreak) break
            }
        }
        if (doBreak) onManualBreakFunction?.invoke()
    }

    /**
     * Perform a small background job. Uses [AsyncCoroutineTask].
     *
     * @param job The function / block of code to run in background.
     * @param postJob The function to run after the [job]. This is run in main thread.
     * It accepts the result / return from [job] as function argument.
     */
    fun doBackgroundTask(job: () -> Any?, postJob: (result: Any?) -> Any?){
        class Class : AsyncCoroutineTask(){
            override suspend fun doInBackground(arg: Any?): Any? {
                return job()
            }

            override suspend fun onPostExecute(result: Any?) {
                super.onPostExecute(result)
                postJob(result)
            }
        }
        Class().execute()
    }

    fun runSuspendFunction(f: suspend () -> Unit){
        CoroutineScope(Default).launch {
            f()
        }
    }

    fun runOnMainThread(f: () -> Unit) {
        CoroutineScope(Main).launch{
            f()
        }
    }

    /**
     * Get current time in milliseconds.
     */
    fun timeInMillis() = Calendar.getInstance().timeInMillis

    /**
     * Get a number ([count]) as a percentage of a [total] number.
     * Has inbuilt check if [total] is zero.
     */
    fun getPercentage(count: Int, total: Int): Int {
        return if (total != 0) (count*100)/total
        else 0
    }

    /**
     * LiveData extension function to fire off an observer only once for the first event
     * and ignore all other events.
     *
     * @param owner Lifecycle owner, basically Activity, Fragment, Service, ViewModels etc.
     * @param observer The observer to receive the event.
     * @param validationFunction Optional function. Here we can check the event
     * and decide that maybe the event was not what we wanted and return `false`.
     * In that case, even though an event has been fired once, the observer
     * will continue getting the subsequent events as long as this function does not return `true`.
     * Once [validationFunction] returns `true`, all further events will not be observed.
     */
    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>, validationFunction: ((value: T) -> Boolean)? = null) {
        observe(owner, object : Observer<T> {
            override fun onChanged(t: T) {
                if (validationFunction == null || validationFunction(t)) {
                    observer.onChanged(t)
                    removeObserver(this)
                }
            }
        })
    }

    /**
     * Start an activity from an intent.
     * Same as [Context.startActivity].
     *
     * @param packageContext Context of Activity or Service or
     * anything else from where the activity is to be started.
     * @param activityIntent The intent that is to be started.
     */
    fun activityStart(packageContext: Context, activityIntent: Intent) {
        packageContext.startActivity(activityIntent)
    }

    /**
     * Start an activity to the given class.
     *
     * Similar to `startActivity(Intent(context, MyActivity::class.java))`
     *
     * @param packageContext Context of Activity or Service or
     * anything else from where the activity is to be started.
     * @param cls Activity class which should be started.
     * @param extras Bundle having extras for the intent.
     * @param newTask If `true` then [Intent.FLAG_ACTIVITY_NEW_TASK] is added to the intent.
     */
    fun activityStart(packageContext: Context, cls: Class<*>?, extras: Bundle? = null, newTask : Boolean = false) {
        val activityIntent = Intent(packageContext, cls).apply {
            extras?.let { putExtras(it) }
            if (newTask) addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        activityStart(packageContext, activityIntent)
    }

    /**
     * Start a service from an intent.
     *
     * For Android version below Oreo, same as [Context.startActivity].
     * From Oreo and above, same as [Context.startForegroundService].
     *
     * @param packageContext Context of Activity or Service or
     * anything else from where the service is to be started.
     * @param serviceIntent The intent that is to be started.
     */
    fun serviceStart(packageContext: Context, serviceIntent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            packageContext.startForegroundService(serviceIntent)
        }
        else {
            packageContext.startService(serviceIntent)
        }
    }

    /**
     * Start a service class from a context.
     *
     * For below Oreo, similar to `startService(Intent(context, MyService::class.java))`.
     * Oreo and above, same as `startForegroundService(Intent(context, MyActivity::class.java))`.
     *
     * @param packageContext Context of Activity or Service or
     * anything else from where the activity is to be started.
     * @param cls Service class which should be started.
     * @param extras Bundle having extras for the intent.
     */
    fun serviceStart(packageContext: Context, cls: Class<*>?, extras: Bundle? = null) {
        val serviceIntent = Intent(packageContext, cls).apply {
            extras?.let { putExtras(it) }
        }
        serviceStart(packageContext, serviceIntent)
    }

    /**
     * Removes multiple slashes ('/') if present in the path.
     *
     * Examples:
     * - "//aaa////bbb/ccc//ddd/" will be converted to "/aaa/bbb/ccc/ddd/"
     * - "a/b/bbb//c///dd/dde///" will be converted to "a/b/bbb/c/dd/dde/"
     */
    @Suppress("NAME_SHADOWING")
    fun removeDuplicateSlashes(path: String): String {
        try {
            path.trim().let {
                if (it.length < 2) return path
                // add a space at the end for cases where duplicate is at the end.
                "$it "
            }.let { path ->

                var lastConsideredPtr: Char = path[0]
                var ptr: Char = path[1]

                fun qualifyForRemoval(startPtr: Char, endPtr: Char): Boolean {
                    // this function can be modified to remove any duplicate character, not just '/'
                    //    return startPtr == endPtr
                    return startPtr == '/' && endPtr == '/'
                }

                val modifiedString = StringBuffer("")

                for (i in 1 until path.length) {
                    ptr = path[i]
                    val behindPtr = path[i-1]
                    // behindPtr is one place behind ptr.
                    // Normally, lastConsideredPtr = behindPtr and added to modifiedString.
                    // Add lastConsideredPtr char to modifiedString if ptr and behindPtr are not duplicate.
                    // If duplicate, freeze lastConsideredPtr in its place, do not add anything to modifiedString.
                    // Once duplication is over, again move lastConsideredPtr at behindPtr and
                    // add lastConsideredPtr char to modifiedString. This will add only one instance of
                    // all the adjacent duplicate characters.
                    if (!qualifyForRemoval(ptr, behindPtr)) {
                        lastConsideredPtr = behindPtr
                        modifiedString.append(lastConsideredPtr)
                    }
                }
                return modifiedString.toString()
            }
        }
        catch (e: Exception){
            e.printStackTrace()
            return path
        }
    }








    @Deprecated(
        message = "Please use AndroidUI.showErrorDialog()",
        replaceWith = ReplaceWith(
            expression = "AndroidUI.showErrorDialog(message, title, activityContext, " +
                    "iconResource, negativeButtonText, onCloseClick)",
        )
    )
    fun showErrorDialog(message: String, title: String = "", activityContext: Context? = null,
                        iconResource: Int = 0, negativeButtonText: String = getStringFromRes(R.string.close), onCloseClick: (() -> Unit)? = null){
        AndroidUI.showErrorDialog(
            message,
            title,
            if (activityContext is Activity) activityContext else null,
            iconResource,
            negativeButtonText,
            onCloseClick
        )
    }
}
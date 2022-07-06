package balti.module.baltitoolbox.functions

import android.app.Activity
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import balti.module.baltitoolbox.R
import balti.module.baltitoolbox.ToolboxHQ
import kotlinx.coroutines.CoroutineScope
import java.io.BufferedReader

/**
 * Defining methods here allows to auto complete these methods from the IDE,
 * without importing objects.
 */

// GetResources ============

fun getStringFromRes(id: Int): String = GetResources.getStringFromRes(id)
fun getStringFromRes(id: Int, vararg formatArgs: Any?): String = GetResources.getStringFromRes(id, formatArgs)
fun getDrawableFromRes(id: Int): Drawable? = GetResources.getDrawableFromRes(id)
fun getColorFromRes(id: Int): Int = GetResources.getColorFromRes(id)
fun getResourceFromAttr(attrId: Int, context: Context): Int = GetResources.getResourceFromAttr(attrId, context)

fun setTintFromRes(view: View, rColor: Int) = GetResources.setTintFromRes(view, rColor)
fun setTintFromRes(image: ImageView, rColor: Int) = GetResources.setTintFromRes(image, rColor)

// =========================



// Misc ====================

fun tryIt(f: () -> Any?, showError: Boolean = false): Any? = Misc.tryIt(f, showError)
fun tryIt(f: () -> Any?): Any? = Misc.tryIt(f)
fun tryIt(f: () -> Unit, showError: Boolean = false) = Misc.tryIt(f, showError)

fun isPackageInstalled(packageName: String): Boolean = Misc.isPackageInstalled(packageName)
fun getAppName(packageName: String): String = Misc.getAppName(packageName)
fun getHumanReadableStorageSpace(
    fileSize: Long, isSpaceInKB: Boolean = false,
    spaceBeforeUnit: Boolean = true
): String = Misc.getHumanReadableStorageSpace(fileSize, isSpaceInKB, spaceBeforeUnit)

fun openWebLink(url: String) = Misc.openWebLink(url)
fun playStoreLink(packageName: String) = Misc.playStoreLink(packageName)

fun makeNotificationChannel(
    channelId: String,
    channelName: String,
    @ToolboxHQ.NotificationImportance importance: Int,
    channelDesc: String? = null,
    groupId: String? = null,
    silentChannel: Boolean = true,
    customizingFunction: ((NotificationChannel) -> Unit)? = null
) = Misc.makeNotificationChannel(
    channelId,
    channelName,
    importance,
    channelDesc,
    groupId,
    silentChannel,
    customizingFunction
)

fun iterateBufferedReader(
    reader: BufferedReader, loopFunction: (line: String) -> Boolean,
    onManualBreakFunction: (() -> Unit)? = null
) = Misc.iterateBufferedReader(reader, loopFunction, onManualBreakFunction)

fun doBackgroundTask(job: () -> Any?, postJob: (result: Any?) -> Any?) =
    Misc.doBackgroundTask(job, postJob)

fun delayTask(delayInMillis: Long, function: () -> Any?) =
    Misc.delayTask(delayInMillis, function)

fun runSuspendFunction(lifecycleScope: CoroutineScope? = null, f: suspend () -> Unit) =
    Misc.runSuspendFunction(lifecycleScope, f)

fun runOnMainThread(lifecycleScope: CoroutineScope? = null, f: () -> Unit) =
    Misc.runOnMainThread(lifecycleScope, f)

fun timeInMillis() = Misc.timeInMillis()
fun getPercentage(count: Int, total: Int): Int = Misc.getPercentage(count, total)
fun getHumanReadableTime(seconds: Long): String = Misc.getHumanReadableTime(seconds)

fun activityStart(packageContext: Context, activityIntent: Intent) =
    Misc.activityStart(packageContext, activityIntent)
fun activityStart(
    packageContext: Context,
    cls: Class<*>?,
    extras: Bundle? = null,
    newTask: Boolean = false
) = Misc.activityStart(packageContext, cls, extras, newTask)

fun serviceStart(packageContext: Context, serviceIntent: Intent) =
    Misc.serviceStart(packageContext, serviceIntent)
fun serviceStart(packageContext: Context, cls: Class<*>?, extras: Bundle? = null) =
    Misc.serviceStart(packageContext, cls, extras)
fun removeDuplicateSlashes(path: String): String = Misc.removeDuplicateSlashes(path)
fun transparentColor(actualColor: Int, transparencyPercentage: Int): Int =
    Misc.transparentColor(actualColor, transparencyPercentage)

// =========================



// AndroidUI ===============

fun chainDialogs(dialogList: List<AlertDialog>, endJob: (() -> Any?)?) = AndroidUI.chainDialogs(dialogList, endJob)

fun getErrorDialog(
    message: String,
    title: String = GetResources.getStringFromRes(R.string.error_occurred),
    activityContext: Activity? = null,
    iconResource: Int = 0,
    negativeButtonText: String = GetResources.getStringFromRes(R.string.close),
    onCloseClick: (() -> Unit)? = null
): AlertDialog? = AndroidUI.getErrorDialog(message, title, activityContext, iconResource, negativeButtonText, onCloseClick)

fun showErrorDialog(
    message: String,
    title: String = GetResources.getStringFromRes(R.string.error_occurred),
    activityContext: Activity? = null,
    iconResource: Int = 0,
    negativeButtonText: String = GetResources.getStringFromRes(R.string.close),
    onCloseClick: (() -> Unit)? = null
): AlertDialog? = AndroidUI.showErrorDialog(message, title, activityContext, iconResource, negativeButtonText, onCloseClick)

// =========================
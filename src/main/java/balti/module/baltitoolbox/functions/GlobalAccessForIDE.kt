package balti.module.baltitoolbox.functions

import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import balti.module.baltitoolbox.ToolboxHQ
import kotlinx.coroutines.CoroutineScope
import java.io.BufferedReader

/**
 * Defining methods here allows to auto complete these methods from the IDE,
 * without importing objects.
 */

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

fun runSuspendFunction(lifecycleScope: CoroutineScope? = null, f: suspend () -> Unit) =
    Misc.runSuspendFunction(lifecycleScope, f)

fun runOnMainThread(lifecycleScope: CoroutineScope? = null, f: () -> Unit) =
    Misc.runOnMainThread(lifecycleScope, f)

fun timeInMillis() = Misc.timeInMillis()
fun getPercentage(count: Int, total: Int): Int = Misc.getPercentage(count, total)

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

// =========================
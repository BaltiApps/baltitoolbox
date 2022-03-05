package balti.module.baltitoolbox.functions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import balti.module.baltitoolbox.R
import balti.module.baltitoolbox.ToolboxHQ
import balti.module.baltitoolbox.functions.GetResources.getStringFromRes
import balti.module.baltitoolbox.functions.Misc.tryIt

object AndroidUI {

    private val application = ToolboxHQ.application

    /**
     * Chain multiple dialogs to show one after another.
     * Next dialog is shown once the previous dialog is dismissed.
     * The first dialog of the chain is automatically displayed.
     *
     * @param dialogList List of [AlertDialog].
     * If you are using [AlertDialog.Builder], please call `create()` on the dialog builder.
     * @param endJob Optional function to execute after the last dialog is dismissed.
     */
    fun chainDialogs(dialogList: List<AlertDialog>, endJob: (() -> Any?)?) {

        dialogList.run {
            if (size > 1){
                for (i in 0 .. size - 2){
                    val currentDialog = this[i]
                    val nextDialog = this[i+1]
                    currentDialog.setOnDismissListener {
                        nextDialog.show()
                    }
                }
                this.last().apply {
                    setOnDismissListener {
                        endJob?.invoke()
                    }
                }
            }
        }

        if (dialogList.isNotEmpty()) dialogList[0].show()
    }

    /**
     * Get an "Error" dialog in the form of [AlertDialog].
     * The dialog is non-cancellable (cannot be closed by back button or clicking outside the dialog)
     * if [onCloseClick] is not null. In that case the user has to press the "Close" button,
     * which executes [onCloseClick].
     *
     * @param message String that must be shown.
     * @param title Optional title for the dialog. If nothing is passed, shows "Error occurred".
     * @param activityContext Optional [Activity] context.
     * If nothing is passed, uses [ToolboxHQ.application] context.
     * @param iconResource Optional icon resource for the dialog.
     * By default uses [android.R.drawable.stat_sys_warning].
     * @param negativeButtonText Optional text for negative button. By default shows "Close".
     * @param onCloseClick Optional function to be executed when the negative button is clicked.
     * Null by default.
     * The dialog becomes non-cancellable if this is not null.
     */
    fun getErrorDialog(
        message: String,
        title: String = getStringFromRes(R.string.error_occurred),
        activityContext: Activity? = null,
        iconResource: Int = 0,
        negativeButtonText: String = getStringFromRes(R.string.close),
        onCloseClick: (() -> Unit)? = null
    ): AlertDialog? {

        val workingContext = activityContext?: application
        var alertDialog: AlertDialog?

        try {
            alertDialog = AlertDialog.Builder(workingContext)
                .setIcon(if (iconResource == 0) android.R.drawable.stat_sys_warning else iconResource)
                .setMessage(message).apply {

                    setNegativeButton(negativeButtonText) { _, _ -> onCloseClick?.invoke() }

                    setCancelable(onCloseClick == null)
                    setTitle(title)

                }
                .create()
        } catch (e: Exception){
            e.printStackTrace()
            alertDialog = null
            tryIt { Toast.makeText(application, message, Toast.LENGTH_SHORT).show() }
        }

        return alertDialog
    }

    /**
     * Show an "Error" dialog, created by [getErrorDialog].
     * Also returns the instance of [AlertDialog] created.
     */
    fun showErrorDialog(
        message: String,
        title: String = getStringFromRes(R.string.error_occurred),
        activityContext: Activity? = null,
        iconResource: Int = 0,
        negativeButtonText: String = getStringFromRes(R.string.close),
        onCloseClick: (() -> Unit)? = null
    ): AlertDialog? {

        val ad = getErrorDialog(
            message,
            title,
            activityContext,
            iconResource,
            negativeButtonText,
            onCloseClick
        )

        ad?.show()

        return ad
    }
}
package balti.module.baltitoolbox.functions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import balti.module.baltitoolbox.ToolboxHQ

object GetResources {

    private val application = ToolboxHQ.application

    fun getStringFromRes(id: Int): String = application.getString(id)
    fun getStringFromRes(id: Int, vararg formatArgs: Any?): String = application.getString(id, formatArgs)
    fun getDrawableFromRes(id: Int): Drawable? = AppCompatResources.getDrawable(application, id)
    fun getColorFromRes(id: Int): Int = ContextCompat.getColor(application, id)

    /*
     * https://stackoverflow.com/a/27020758
     */
    fun getResourceFromAttr(attrId: Int, context: Context): Int {
        if (attrId == 0) return 0
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.resourceId
    }

    fun setTintFromRes(view: View, rColor: Int) {
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(getColorFromRes(rColor)))
    }
    fun setTintFromRes(image: ImageView, rColor: Int) {
        ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(getColorFromRes(rColor)))
    }

}
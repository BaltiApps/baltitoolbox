package balti.module.baltitoolbox.functions

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
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
    fun getStringFromRes(id: Int, vararg formatArgs: Any): String = application.getString(id, formatArgs)
    fun getDrawableFromRes(id: Int): Drawable? = AppCompatResources.getDrawable(application, id)
    fun getColorFromRes(id: Int): Int = ContextCompat.getColor(application, id)

    fun setTintFromRes(view: View, rColor: Int) {
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(getColorFromRes(rColor)))
    }
    fun setTintFromRes(image: ImageView, rColor: Int) {
        ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(getColorFromRes(rColor)))
    }

}
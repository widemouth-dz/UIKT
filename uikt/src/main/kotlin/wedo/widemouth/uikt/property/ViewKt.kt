package wedo.widemouth.uikt.property

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import wedo.widemouth.uikt.GetterUnSupported
import wedo.widemouth.uikt.GetterUnSupportedMessage

var <V : View> V.style: DefStyle<V>
	set(value) = value(this)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var View.padding: Int
	set(value) = setPadding(value)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var View.leftPadding: Int
	get() = paddingLeft
	set(value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

var View.rightPadding: Int
	get() = paddingRight
	set(value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

var View.topPadding: Int
	get() = paddingTop
	set(value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

var View.bottomPadding: Int
	get() = paddingBottom
	set(value) = setPadding(paddingLeft, paddingTop, paddingRight, value)

var View.horizontalPadding: Int
	set(value) = setPadding(value, paddingTop, value, paddingBottom)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var View.verticalPadding: Int
	set(value) = setPadding(paddingLeft, value, paddingRight, value)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var View.bgColor: Int
	get() = (background as? ColorDrawable)?.color ?: Color.TRANSPARENT
	set(@ColorInt value) = setBackgroundColor(value)

var View.bgColorRes: Int
	set(@ColorRes value) = setBackgroundColor(ContextCompat.getColor(context, value))
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var TextView.textRes: Int
	set(@StringRes value) = setText(value)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var TextView.textColor
	get() = currentTextColor
	set(@ColorInt value) = setTextColor(value)

var TextView.textColorRes: Int
	set(@ColorRes value) = setTextColor(ContextCompat.getColor(context, value))
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

var TextView.isBold: Boolean
	set(value) {
		val style = if (value) Typeface.BOLD else Typeface.NORMAL
		setTypeface(typeface, style)
	}
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = error("")

var ImageView.img: Drawable
	get() = GetterUnSupported
	set(value) = setImageDrawable(value)


var ImageView.src: Int
	set(@DrawableRes value) = setImageResource(value)
	@Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
	get() = GetterUnSupported

operator fun View.set(key: Int, tag: Any?) = setTag(key, tag)
operator fun View.get(key: Int): Any? = getTag(key)
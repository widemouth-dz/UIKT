package wedo.widemouth.uikt.property

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

val Int.sp: Int get() = spF.roundToInt()
val Int.spF: Float get() = toFloat() unit TypedValue.COMPLEX_UNIT_SP
val Int.dp: Int get() = toFloat().dp
val Int.dpF: Float get() = toFloat().dpF
val Float.dp: Int get() = dpF.roundToInt()
val Float.dpF: Float get() = this unit TypedValue.COMPLEX_UNIT_DIP

/**
 * Note: Using dimension units by [Resources.getSystem] is not suggested in [Android Resource].
 * @param unit @[TypedValue.ComplexDimensionUnit]
 */
private infix fun Float.unit(unit: Int) =
    TypedValue.applyDimension(unit, this, Resources.getSystem().displayMetrics)

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.px2dp(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Int = context.dip(value)
fun View.sp(value: Int): Int = context.sp(value)
fun View.sp(value: Float): Int = context.sp(value)
fun View.px2dp(px: Int): Float = context.px2dp(px)
fun View.px2sp(px: Int): Float = context.px2sp(px)
fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

// must be called after attached.
fun Fragment.dip(value: Int): Int = requireContext().dip(value)
fun Fragment.dip(value: Float): Int = requireContext().dip(value)
fun Fragment.sp(value: Int): Int = requireContext().sp(value)
fun Fragment.sp(value: Float): Int = requireContext().sp(value)
fun Fragment.px2dp(px: Int): Float = requireContext().px2dp(px)
fun Fragment.px2sp(px: Int): Float = requireContext().px2sp(px)
fun Fragment.dimen(@DimenRes resource: Int): Int = requireContext().dimen(resource)
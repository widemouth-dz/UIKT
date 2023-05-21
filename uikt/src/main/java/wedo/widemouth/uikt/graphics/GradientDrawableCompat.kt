package wedo.widemouth.uikt.graphics

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.ColorInt
import wedo.widemouth.uikt.GetterUnSupported
import wedo.widemouth.uikt.GetterUnSupportedMessage
import kotlin.math.min


/**
 * Compatible with [ColorStateList] in [GradientDrawable].
 *
 * About [Drawable.mutate][android.graphics.drawable.Drawable.mutate]:
 *
 * In my opinion, it is mainly used to improve the performance of `XMl` resources rather than saving memory,
 * so there is no need to adapt to [android.graphics.drawable.Drawable.ConstantState] for the [mutate].
 * if you need a [GradientDrawableCompat], just `new` it via its factory, [GradientDrawable].
 *
 * @property radiusAdjustBounds Whether to adjust radius to the half of size.
 * @property bgColors The background [ColorStateList]] before [Build.VERSION_CODES.LOLLIPOP]
 * @property strokeWidth The width of stroke line.
 * @property strokeColors The stroke [ColorStateList]] before [Build.VERSION_CODES.LOLLIPOP]
 * @author WideMouth
 * @since 2023/5/13
 */
class GradientDrawableCompat : GradientDrawable() {

    var radiusAdjustBounds = true
        set(value) {
            field = value
            if (field) cornerRadius = min(bounds.width(), bounds.height()) / 2f
        }

    private var mBgColors: ColorStateList? = null
    var bgColors: ColorStateList?
        set(value) {
            color = value
        }
        @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
        get() = GetterUnSupported

    private var mStrokeWidth = 0
    var strokeWidth: Int
        set(value) = setStroke(value, mStrokeColors)
        @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
        get() = GetterUnSupported

    private var mStrokeColors: ColorStateList? = null
    var strokeColors: ColorStateList?
        set(value) = setStroke(mStrokeWidth, value)
        @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
        get() = GetterUnSupported


    override fun setColor(colorStateList: ColorStateList?) {
        mBgColors = colorStateList
        if (hasNativeStateListAPI) {
            super.setColor(colorStateList)
        } else {
            setColor(mBgColors?.getColorForState(state, 0) ?: Color.TRANSPARENT)
        }
    }

    override fun setStroke(width: Int, colorStateList: ColorStateList?) {
        mStrokeWidth = width
        mStrokeColors = colorStateList
        if (hasNativeStateListAPI) {
            super.setStroke(mStrokeWidth, mStrokeColors)
        } else {
            val currentColor: Int = mStrokeColors?.getColorForState(state, 0) ?: Color.TRANSPARENT
            setStroke(mStrokeWidth, currentColor)
        }
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        var result = super.onStateChange(stateSet)
        if (!hasNativeStateListAPI) {
            mBgColors?.let {
                val bgColor = it.getColorForState(stateSet, 0)
                setColor(bgColor)
                result = true
            }

            mStrokeColors?.let {
                val strokeColor = it.getColorForState(stateSet, 0)
                setStroke(mStrokeWidth, strokeColor)
                result = true
            }
        }
        return result
    }

    override fun isStateful(): Boolean =
        super.isStateful() || mBgColors?.isStateful == true || mStrokeColors?.isStateful == true

    override fun onBoundsChange(r: Rect) {
        super.onBoundsChange(r)
        if (radiusAdjustBounds) cornerRadius = min(r.width(), r.height()) / 2f
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
    private val hasNativeStateListAPI: Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

/** [GradientDrawableCompat] building factory. */
@Suppress("FunctionName")
fun GradientDrawable(
    @ColorInt color: Int? = null,
    colors: ColorStateList? = null,
    alpha: Float = 1f,
    radiusAdjustBounds: Boolean = false,
    cornerRadius: Float = 0f,
    leftTopRadius: Float = cornerRadius,
    rightTopRadius: Float = cornerRadius,
    leftBottomRadius: Float = cornerRadius,
    rightBottomRadius: Float = cornerRadius,
    strokeColor: Int? = null,
    strokeColors: ColorStateList? = null,
    strokeWidth: Int = 0,
    sizeW: Int = -1,
    sizeH: Int = -1
): GradientDrawableCompat {
    val isValidRadius =
        leftTopRadius > 0 || rightTopRadius > 0 || leftBottomRadius > 0 || rightBottomRadius > 0
    return GradientDrawableCompat().apply {
        setSize(sizeW, sizeH)
        this.radiusAdjustBounds = radiusAdjustBounds
        this.alpha = (0xFF * alpha).toInt()
        if (color != null) setColor(color)
        if (colors != null) bgColors = colors
        if (isValidRadius) cornerRadii = floatArrayOf(
            leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
            rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius,
        )
        if (strokeWidth > 0) {
            if (strokeColor != null) setStroke(strokeWidth, strokeColor)
            if (strokeColors != null) setStroke(strokeWidth, strokeColors)
        }
    }
}

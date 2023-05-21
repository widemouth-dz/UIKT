package wedo.widemouth.uikt.decoration

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import wedo.widemouth.uikt.R
import wedo.widemouth.uikt.decoration.IViewEffect.Companion.HIDE_RADIUS_SIDE_BOTTOM
import wedo.widemouth.uikt.decoration.IViewEffect.Companion.HIDE_RADIUS_SIDE_LEFT
import wedo.widemouth.uikt.decoration.IViewEffect.Companion.HIDE_RADIUS_SIDE_NONE
import wedo.widemouth.uikt.decoration.IViewEffect.Companion.HIDE_RADIUS_SIDE_RIGHT
import wedo.widemouth.uikt.decoration.IViewEffect.Companion.HIDE_RADIUS_SIDE_TOP
import wedo.widemouth.uikt.delegate.ObservableTProperty
import java.lang.StrictMath.floor
import java.lang.ref.WeakReference
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty

/**
 * The helper of [IViewEffect].
 *
 * Note that:
 * * Shadow and border are mutually exclusive.
 * * Rounded corners and shadow require [Build.VERSION_CODES.LOLLIPOP].
 * * The granularity of rounded corners only supports side dimension, see [IViewEffect.HideRadiusSide].
 *
 * Extend like that:
 * ```
 * 	class ViewExt @JvmOverloads constructor(
 * 		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
 * 	) : AppCompatView(context, attrs),
 * 		IViewEffect by ViewEffectHelper(context, attrs, defStyleAttr, defStyleRes) {
 * 		init {
 * 			@Suppress("LeakingThis")
 * 			owner = this
 * 		}
 * 		override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
 * 			measureDelegate(widthMeasureSpec, heightMeasureSpec) { widthMeasuredSpec, heightMeasuredSpec ->
 * 				super.onMeasure(widthMeasuredSpec, heightMeasuredSpec)
 * 			}
 * 		}
 * 		override fun dispatchDraw(canvas: Canvas) {
 * 			super.dispatchDraw(canvas)
 * 			drawDividers(canvas)
 * 			dispatchRoundBorderDraw(canvas)
 * 		}
 * ```
 * Then `ViewExt` becomes an extension of [IViewEffect].
 *
 * In XML:
 * ```
 * <ViewExt
 *		android:id="@+id/ext"
 *		android:layout_width="match_parent"
 *		android:layout_height="match_parent"
 *		android:maxWidth="100dp"
 *		android:minWidth="80dp"
 *		android:maxHeight="50dp"
 *		android:minHeight="30dp"
 *		app:shadowColor="@color/blue"
 *		app:shadowAlpha="0.1f"
 *		app:shadowElevation="10dp"
 *		app:borderWidth="1dp"
 *		app:borderColor="@color/black"
 *		app:radius="6dp"
 *		app:isRadiusAdjustBounds="true"
 *		...	/>
 * ```
 *
 * In code:
 * ```
 * 	val ext:ViewExt = findViewById(R.id.ext)
 * 	ext.run{
 * 		radius = 6.dp
 * 		borderWidth = 1.dp
 * 		shadowColor = Color.Blue
 * 		...
 * 	}
 * ```
 * @author WideMouth
 * @since 2023/3/7
 */
class ViewEffectHelper(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : IViewEffect {

	companion object {
		@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
		val useFeature
			get() = false
	}

	private var _owner: WeakReference<View>? = null
	override var owner: View?
		get() = _owner?.get()
		set(value) {
			_owner = WeakReference(value)
			applyOutLineEffect()
		}
	// size
	override var maxiWidth: Int by UIEffect(0, draw = true, layout = true)
	override var maxiHeight: Int by UIEffect(0, draw = true, layout = true)
	override var miniWidth: Int by UIEffect(0, draw = true, layout = true)
	override var miniHeight: Int by UIEffect(0, draw = true, layout = true)


	// round
	private val mMaskPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
	private val mRadiusArray by lazy { FloatArray(8) }
	private val mBorderRect by lazy { RectF() }
	private val mPath by lazy { Path() }


	override var radius: Int by OutLineEffect(0)
	override var radiusAdjustBounds: Boolean by OutLineEffect(false)
	override var clipToRoundedCorner: Boolean by UIEffect(false)

	@IViewEffect.HideRadiusSide
	override var hideRadiusSide: Int by OutLineEffect(HIDE_RADIUS_SIDE_NONE)
	override var borderColor: Int by UIEffect(0)
	override var borderWidth: Int by UIEffect(0)
	override var cornerOuterColor: Int by UIEffect(0)
	override var isOutlineExcludePadding: Boolean by UIEffect(false)

	// shadow
	override var isShowBorderOnlyBeforeL: Boolean by UIEffect(true)
	override var shadowElevation: Int by OutLineEffect(0)
	override var shadowAlpha: Float by OutLineEffect(1f)
	override var shadowColor: Int by OutLineEffect(Color.BLACK)

	// outline inset
	override var outlineInsetLeft: Int by UIEffect(0)
	override var outlineInsetRight: Int by UIEffect(0)
	override var outlineInsetTop: Int by UIEffect(0)
	override var outlineInsetBottom: Int by UIEffect(0)

	// divider line
	override var isDividerEnabled: Boolean by UIEffect(true)

	override var topDividerHeight: Int by UIEffect(0)
	override var topDividerInsetLeft: Int by UIEffect(0)
	override var topDividerInsetRight: Int by UIEffect(0)
	override var topDividerColor: Int by UIEffect(Color.GRAY)
	override var topDividerAlpha: Int by UIEffect(255)

	override var bottomDividerHeight: Int by UIEffect(0)
	override var bottomDividerInsetLeft: Int by UIEffect(0)
	override var bottomDividerInsetRight: Int by UIEffect(0)
	override var bottomDividerColor: Int by UIEffect(Color.GRAY)
	override var bottomDividerAlpha: Int by UIEffect(255)

	override var leftDividerWidth: Int by UIEffect(0)
	override var leftDividerInsetTop: Int by UIEffect(0)
	override var leftDividerInsetBottom: Int by UIEffect(0)
	override var leftDividerColor: Int by UIEffect(Color.GRAY)
	override var leftDividerAlpha: Int by UIEffect(255)

	override var rightDividerWidth: Int by UIEffect(0)
	override var rightDividerInsetTop: Int by UIEffect(0)
	override var rightDividerInsetBottom: Int by UIEffect(0)
	override var rightDividerColor: Int by UIEffect(Color.GRAY)
	override var rightDividerAlpha: Int by UIEffect(255)

	private val mDividerPaint: Paint by lazy { Paint() }

	init {
		if (null != attrs || defStyleAttr != 0 || defStyleRes != 0) {
			val ta = context.obtainStyledAttributes(
				attrs, R.styleable.ViewDecorationHelper, defStyleAttr, defStyleRes
			)
			val count = ta.indexCount
			for (i in 0 until count) {
				when (val index = ta.getIndex(i)) {
					R.styleable.ViewDecorationHelper_android_maxWidth ->
						maxiWidth = ta.getDimensionPixelSize(index, maxiWidth)
					R.styleable.ViewDecorationHelper_android_maxHeight ->
						maxiHeight = ta.getDimensionPixelSize(index, maxiHeight)
					R.styleable.ViewDecorationHelper_android_minWidth ->
						miniWidth = ta.getDimensionPixelSize(index, miniWidth)
					R.styleable.ViewDecorationHelper_android_minHeight ->
						miniHeight = ta.getDimensionPixelSize(index, miniHeight)
					R.styleable.ViewDecorationHelper_topDividerColor ->
						topDividerColor = ta.getColor(index, topDividerColor)
					R.styleable.ViewDecorationHelper_topDividerHeight ->
						topDividerHeight = ta.getDimensionPixelSize(index, topDividerHeight)
					R.styleable.ViewDecorationHelper_topDividerInsetLeft ->
						topDividerInsetLeft = ta.getDimensionPixelSize(index, topDividerInsetLeft)
					R.styleable.ViewDecorationHelper_topDividerInsetRight ->
						topDividerInsetRight = ta.getDimensionPixelSize(index, topDividerInsetRight)
					R.styleable.ViewDecorationHelper_bottomDividerColor ->
						bottomDividerColor = ta.getColor(index, bottomDividerColor)
					R.styleable.ViewDecorationHelper_bottomDividerHeight ->
						bottomDividerHeight = ta.getDimensionPixelSize(index, bottomDividerHeight)
					R.styleable.ViewDecorationHelper_bottomDividerInsetLeft ->
						bottomDividerInsetLeft =
							ta.getDimensionPixelSize(index, bottomDividerInsetLeft)
					R.styleable.ViewDecorationHelper_bottomDividerInsetRight ->
						bottomDividerInsetRight =
							ta.getDimensionPixelSize(index, bottomDividerInsetRight)
					R.styleable.ViewDecorationHelper_leftDividerColor ->
						leftDividerColor = ta.getColor(index, leftDividerColor)
					R.styleable.ViewDecorationHelper_leftDividerWidth ->
						leftDividerWidth = ta.getDimensionPixelSize(index, leftDividerWidth)
					R.styleable.ViewDecorationHelper_leftDividerInsetTop ->
						leftDividerInsetTop = ta.getDimensionPixelSize(index, leftDividerInsetTop)
					R.styleable.ViewDecorationHelper_leftDividerInsetBottom ->
						leftDividerInsetBottom =
							ta.getDimensionPixelSize(index, leftDividerInsetBottom)
					R.styleable.ViewDecorationHelper_rightDividerColor ->
						rightDividerColor = ta.getColor(index, rightDividerColor)
					R.styleable.ViewDecorationHelper_rightDividerWidth ->
						rightDividerWidth = ta.getDimensionPixelSize(index, rightDividerWidth)
					R.styleable.ViewDecorationHelper_rightDividerInsetTop ->
						rightDividerInsetTop = ta.getDimensionPixelSize(index, rightDividerInsetTop)
					R.styleable.ViewDecorationHelper_rightDividerInsetBottom ->
						rightDividerInsetBottom =
							ta.getDimensionPixelSize(index, rightDividerInsetBottom)
					R.styleable.ViewDecorationHelper_borderColor ->
						borderColor = ta.getColor(index, borderColor)
					R.styleable.ViewDecorationHelper_borderWidth ->
						borderWidth = ta.getDimensionPixelSize(index, borderWidth)
					R.styleable.ViewDecorationHelper_isRadiusAdjustBounds ->
						radiusAdjustBounds = ta.getBoolean(index, radiusAdjustBounds)
					R.styleable.ViewDecorationHelper_radius ->
						radius = ta.getDimensionPixelSize(index, radius)
					R.styleable.ViewDecorationHelper_outerNormalColor ->
						cornerOuterColor = ta.getColor(index, cornerOuterColor)
					R.styleable.ViewDecorationHelper_hideRadiusSide ->
						hideRadiusSide = ta.getInt(index, hideRadiusSide)
					R.styleable.ViewDecorationHelper_showBorderOnlyBeforeL ->
						isShowBorderOnlyBeforeL = ta.getBoolean(index, isShowBorderOnlyBeforeL)
					R.styleable.ViewDecorationHelper_shadowElevation ->
						shadowElevation = ta.getDimensionPixelSize(index, shadowElevation)
					R.styleable.ViewDecorationHelper_shadowAlpha ->
						shadowAlpha = ta.getFloat(index, shadowAlpha)
					R.styleable.ViewDecorationHelper_outlineInsetLeft ->
						outlineInsetLeft = ta.getDimensionPixelSize(index, outlineInsetLeft)
					R.styleable.ViewDecorationHelper_outlineInsetRight ->
						outlineInsetRight = ta.getDimensionPixelSize(index, outlineInsetRight)
					R.styleable.ViewDecorationHelper_outlineInsetTop ->
						outlineInsetTop = ta.getDimensionPixelSize(index, outlineInsetTop)
					R.styleable.ViewDecorationHelper_outlineInsetBottom ->
						outlineInsetBottom = ta.getDimensionPixelSize(index, outlineInsetBottom)
					R.styleable.ViewDecorationHelper_outlineExcludePadding ->
						isOutlineExcludePadding = ta.getBoolean(index, isOutlineExcludePadding)
				}
			}
			ta.recycle()
		}
	}

	private fun invalidate() {
		owner?.invalidate()
	}

	private fun requestLayout() {
		owner?.requestLayout()
	}

	private var mOutLineEffectScheduled = false

	/** Schedule the [applyOutLineEffect], just like [View.invalidate]. */
	private fun scheduleOutLineEffect() {
		if (mOutLineEffectScheduled) return
		owner?.run {
			mOutLineEffectScheduled = true
			post {
				mOutLineEffectScheduled = false
				applyOutLineEffect()
			}
		}
	}

	/** Apply rounded corners and shadow by [ViewOutlineProvider]. */
	private fun applyOutLineEffect() {
		val owner: View = owner ?: return

		val isGranularRadius = isGranularRadius()
		if (useFeature) {
			if (shadowElevation == 0 || isGranularRadius) {
				owner.elevation = 0f
			} else {
				owner.elevation = shadowElevation.toFloat()
			}
			setShadowColorInner(shadowColor)
			owner.outlineProvider = object : ViewOutlineProvider() {
				@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
				override fun getOutline(view: View, outline: Outline) {
					val rRadius: Int = realRadius
					val w = view.width
					val h = view.height
					if (w == 0 || h == 0) return

					if (isGranularRadius) {
						var left = 0
						var top = 0
						var right = w
						var bottom = h
						when (hideRadiusSide) {
							HIDE_RADIUS_SIDE_LEFT -> left -= rRadius
							HIDE_RADIUS_SIDE_TOP -> top -= rRadius
							HIDE_RADIUS_SIDE_RIGHT -> right += rRadius
							HIDE_RADIUS_SIDE_BOTTOM -> bottom += rRadius
						}
						outline.setRoundRect(left, top, right, bottom, rRadius.toFloat())
						return
					}
					var top: Int = outlineInsetTop
					var bottom = max(top + 1, h - outlineInsetBottom)
					var left: Int = outlineInsetLeft
					var right: Int = w - outlineInsetRight
					if (isOutlineExcludePadding) {
						left += view.paddingLeft
						top += view.paddingTop
						right = max(left + 1, right - view.paddingRight)
						bottom = max(top + 1, bottom - view.paddingBottom)
					}
					// outline.setAlpha will work even if shadowElevation == 0
					outline.alpha = if (shadowElevation == 0) 1f else shadowAlpha
					if (rRadius <= 0) {
						outline.setRect(left, top, right, bottom)
					} else {
						outline.setRoundRect(left, top, right, bottom, rRadius.toFloat())
					}
				}
			}
			owner.clipToOutline = radiusAdjustBounds || radius > 0
		}
		owner.invalidate()
	}

	private fun setShadowColorInner(shadowColor: Int) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			val owner: View = owner ?: return
			owner.outlineAmbientShadowColor = shadowColor
			owner.outlineSpotShadowColor = shadowColor
		}
	}

	private fun isGranularRadius(): Boolean =
		radius > 0 && hideRadiusSide != HIDE_RADIUS_SIDE_NONE

	private val realRadius: Int
		get() {
			val owner: View = owner ?: return radius
			return if (radiusAdjustBounds) min(owner.width, owner.height) / 2 else radius
		}


	/**
	 * Perform two actions.
	 * * Border.
	 * * Mock round corners mask.
	 * @param canvas Canvas
	 */
	override fun dispatchRoundBorderDraw(canvas: Canvas) {
		val owner: View = owner ?: return

		val isGranularRadius = isGranularRadius()
		val radius: Float = realRadius.toFloat()
		val needCheckFakeCornerOuterDraw = radius > 0 && !useFeature && cornerOuterColor != 0
		val needDrawBorder = borderWidth > 0 && borderColor != 0
		if (!needCheckFakeCornerOuterDraw && !needDrawBorder) {
			return
		}
		if (isShowBorderOnlyBeforeL && useFeature && shadowElevation != 0) {
			return
		}
		val width = canvas.width
		val height = canvas.height
		canvas.save()
		canvas.translate(owner.scrollX.toFloat(), owner.scrollY.toFloat())

		// react
		val halfBorderWith = borderWidth / 2f
		val insetRadius = radius - halfBorderWith
		if (isOutlineExcludePadding) {
			mBorderRect.set(
				owner.paddingLeft.toFloat(),
				owner.paddingTop.toFloat(),
				width - owner.paddingRight.toFloat(),
				height - owner.paddingBottom.toFloat()
			)
		} else {
			mBorderRect.set(0f, 0f, width.toFloat(), height.toFloat())
		}
		mBorderRect.inset(halfBorderWith, halfBorderWith)
		if (isGranularRadius) {
			// reset to 0f.
			mRadiusArray.fill(0f)
			when (hideRadiusSide) {
				HIDE_RADIUS_SIDE_TOP -> {
					mRadiusArray.fill(insetRadius, 4, 8)
				}
				HIDE_RADIUS_SIDE_RIGHT -> {
					mRadiusArray.fill(insetRadius, 0, 2)
					mRadiusArray.fill(insetRadius, 6, 8)
				}
				HIDE_RADIUS_SIDE_BOTTOM -> {
					mRadiusArray.fill(insetRadius, 0, 4)
				}
				HIDE_RADIUS_SIDE_LEFT -> {
					mRadiusArray.fill(insetRadius, 2, 6)
				}
			}
		}

		// Mock round corners mask.
		if (needCheckFakeCornerOuterDraw) {
			mMaskPaint.color = cornerOuterColor
			mMaskPaint.style = Paint.Style.FILL
			canvas.drawPath(mPath.apply {
				reset()
				// A rect and a round rect in opposite directions so that paint is filled between them.
				addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
				if (!isGranularRadius) {
					addRoundRect(mBorderRect, insetRadius, insetRadius, Path.Direction.CCW)
				} else {
					addRoundRect(mBorderRect, mRadiusArray, Path.Direction.CCW)
				}
			}, mMaskPaint)
		}

		// Border.
		if (needDrawBorder) {
			mMaskPaint.color = borderColor
			mMaskPaint.strokeWidth = borderWidth.toFloat()
			mMaskPaint.style = Paint.Style.STROKE
			if (isGranularRadius) {
				drawRoundRect(canvas, mBorderRect, mRadiusArray, mMaskPaint)
			} else if (insetRadius <= 0) {
				canvas.drawRect(mBorderRect, mMaskPaint)
			} else {
				canvas.drawRoundRect(mBorderRect, insetRadius, insetRadius, mMaskPaint)
			}
		}
		canvas.restore()
	}

	private fun drawRoundRect(canvas: Canvas, rect: RectF, radiusArray: FloatArray, paint: Paint) {
		mPath.reset()
		mPath.addRoundRect(rect, radiusArray, Path.Direction.CW)
		canvas.drawPath(mPath, paint)
	}

	/**
	 * Draw dividers if set.
	 */
	override fun drawDividers(canvas: Canvas) {
		if (!isDividerEnabled) return
		val owner = owner ?: return
		val w = owner.width
		val h = owner.height
		canvas.save()
		canvas.translate(owner.scrollX.toFloat(), owner.scrollY.toFloat())
		if (topDividerHeight > 0) {
			mDividerPaint.strokeWidth = topDividerHeight.toFloat()
			mDividerPaint.color = topDividerColor
			mDividerPaint.alpha = topDividerAlpha

			val y: Float = topDividerHeight / 2f
			canvas.drawLine(
				topDividerInsetLeft.toFloat(), y, (w - topDividerInsetRight).toFloat(), y,
				mDividerPaint
			)
		}
		if (bottomDividerHeight > 0) {
			mDividerPaint.strokeWidth = bottomDividerHeight.toFloat()
			mDividerPaint.color = bottomDividerColor
			mDividerPaint.alpha = bottomDividerAlpha
			val y = floor((h - bottomDividerHeight / 2f).toDouble()).toFloat()
			canvas.drawLine(
				bottomDividerInsetLeft.toFloat(), y, (w - bottomDividerInsetRight).toFloat(), y,
				mDividerPaint
			)
		}
		if (leftDividerWidth > 0) {
			mDividerPaint.strokeWidth = leftDividerWidth.toFloat()
			mDividerPaint.color = leftDividerColor
			mDividerPaint.alpha = leftDividerAlpha
			val x: Float = leftDividerWidth / 2f
			canvas.drawLine(
				x, leftDividerInsetTop.toFloat(), x, (h - leftDividerInsetBottom).toFloat(),
				mDividerPaint
			)
		}
		if (rightDividerWidth > 0) {
			mDividerPaint.strokeWidth = rightDividerWidth.toFloat()
			mDividerPaint.color = rightDividerColor
			mDividerPaint.alpha = rightDividerAlpha
			val x = floor((w - rightDividerWidth / 2f).toDouble()).toFloat()
			canvas.drawLine(
				x, rightDividerInsetTop.toFloat(), x, (h - rightDividerInsetBottom).toFloat(),
				mDividerPaint
			)
		}
		canvas.restore()
	}

	/**
	 * Use it in [View.onMeasure] of [owner] like this:
	 * ```
	 * 	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
	 * 		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	 * 		measureDelegate(widthMeasureSpec, heightMeasureSpec) { widthMeasuredSpec, heightMeasuredSpec ->
	 * 			super.onMeasure(widthMeasuredSpec, heightMeasuredSpec)
	 * 		}
	 * 	}
	 * ```
	 * @param widthMeasureSpec widthMeasureSpec in [View.onMeasure].
	 * @param heightMeasureSpec heightMeasureSpec in [View.onMeasure].
	 * @param measure The measured result lambda.
	 */
	override fun measureDelegate(
		widthMeasureSpec: Int, heightMeasureSpec: Int,
		measure: (widthMeasuredSpec: Int, heightMeasuredSpec: Int) -> Unit
	) {
		val delegated =
			owner ?: throw IllegalStateException("Bind the delegated view owner before called!")
		measure(
			getLimitMeasuredSpec(widthMeasureSpec, maxiWidth),
			getLimitMeasuredSpec(heightMeasureSpec, maxiHeight)
		)
		val minWidthSpec: Int =
			getMiniMeasuredSpec(widthMeasureSpec, delegated.measuredWidth, miniWidth)
		val minHeightSpec: Int =
			getMiniMeasuredSpec(heightMeasureSpec, delegated.measuredHeight, miniHeight)
		if (widthMeasureSpec != minWidthSpec || heightMeasureSpec != minHeightSpec) {
			measure(minWidthSpec, minHeightSpec)
		}
	}


	private fun getMiniMeasuredSpec(measureSpec: Int, measuredSize: Int, sizeMini: Int): Int =
		if (View.MeasureSpec.getMode(measureSpec) != View.MeasureSpec.EXACTLY && measuredSize < sizeMini) {
			View.MeasureSpec.makeMeasureSpec(sizeMini, View.MeasureSpec.EXACTLY)
		} else measureSpec


	private fun getLimitMeasuredSpec(measureSpec: Int, sizeLimit: Int): Int {
		var measuredSpec = measureSpec
		if (sizeLimit > 0) {
			val size = View.MeasureSpec.getSize(measuredSpec)
			if (size > sizeLimit) {
				val mode = View.MeasureSpec.getMode(measuredSpec)
				measuredSpec =
					if (mode == View.MeasureSpec.AT_MOST) {
						View.MeasureSpec.makeMeasureSpec(sizeLimit, View.MeasureSpec.AT_MOST)
					} else {
						View.MeasureSpec.makeMeasureSpec(sizeLimit, View.MeasureSpec.EXACTLY)
					}
			}
		}
		return measuredSpec
	}

	/**
	 * The delegate class for [ViewEffectHelper] properties
	 * @param initialValue The initial value of property.
	 * @property draw Whether to call [View.invalidate] on change.
	 * @property layout Whether to call [View.requestLayout]  on change.
	 * @constructor
	 */
	class UIEffect<V>(
		initialValue: V,
		private val draw: Boolean = true,
		private val layout: Boolean = false
	) : ObservableTProperty<ViewEffectHelper, V>(initialValue) {
		override fun afterChange(
			thisRef: ViewEffectHelper, property: KProperty<*>, oldValue: V, newValue: V
		) {
			super.afterChange(thisRef, property, oldValue, newValue)
			if (oldValue != newValue) {
				if (draw) thisRef.invalidate()
				if (layout) thisRef.requestLayout()
			}
		}
	}

	/**
	 * The delegate class for [ViewEffectHelper] properties about [Outline],
	 * it calls [scheduleOutLineEffect] to schedule [applyOutLineEffect], just like [invalidate].
	 * @param initialValue The initial value of property.
	 */
	class OutLineEffect<V>(initialValue: V) :
		ObservableTProperty<ViewEffectHelper, V>(initialValue) {
		override fun afterChange(
			thisRef: ViewEffectHelper, property: KProperty<*>, oldValue: V, newValue: V
		) {
			super.afterChange(thisRef, property, oldValue, newValue)
			if (oldValue != newValue) thisRef.scheduleOutLineEffect()
		}
	}
}




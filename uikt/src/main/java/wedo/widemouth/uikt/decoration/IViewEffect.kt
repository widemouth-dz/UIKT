package wedo.widemouth.uikt.decoration

import android.graphics.Canvas
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.annotation.ViewEffectGroup

/**
 * This is an extension for view, implemented by [ViewEffectHelper]
 *
 * Support that:
 * * Border(width, color).
 * * Shadow(elevation, color, alpha).
 * * Rounded corner(hide side, radius, adjust bound).
 * * Divider(top, bottom, left, right, color, width, alpha).
 * * Width(min, max) and height(min, max).
 *
 * @author WideMouth
 * @since 2023/3/7
 */
@ViewEffectGroup(
	[
		ConstraintLayout::class,
		FrameLayout::class,
		RelativeLayout::class,
		LinearLayout::class,
		CoordinatorLayout::class,
//		ScrollView::class,

		AppCompatImageView::class,

		ImageButton::class,
		ImageView::class
	]
)
interface IViewEffect {
	companion object {
		const val HIDE_RADIUS_SIDE_NONE = 0
		const val HIDE_RADIUS_SIDE_TOP = 1
		const val HIDE_RADIUS_SIDE_RIGHT = 2
		const val HIDE_RADIUS_SIDE_BOTTOM = 3
		const val HIDE_RADIUS_SIDE_LEFT = 4
	}

	@IntDef(value = [HIDE_RADIUS_SIDE_NONE, HIDE_RADIUS_SIDE_TOP, HIDE_RADIUS_SIDE_RIGHT, HIDE_RADIUS_SIDE_BOTTOM, HIDE_RADIUS_SIDE_LEFT])
	@Retention(AnnotationRetention.SOURCE)
	annotation class HideRadiusSide

	var owner: View?

	// size
	var maxiWidth: Int
	var maxiHeight: Int
	var miniWidth: Int
	var miniHeight: Int

	// round
	var radius: Int
	var radiusAdjustBounds: Boolean
	var clipToRoundedCorner: Boolean

	@HideRadiusSide
	var hideRadiusSide: Int
	var borderColor: Int
	var borderWidth: Int
	var cornerOuterColor: Int
	var isOutlineExcludePadding: Boolean

	// shadow
	var isShowBorderOnlyBeforeL: Boolean
	var shadowElevation: Int
	var shadowAlpha: Float
	var shadowColor: Int

	// outline inset
	var outlineInsetLeft: Int
	var outlineInsetRight: Int
	var outlineInsetTop: Int
	var outlineInsetBottom: Int

	var isDividerEnabled: Boolean
	var topDividerHeight: Int
	var topDividerInsetLeft: Int
	var topDividerInsetRight: Int
	var topDividerColor: Int
	var topDividerAlpha: Int

	var bottomDividerHeight: Int
	var bottomDividerInsetLeft: Int
	var bottomDividerInsetRight: Int
	var bottomDividerColor: Int
	var bottomDividerAlpha: Int

	var leftDividerWidth: Int
	var leftDividerInsetTop: Int
	var leftDividerInsetBottom: Int
	var leftDividerColor: Int
	var leftDividerAlpha: Int

	var rightDividerWidth: Int
	var rightDividerInsetTop: Int
	var rightDividerInsetBottom: Int
	var rightDividerColor: Int
	var rightDividerAlpha: Int

	fun setTopDivider(
		topDividerHeight: Int = this.topDividerHeight,
		topDividerInsetLeft: Int = this.topDividerInsetLeft,
		topDividerInsetRight: Int = this.topDividerInsetRight,
		topDividerColor: Int = this.topDividerColor,
		topDividerAlpha: Int = this.topDividerAlpha,
	) {
		this.topDividerHeight = topDividerHeight
		this.topDividerInsetLeft = topDividerInsetLeft
		this.topDividerInsetRight = topDividerInsetRight
		this.topDividerColor = topDividerColor
		this.topDividerAlpha = topDividerAlpha
	}

	fun setBottomDivider(
		bottomDividerHeight: Int = this.bottomDividerHeight,
		bottomDividerInsetLeft: Int = this.bottomDividerInsetLeft,
		bottomDividerInsetRight: Int = this.bottomDividerInsetRight,
		bottomDividerColor: Int = this.bottomDividerColor,
		bottomDividerAlpha: Int = this.bottomDividerAlpha,
	) {
		this.bottomDividerHeight = bottomDividerHeight
		this.bottomDividerInsetLeft = bottomDividerInsetLeft
		this.bottomDividerInsetRight = bottomDividerInsetRight
		this.bottomDividerColor = bottomDividerColor
		this.bottomDividerAlpha = bottomDividerAlpha
	}

	fun setLeftDivider(
		leftDividerWidth: Int = this.leftDividerWidth,
		leftDividerInsetTop: Int = this.leftDividerInsetTop,
		leftDividerInsetBottom: Int = this.leftDividerInsetBottom,
		leftDividerColor: Int = this.leftDividerColor,
		leftDividerAlpha: Int = this.leftDividerAlpha,
	) {
		this.leftDividerWidth = leftDividerWidth
		this.leftDividerInsetTop = leftDividerInsetTop
		this.leftDividerInsetBottom = leftDividerInsetBottom
		this.leftDividerColor = leftDividerColor
		this.leftDividerAlpha = leftDividerAlpha
	}

	fun setRightDivider(
		rightDividerWidth: Int = this.rightDividerWidth,
		rightDividerInsetTop: Int = this.rightDividerInsetTop,
		rightDividerInsetBottom: Int = this.rightDividerInsetBottom,
		rightDividerColor: Int = this.rightDividerColor,
		rightDividerAlpha: Int = this.rightDividerAlpha,
	) {
		this.rightDividerWidth = rightDividerWidth
		this.rightDividerInsetTop = rightDividerInsetTop
		this.rightDividerInsetBottom = rightDividerInsetBottom
		this.rightDividerColor = rightDividerColor
		this.rightDividerAlpha = rightDividerAlpha
	}

	fun drawDividers(canvas: Canvas)

	fun dispatchRoundBorderDraw(canvas: Canvas)

	fun measureDelegate(
		widthMeasureSpec: Int,
		heightMeasureSpec: Int,
		measure: (widthMeasuredSpec: Int, heightMeasuredSpec: Int) -> Unit,
	)

}
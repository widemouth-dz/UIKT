package wedo.widemouth.uikt.decoration

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

open class ConstraintLayoutExt @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs),
	IViewEffect by ViewEffectHelper(context, attrs, defStyleAttr, defStyleRes) {


	init {
		@Suppress("LeakingThis")
		owner = this
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//		measureDelegate(widthMeasureSpec, heightMeasureSpec) { widthMeasuredSpec, heightMeasuredSpec ->
//			super.onMeasure(widthMeasuredSpec, heightMeasuredSpec)
//		}
	}

	override fun dispatchDraw(canvas: Canvas) {
		super.dispatchDraw(canvas)
		drawDividers(canvas)
		dispatchRoundBorderDraw(canvas)
	}
}





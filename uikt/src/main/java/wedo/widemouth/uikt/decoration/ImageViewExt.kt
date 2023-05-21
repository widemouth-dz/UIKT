package wedo.widemouth.uikt.decoration

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

open class ImageViewExt @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr),
    IViewEffect by ViewEffectHelper(context, attrs, defStyleAttr, defStyleRes) {


    init {

        owner = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureDelegate(
            widthMeasureSpec, heightMeasureSpec
        ) { widthMeasuredSpec, heightMeasuredSpec ->
            super.onMeasure(widthMeasuredSpec, heightMeasuredSpec)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawDividers(canvas)
        dispatchRoundBorderDraw(canvas)
    }
}





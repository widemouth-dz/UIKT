package wedo.widemouth.uikt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

@SuppressLint("ViewConstructor")
class StubView(context: Context) : View(context) {

    private var mInflatedViewRef: WeakReference<View>? = null

    var inflate: ScopeViewBuilder<View>? = null
    var inflatedId: Int = NO_ID

    init {
        visibility = GONE
        setWillNotDraw(true)
    }

    fun inflate(): View {
        val view = inflate?.invoke(context) ?: error("Stub must have a valid scope layout")
        if (inflatedId != NO_ID) view.id = inflatedId
        mInflatedViewRef = WeakReference(view)
        val stubParent = parent as? ViewGroup ?: error("Stub must have a non-null ViewGroup parent")
        stubParent.addView(view, stubParent.indexOfChild(this), layoutParams)
        return view
    }

    override fun setVisibility(visibility: Int) {
        if (mInflatedViewRef != null) {
            val view: View = mInflatedViewRef!!.get()
                ?: throw IllegalStateException("setVisibility called on un-referenced view")
            view.visibility = visibility
        } else {
            super.setVisibility(visibility)
            if (visibility == VISIBLE || visibility == INVISIBLE) {
                inflate()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) {
    }

    override fun dispatchDraw(canvas: Canvas) {}
}

fun <SL : LP> Scope<SL>.Stub(block: WidgetReceiver<StubView, SL>) =
    Widget(::StubView, block)





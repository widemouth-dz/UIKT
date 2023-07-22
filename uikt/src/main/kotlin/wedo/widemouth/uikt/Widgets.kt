@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager2.widget.ViewPager2
import wedo.widemouth.annotation.DslWidget
import wedo.widemouth.uikt.vieweffect.FrameLayoutExt
import wedo.widemouth.uikt.property.WRAP_CONTENT
import wedo.widemouth.uikt.vieweffect.AppCompatImageViewExt
import wedo.widemouth.uikt.vieweffect.ConstraintLayoutExt
import wedo.widemouth.uikt.vieweffect.CoordinatorLayoutExt
import wedo.widemouth.uikt.vieweffect.LinearLayoutExt
import wedo.widemouth.uikt.vieweffect.RelativeLayoutExt

/**
 * DslWidget partial applied functions, for example:
 * ```
 * Text(::boxLayout){ ... }(ctx)
 * ```
 */
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <reified V : View, reified VL : LP> Widget(
	noinline block: WidgetReceiver<V, VL>,
): ViewBuilder<V> = Widget(::viewConstructor, ::layoutConstructor, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <V : View> Widget(
	crossinline viewBuilder: ViewBuilder<V>,
	noinline block: WidgetReceiver<V, MarginLP>,
): ViewBuilder<V> = Widget(viewBuilder, ::marginLayout, block)

/** Partial applied function of `DslWidget(ctx, ...)`. */
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <V : View, VL : LP> Widget(
	crossinline viewBuilder: ViewBuilder<V>,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<V, VL>,
): ViewBuilder<V> = { ctx -> Widget(ctx, viewBuilder, widgetLayoutBuilder, block) }

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <reified V : View, reified VL : LP> Widget(
	ctx: Context,
	noinline block: WidgetReceiver<V, VL>,
): ViewBuilder<V> = Widget(ctx, ::viewConstructor, ::layoutConstructor, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <V : View> Widget(
	ctx: Context,
	crossinline viewBuilder: ViewBuilder<V>,
	noinline block: WidgetReceiver<V, MarginLP>,
): V = Widget(ctx, viewBuilder, ::marginLayout, block)

@DslWidget(ImageView::class)
@DslWidget(Button::class)
@DslWidget(TextView::class)
@DslWidget(EditText::class)
@DslWidget(ViewPager2::class)
@DslWidget(AppCompatImageViewExt::class)

@DslWidget(LinearLayout::class)
@DslWidget(FrameLayout::class)
@DslWidget(RelativeLayout::class)
@DslWidget(ConstraintLayout::class)
@DslWidget(CoordinatorLayout::class)

@DslWidget(LinearLayoutExt::class)
@DslWidget(FrameLayoutExt::class)
@DslWidget(RelativeLayoutExt::class)
@DslWidget(ConstraintLayoutExt::class)
@DslWidget(CoordinatorLayoutExt::class)
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <V : View, VL : LP> Widget(
	ctx: Context,
	crossinline viewBuilder: ViewBuilder<V>,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<V, VL>,
): V {
	val widget = viewBuilder(ctx)
	val widgetLayout = widgetLayoutBuilder(WRAP_CONTENT, WRAP_CONTENT)
	block(widget, widgetLayout)
	widget.layoutParams = widgetLayout
	return widget
}
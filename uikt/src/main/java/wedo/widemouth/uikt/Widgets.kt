@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.annotation.DslWidget

inline fun <VL : LP> Text(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_Text, VL>,
): _Text = Widget(ctx, ::_Text, widgetLayoutBuilder, block)

inline fun <VL : LP> Image(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_Image, VL>,
): _Image = Widget(ctx, ::_Image, widgetLayoutBuilder, block)

inline fun <VL : LP> Button(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_Button, VL>,
): _Button = Widget(ctx, ::_Button, widgetLayoutBuilder, block)

inline fun <VL : LP> EditText(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_EditText, VL>,
): _EditText = Widget(ctx, ::_EditText, widgetLayoutBuilder, block)

inline fun <VL : LP> ProgressBar(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_ProgressBar, VL>,
): _ProgressBar = Widget(ctx, ::_ProgressBar, widgetLayoutBuilder, block)

inline fun <VL : LP> Pager(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_Pager, VL>,
): _Pager = Widget(ctx, ::_Pager, widgetLayoutBuilder, block)

inline fun <VL : LP> LazyList(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_LazyList, VL>,
): _LazyList = Widget(ctx, ::_LazyList, widgetLayoutBuilder, block)

inline fun <VL : LP> View(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_View, VL>,
): _View = Widget(ctx, ::_View, widgetLayoutBuilder, block)

inline fun <VL : LP> Space(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_Space, VL>,
): _Space = Widget(ctx, ::_Space, widgetLayoutBuilder, block)

inline fun <VL : LP> ViewStub(
	ctx: Context,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<_ViewStub, VL>,
): _ViewStub = Widget(ctx, ::_ViewStub, widgetLayoutBuilder, block)

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

@DslGroup(
	[
		ConstraintLayout::class,
		FrameLayout::class,
		RelativeLayout::class,
		LinearLayout::class,
		CoordinatorLayout::class,

		TextView::class,
		Button::class,
		ImageButton::class,
		ImageView::class
	]
)
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <V : View, VL : LP> Widget(
	ctx: Context,
	crossinline viewBuilder: ViewBuilder<V>,
	crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
	noinline block: WidgetReceiver<V, VL>,
): V {
	val widget = viewBuilder(ctx)
	val widgetLayout = widgetLayoutBuilder()
	block(widget, widgetLayout)
	widget.layoutParams = widgetLayout
	return widget
}
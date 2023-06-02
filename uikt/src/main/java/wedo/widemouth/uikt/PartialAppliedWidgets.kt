@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.view.View
import android.widget.TextView

/**
 * DslWidget partial applied functions, for example:
 * ```
 * Text(::boxLayout){ ... }(ctx)
 * ```
 */

fun Text(block: WidgetReceiver<_Text, MarginLP>): ViewBuilder<TextView> =
    Widget(::_Text, ::marginLayout, block)

fun Image(block: WidgetReceiver<_Image, MarginLP>): ViewBuilder<_Image> =
    Widget(::_Image, ::marginLayout, block)

fun Button(block: WidgetReceiver<_Button, MarginLP>): ViewBuilder<_Button> =
    Widget(::_Button, ::marginLayout, block)

fun EditText(block: WidgetReceiver<_EditText, MarginLP>): ViewBuilder<_EditText> =
    Widget(::_EditText, ::marginLayout, block)

fun ProgressBar(block: WidgetReceiver<_ProgressBar, MarginLP>): ViewBuilder<_ProgressBar> =
    Widget(::_ProgressBar, ::marginLayout, block)

fun Pager(block: WidgetReceiver<_Pager, MarginLP>): ViewBuilder<_Pager> =
    Widget(::_Pager, ::marginLayout, block)

fun LazyList(block: WidgetReceiver<_LazyList, MarginLP>): ViewBuilder<_LazyList> =
    Widget(::_LazyList, ::marginLayout, block)

fun View(block: WidgetReceiver<_View, MarginLP>): ViewBuilder<_View> =
    Widget(::_View, ::marginLayout, block)

fun Space(block: WidgetReceiver<_Space, MarginLP>): ViewBuilder<_Space> =
    Widget(::_Space, ::marginLayout, block)

fun ViewStub(block: WidgetReceiver<_ViewStub, MarginLP>): ViewBuilder<_ViewStub> =
    Widget(::_ViewStub, ::marginLayout, block)

inline fun <VL : LP> Text(
    crossinline layoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_Text, VL>,
): ViewBuilder<_Text> = Widget(::_Text, layoutBuilder, block)

inline fun <VL : LP> Image(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_Image, VL>,
): ViewBuilder<_Image> = Widget(::_Image, widgetLayoutBuilder, block)

inline fun <VL : LP> Button(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_Button, VL>,
): ViewBuilder<_Button> = Widget(::_Button, widgetLayoutBuilder, block)

inline fun <VL : LP> EditText(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_EditText, VL>,
): ViewBuilder<_EditText> = Widget(::_EditText, widgetLayoutBuilder, block)

inline fun <VL : LP> ProgressBar(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_ProgressBar, VL>,
): ViewBuilder<_ProgressBar> = Widget(::_ProgressBar, widgetLayoutBuilder, block)

inline fun <VL : LP> Pager(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_Pager, VL>,
): ViewBuilder<_Pager> = Widget(::_Pager, widgetLayoutBuilder, block)

inline fun <VL : LP> LazyList(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_LazyList, VL>,
): ViewBuilder<_LazyList> = Widget(::_LazyList, widgetLayoutBuilder, block)

inline fun <VL : LP> View(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_View, VL>,
): ViewBuilder<_View> = Widget(::_View, widgetLayoutBuilder, block)

inline fun <VL : LP> Space(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_Space, VL>,
): ViewBuilder<_Space> = Widget(::_Space, widgetLayoutBuilder, block)

inline fun <VL : LP> ViewStub(
    crossinline widgetLayoutBuilder: LayoutBuilder<VL>,
    noinline block: WidgetReceiver<_ViewStub, VL>,
): ViewBuilder<_ViewStub> = Widget(::_ViewStub, widgetLayoutBuilder, block)

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
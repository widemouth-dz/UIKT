package wedo.widemouth.uikt

import android.view.View
import android.view.ViewGroup

/**
 * Used for building layout tree.
 *
 * @param SL The [LP] type for children in [Scope]
 * @property group The [ViewGroup] that children in [Scope] will be added to.
 * @property defaultLayout The [defaultLayout] for children in [Scope].
 * @author WideMouth
 * @since 2023/5/4
 */
@Suppress("FunctionName")
class Scope<out SL : LP>(
    @PublishedApi internal val group: ViewGroup,
    @PublishedApi internal val defaultLayout: LayoutBuilder<SL>
) {

    @SinceKotlin(ContextReceiverGenericSinceKotlin)
    inline fun <reified V : View> Widget(noinline block: WidgetReceiver<V, SL>): V =
        Widget(::viewConstructor, block)

    @SinceKotlin(ContextReceiverGenericSinceKotlin)
    inline fun <V : View> _Widget(
        noinline block: WidgetReceiver<V, SL>, crossinline viewBuilder: ViewBuilder<V>
    ): V = Widget(viewBuilder, block)

    @SinceKotlin(ContextReceiverGenericSinceKotlin)
    inline fun <V : View> Widget(
        crossinline viewBuilder: ViewBuilder<V>, noinline block: WidgetReceiver<V, SL>
    ): V = Widget(group.context, viewBuilder, defaultLayout, block).also(group::addView)

    @SinceKotlin(ContextReceiverGenericSinceKotlin)
    inline fun <reified G : ViewGroup, reified L : LP> Group(
        noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<L>)) SL.() -> Unit,
    ): G = Group(::viewConstructor, ::layoutConstructor, block)

    @SinceKotlin(ContextReceiverGenericSinceKotlin)
    inline fun <G : ViewGroup, L : LP> Group(
        crossinline groupBuilder: ViewBuilder<G>,
        noinline scopeLayoutBuilder: LayoutBuilder<L>,
        noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<L>)) SL.() -> Unit,
    ): G = Group(group.context, groupBuilder, scopeLayoutBuilder, defaultLayout, block)
        .also(group::addView)
}
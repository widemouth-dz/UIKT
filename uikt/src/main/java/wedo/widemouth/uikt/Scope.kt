@file:Suppress("FunctionName")

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
class Scope<out SL : LP>(
    @PublishedApi internal val group: ViewGroup,
    @PublishedApi internal val defaultLayout: LayoutBuilder<SL>
) {
    fun Text(block: WidgetReceiver<_Text, SL>): _Text =
        Widget(::_Text, block)

    fun Image(block: WidgetReceiver<_Image, SL>): _Image =
        Widget(::_Image, block)

    fun Button(block: WidgetReceiver<_Button, SL>): _Button =
        Widget(::_Button, block)

    fun EditText(block: WidgetReceiver<_EditText, SL>): _EditText =
        Widget(::_EditText, block)

    fun ProgressBar(block: WidgetReceiver<_ProgressBar, SL>): _ProgressBar =
        Widget(::_ProgressBar, block)

    fun Pager(block: WidgetReceiver<_Pager, SL>): _Pager =
        Widget(::_Pager, block)

    fun LazyList(block: WidgetReceiver<_LazyList, SL>): _LazyList =
        Widget(::_LazyList, block)

    fun View(block: WidgetReceiver<_View, SL>): _View =
        Widget(::_View, block)

    fun Space(block: WidgetReceiver<_Space, SL>): _Space =
        Widget(::_Space, block)

    fun ViewStub(block: WidgetReceiver<_ViewStub, SL>): _ViewStub =
        Widget(::_ViewStub, block)

    fun Box(block: BoxReceiver<SL>): _Box =
        Group(::_Box, ::boxLayout, block)

    fun Constraint(block: ConstraintReceiver<SL>): _Constraint =
        Group(::_Constraint, ::constraintLayout, block)

    fun Relative(block: RelativeReceiver<SL>): _Relative =
        Group(::_Relative, ::relativeLayout, block)

    fun Row(block: RowReceiver<SL>): _Row =
        Group(::_Row, ::linearLayout, block)

    fun Column(block: ColumnReceiver<SL>): _Column =
        Group(::_column, ::linearLayout, block)

    fun ScrollRow(block: ScrollRowReceiver<SL>): _ScrollRow =
        Group(::_ScrollRow,::marginLayout, block)

    fun ScrollColumn(block: ScrollColumnReceiver<SL>): _ScrollColumn =
        Group(::_ScrollColumn,::marginLayout, block)

    fun NestedScrollColumn(block: NestedScrollColumnReceiver<SL>): _NestedScrollColumn =
        Group(::_NestedScrollColumn,::marginLayout, block)

    @Deprecated(
        message = "Use `DslWidget` instead for viewConstructor performance.",
        replaceWith = ReplaceWith("DslWidget(::viewConstructor, block)")
    )
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
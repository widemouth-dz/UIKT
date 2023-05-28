@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.content.Context
import android.view.ViewGroup

fun Box(ctx: Context, block: BoxReceiver<MarginLP>): _Box =
    Root(ctx, ::_Box, ::boxLayout, block)

fun Constraint(ctx: Context, block: ConstraintReceiver<MarginLP>): _Constraint =
    Root(ctx, ::_Constraint, ::constraintLayout, block)

fun Relative(ctx: Context, block: RelativeReceiver<MarginLP>): _Relative =
    Root(ctx, ::_Relative, ::relativeLayout, block)

fun Row(ctx: Context, block: RowReceiver<MarginLP>): _Row =
    Root(ctx, ::_Row, ::linearLayout, block)

fun Column(ctx: Context, block: ColumnReceiver<MarginLP>): _Column =
    Root(ctx, ::_column, ::linearLayout, block)

fun ScrollRow(ctx: Context, block: ScrollRowReceiver<MarginLP>): _ScrollRow =
    Root(ctx, ::_ScrollRow, ::linearLayout, block)

fun ScrollColumn(ctx: Context, block: ScrollColumnReceiver<MarginLP>): _ScrollColumn =
    Root(ctx, ::_ScrollColumn, ::linearLayout, block)

fun NestedScrollColumn(
    ctx: Context, block: NestedScrollColumnReceiver<MarginLP>
): _NestedScrollColumn = Root(ctx, ::_NestedScrollColumn, ::linearLayout, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <reified G : ViewGroup, reified GSL : LP> Root(
    ctx: Context,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit,
): G = Root(ctx, ::viewConstructor, ::layoutConstructor, block)


@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <G : ViewGroup, GSL : LP> Root(
    ctx: Context,
    crossinline groupBuilder: ViewBuilder<G>,
    noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit,
): G = Group(ctx, groupBuilder, scopeLayoutBuilder, ::rootLayout, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <G : ViewGroup, GL : LP, GSL : LP> Root(
    ctx: Context,
    crossinline groupBuilder: ViewBuilder<G>,
    noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
    groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) GL.() -> Unit,
): G = Group(ctx, groupBuilder, scopeLayoutBuilder, groupLayoutBuilder, block)

@PublishedApi
@SinceKotlin(ContextReceiverGenericSinceKotlin)
internal inline fun <G : ViewGroup, GL : LP, GSL : LP> Group(
    ctx: Context,
    crossinline groupBuilder: ViewBuilder<G>,
    noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
    groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>))  GL.() -> Unit,
): G {
    val group = groupBuilder(ctx)
    val groupLayout = groupLayoutBuilder()
    block(group, Scope(group, scopeLayoutBuilder), groupLayout)
    group.layoutParams = groupLayout
    return group
}

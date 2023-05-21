@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.view.ViewGroup

/**
 * Group partial applied functions, for example:
 * ```
 * 	Box{ ... }(ctx)
 * ```
 * or
 * ```
 * 	val ui =
 * 		Box{
 * 			...
 * 		}
 * 	val box = ui(ctx)
 * ```
 */

fun Box(block: BoxReceiver<MarginLP>): ScopeViewBuilder<_Box> =
    Root(::_Box, ::boxLayout, block)

fun Constraint(block: ConstraintReceiver<MarginLP>): ScopeViewBuilder<_Constraint> =
    Root(::_Constraint, ::constraintLayout, block)

fun Relative(block: RelativeReceiver<MarginLP>): ScopeViewBuilder<_Relative> =
    Root(::_Relative, ::relativeLayout, block)

fun Row(block: RowReceiver<MarginLP>): ScopeViewBuilder<_Row> =
    Root(::_Row, ::linearLayout, block)

fun Column(block: ColumnReceiver<MarginLP>): ScopeViewBuilder<_Column> =
    Root(::_column, ::linearLayout, block)

fun ScrollRow(block: ScrollRowReceiver<MarginLP>): ScopeViewBuilder<_ScrollRow> =
    Root(::_ScrollRow, ::marginLayout, block)

fun ScrollColumn(block: ScrollColumnReceiver<MarginLP>): ScopeViewBuilder<_ScrollColumn> =
    Root(::_ScrollColumn, ::marginLayout, block)

fun NestedScrollColumn(block: NestedScrollColumnReceiver<MarginLP>): ScopeViewBuilder<_NestedScrollColumn> =
    Root(::_NestedScrollColumn, ::marginLayout, block)

inline fun <GL : LP> Box(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: BoxReceiver<GL>
): ScopeViewBuilder<_Box> =
    Root(::_Box, ::boxLayout, groupLayoutBuilder, block)

inline fun <GL : LP> Constraint(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: ConstraintReceiver<GL>
): ScopeViewBuilder<_Constraint> =
    Root(::_Constraint, ::constraintLayout, groupLayoutBuilder, block)

inline fun <GL : LP> Relative(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: RelativeReceiver<GL>
): ScopeViewBuilder<_Relative> =
    Root(::_Relative, ::relativeLayout, groupLayoutBuilder, block)

inline fun <GL : LP> Row(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: RowReceiver<GL>
): ScopeViewBuilder<_Row> =
    Root(::_Row, ::linearLayout, groupLayoutBuilder, block)

inline fun <GL : LP> Column(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: ColumnReceiver<GL>
): ScopeViewBuilder<_Column> =
    Root(::_column, ::linearLayout, groupLayoutBuilder, block)

inline fun <GL : LP> ScrollRow(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: ScrollRowReceiver<GL>
): ScopeViewBuilder<_ScrollRow> =
    Root(::_ScrollRow, ::marginLayout, groupLayoutBuilder, block)

inline fun <GL : LP> ScrollColumn(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: ScrollColumnReceiver<GL>
): ScopeViewBuilder<_ScrollColumn> =
    Root(::_ScrollColumn, ::marginLayout, groupLayoutBuilder, block)

inline fun <GL : LP> NestedScrollColumn(
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: NestedScrollColumnReceiver<GL>
): ScopeViewBuilder<_NestedScrollColumn> =
    Root(::_NestedScrollColumn, ::marginLayout, groupLayoutBuilder, block)

@Deprecated(
    message = "Use `Root` instead for viewConstructor and layoutConstructor performance. ",
    replaceWith = ReplaceWith("Root(::viewConstructor, ::layoutConstructor, block)")
)
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <reified G : ViewGroup, reified GSL : LP> Root(
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit
): ScopeViewBuilder<G> =
    Root(::viewConstructor, ::layoutConstructor, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <G : ViewGroup, GSL : LP> Root(
    crossinline groupBuilder: ViewBuilder<G>,
    noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit
): ScopeViewBuilder<G> =
    Root(groupBuilder, scopeLayoutBuilder, ::marginLayout, block)

/** Partial applied function of `Group(ctx, ...)`. */
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <G : ViewGroup, GL : LP, GSL : LP> Root(
    crossinline groupBuilder: ViewBuilder<G>,
    noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
    crossinline groupLayoutBuilder: LayoutBuilder<GL>,
    noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) GL.() -> Unit,
): ScopeViewBuilder<G> = { ctx ->
    Group(ctx, groupBuilder, scopeLayoutBuilder, groupLayoutBuilder, block)
}

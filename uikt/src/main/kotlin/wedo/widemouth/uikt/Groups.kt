@file:Suppress("FunctionName")

package wedo.widemouth.uikt

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.uikt.vieweffect.ConstraintLayoutExt
import wedo.widemouth.uikt.vieweffect.CoordinatorLayoutExt
import wedo.widemouth.uikt.vieweffect.FrameLayoutExt
import wedo.widemouth.uikt.vieweffect.LinearLayoutExt
import wedo.widemouth.uikt.vieweffect.RelativeLayoutExt
import wedo.widemouth.uikt.property.WRAP_CONTENT

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
@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <reified G : ViewGroup, reified GSL : LP> Root(
	noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit,
): ScopeViewBuilder<G> =
	Root(::viewConstructor, ::layoutConstructor, block)

@SinceKotlin(ContextReceiverGenericSinceKotlin)
inline fun <G : ViewGroup, GSL : LP> Root(
	crossinline groupBuilder: ViewBuilder<G>,
	noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
	noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) MarginLP.() -> Unit,
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
	groupBuilder: ViewBuilder<G>,
	noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
	groupLayoutBuilder: LayoutBuilder<GL>,
	noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>)) GL.() -> Unit,
): G = Group(ctx, groupBuilder, scopeLayoutBuilder, groupLayoutBuilder, block)

@DslGroup(ScrollView::class)
@DslGroup(NestedScrollView::class)
@DslGroup(HorizontalScrollView::class)

@DslGroup(LinearLayout::class)
@DslGroup(FrameLayout::class)
@DslGroup(RelativeLayout::class)
@DslGroup(ConstraintLayout::class)
@DslGroup(CoordinatorLayout::class)

@DslGroup(LinearLayoutExt::class)
@DslGroup(FrameLayoutExt::class)
@DslGroup(RelativeLayoutExt::class)
@DslGroup(ConstraintLayoutExt::class)
@DslGroup(CoordinatorLayoutExt::class)
@PublishedApi
@SinceKotlin(ContextReceiverGenericSinceKotlin)
internal inline fun <G : ViewGroup, GL : LP, GSL : LP> Group(
	ctx: Context,
	groupBuilder: ViewBuilder<G>,
	noinline scopeLayoutBuilder: LayoutBuilder<GSL>,
	groupLayoutBuilder: LayoutBuilder<GL>,
	noinline block: @LayoutMarker context((@ViewMarker G), (@ScopeMarker Scope<GSL>))  GL.() -> Unit,
): G {
	val group = groupBuilder(ctx)
	val groupLayout = groupLayoutBuilder(WRAP_CONTENT, WRAP_CONTENT)
	block(group, Scope(group, scopeLayoutBuilder), groupLayout)
	group.layoutParams = groupLayout
	return group
}

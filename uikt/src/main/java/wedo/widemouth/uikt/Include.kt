package wedo.widemouth.uikt

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes

typealias GroupMerge = MergeReceiver<MarginLP>
typealias BoxMerge = MergeReceiver<BoxLP>
typealias ConstraintMerge = MergeReceiver<ConstraintLP>
typealias RelativeMerge = MergeReceiver<RelativeLP>
typealias ColumnMerge = MergeReceiver<LinearLP>
typealias RowMerge = ColumnMerge
typealias MergeReceiver<L> = @ScopeMarker Scope<L>.() -> Unit

fun GroupMerge(merge: GroupMerge): GroupMerge = merge
fun BoxMerge(merge: BoxMerge): BoxMerge = merge
fun ConstraintMerge(merge: ConstraintMerge): ConstraintMerge = merge
fun RelativeMerge(merge: RelativeMerge): RelativeMerge = merge
fun ColumnMerge(merge: GroupMerge): ColumnMerge = merge
fun RowMerge(merge: RowMerge): RowMerge = merge

fun <V : View> Scope<*>.include(scopeViewBuilder: ScopeViewBuilder<V>): V =
    scopeViewBuilder(group.context).also(group::addView)

fun <V : View, SL : LP> Scope<SL>.include(
    scopeViewBuilder: ScopeViewBuilder<V>, block: WidgetReceiver<V, SL>,
) = Widget(scopeViewBuilder, block)

fun Scope<*>.include(@LayoutRes layoutResId: Int): View =
    LayoutInflater.from(group.context).inflate(layoutResId, group, true)

/** The root tag in [layoutResId] must be a valid view, use `include(Int)` if the root tag is <merge>. */
fun <SL : LP> Scope<SL>.include(
    @LayoutRes layoutResId: Int, block: WidgetReceiver<View, SL>,
): View = _Widget(block) {
    LayoutInflater.from(it).inflate(layoutResId, group, false).also(group::addView)
}

/** The root tag in [layoutResId] must be a valid view, use `include(Int)` if the root tag is <merge>. */
//fun <V : View, SL : LP> Scope<SL>.include(
//    @LayoutRes layoutResId: Int, block: WidgetReceiver<V, SL>,
//): V = _Widget(block) {
//    @Suppress("UNCHECKED_CAST")
//    LayoutInflater.from(it).inflate(layoutResId, group, false).also(group::addView) as V
//}

fun <SL : LP> Scope<SL>.include(block: MergeReceiver<SL>) = block()

//context (Scope<SL>)
//fun <SL : ViewGroup.LayoutParams> MergeReceiver<SL>.merge() = this@Scope.this@merge()

context (Scope<SL>)
        operator fun <SL : LP> MergeReceiver<SL>.invoke() = this@Scope.this@invoke()
package wedo.widemouth.uikt

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes

typealias GroupScope = ScopeReceiver<MarginLP>
typealias BoxScope = ScopeReceiver<BoxLP>
typealias ConstraintScope = ScopeReceiver<ConstraintLP>
typealias RelativeScope = ScopeReceiver<RelativeLP>
typealias ColumnScope = ScopeReceiver<LinearLP>
typealias RowScope = ColumnScope
typealias ScopeReceiver<L> = @ScopeMarker Scope<L>.() -> Unit

fun GroupScope(scope: GroupScope): GroupScope = scope
fun BoxScope(scope: BoxScope): BoxScope = scope
fun ConstraintScope(scope: ConstraintScope): ConstraintScope = scope
fun RelativeScope(scope: RelativeScope): RelativeScope = scope
fun ColumnScope(scope: GroupScope): ColumnScope = scope
fun RowScope(scope: RowScope): RowScope = scope

fun <V : View, SL : LP> Scope<SL>.merge(
	scopeViewBuilder: ScopeViewBuilder<V>, block: WidgetReceiver<V, SL>,
) = Widget(scopeViewBuilder, block)

fun <V : View, SL : LP> Scope<SL>.merge(
	@LayoutRes layoutResId: Int, block: WidgetReceiver<V, SL>,
) = _Widget(block) {
	@Suppress("UNCHECKED_CAST")
	LayoutInflater.from(it).inflate(layoutResId, group, true) as V
}

fun <SL : LP> Scope<SL>.merge(block: ScopeReceiver<SL>) = block()

//context (Scope<SL>)
//fun <SL : ViewGroup.LayoutParams> ScopeReceiver<SL>.merge() = this@Scope.this@merge()

context (Scope<SL>)
        operator fun <SL : LP> ScopeReceiver<SL>.invoke() = this@Scope.this@invoke()
package wedo.widemouth.uikt

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

fun <SL : LP> Scope<SL>.merge(block: ScopeReceiver<SL>) = block()

//context (Scope<SL>)
//fun <SL : ViewGroup.LayoutParams> ScopeReceiver<SL>.merge() = this@Scope.this@merge()

context (Scope<SL>)
        operator fun <SL : LP> ScopeReceiver<SL>.invoke() = this@Scope.this@invoke()
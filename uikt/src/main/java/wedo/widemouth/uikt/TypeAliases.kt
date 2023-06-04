package wedo.widemouth.uikt

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import wedo.widemouth.uikt.decoration.ConstraintLayoutExt
import wedo.widemouth.uikt.decoration.FrameLayoutExt
import wedo.widemouth.uikt.decoration.LinearLayoutExt
import wedo.widemouth.uikt.decoration.RelativeLayoutExt


typealias LP = ViewGroup.LayoutParams
typealias MarginLP = ViewGroup.MarginLayoutParams
typealias LinearLP = LinearLayoutCompat.LayoutParams
typealias BoxLP = FrameLayout.LayoutParams
typealias ConstraintLP = ConstraintLayout.LayoutParams
typealias RelativeLP = RelativeLayout.LayoutParams

typealias ViewBuilder<V> = (ctx: Context) -> V
typealias LayoutBuilder<L> = (Int, Int) -> L
typealias ScopeViewBuilder<V> = ViewBuilder<V>

@SinceKotlin(ContextReceiverSinceKotlin)
typealias WidgetReceiver<V, L> = Receiver2<@ViewMarker V, @ScopeMarker @LayoutMarker L>
// Not supported since `Kotlin 1.7.20`.
// typealias GroupReceiver<G, SL, L> = Receiver3<@ViewMarker G, @ScopeMarker SL, @LayoutMarker L>

@SinceKotlin(ContextReceiverSinceKotlin)
typealias Receiver2<R1, R2> = context(R1) R2.() -> Unit
// Not supported since `Kotlin 1.7.20`.
// typealias Receiver3<R1, R2, R3> = context(R1, R2) R3.() -> Unit

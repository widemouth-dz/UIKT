package wedo.widemouth.uikt

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import wedo.widemouth.uikt.decoration.ConstraintLayoutExt
import wedo.widemouth.uikt.decoration.FrameLayoutExt
import wedo.widemouth.uikt.decoration.ImageViewExt
import wedo.widemouth.uikt.decoration.LinearLayoutExt
import wedo.widemouth.uikt.decoration.RelativeLayoutExt

typealias _Box = FrameLayoutExt
typealias _Constraint = ConstraintLayoutExt
typealias _Relative = RelativeLayoutExt
typealias _Row = LinearLayoutExt
typealias _Column = _Row

typealias _ScrollRow = HorizontalScrollView
typealias _ScrollColumn = ScrollView
typealias _NestedScrollColumn = NestedScrollView

typealias _Text = TextView
typealias _Image = ImageViewExt
typealias _Button = Button
typealias _EditText = EditText
typealias _ProgressBar = ProgressBar
typealias _Pager = ViewPager2
typealias _LazyList = RecyclerView
typealias _View = View
typealias _Space = Space
typealias _ViewStub = ViewStub

typealias LP = ViewGroup.LayoutParams
typealias MarginLP = ViewGroup.MarginLayoutParams
typealias LinearLP = LinearLayoutCompat.LayoutParams
typealias BoxLP = FrameLayout.LayoutParams
typealias ConstraintLP = ConstraintLayout.LayoutParams
typealias RelativeLP = RelativeLayout.LayoutParams

typealias BoxReceiver<SL> = @LayoutMarker context((@ViewMarker _Box), (@ScopeMarker Scope<BoxLP>)) SL.() -> Unit
typealias ConstraintReceiver<SL> = @LayoutMarker context((@ViewMarker _Constraint), (@ScopeMarker Scope<ConstraintLP>)) SL.() -> Unit
typealias RelativeReceiver<SL> = @LayoutMarker context((@ViewMarker _Relative), (@ScopeMarker Scope<RelativeLP>)) SL.() -> Unit
typealias ColumnReceiver<SL> = @LayoutMarker context((@ViewMarker _Column), (@ScopeMarker Scope<LinearLP>)) SL.() -> Unit
typealias RowReceiver<SL> = ColumnReceiver<SL>

typealias ScrollColumnReceiver<SL> = @LayoutMarker context((@ViewMarker _ScrollColumn), (@ScopeMarker Scope<MarginLP>)) SL.() -> Unit
typealias ScrollRowReceiver<SL> = @LayoutMarker context((@ViewMarker _ScrollRow), (@ScopeMarker Scope<MarginLP>)) SL.() -> Unit
typealias NestedScrollColumnReceiver<SL> = @LayoutMarker context((@ViewMarker _NestedScrollColumn), (@ScopeMarker Scope<MarginLP>)) SL.() -> Unit

typealias ViewBuilder<V> = (ctx: Context) -> V
typealias LayoutBuilder<L> = () -> L
typealias ScopeViewBuilder<V> = ViewBuilder<V>

@SinceKotlin(ContextReceiverSinceKotlin)
typealias WidgetReceiver<V, L> = Receiver2<@ViewMarker V, @ScopeMarker @LayoutMarker L>
// Not supported since `Kotlin 1.7.20`.
// typealias GroupReceiver<G, SL, L> = Receiver3<@ViewMarker G, @ScopeMarker SL, @LayoutMarker L>

@SinceKotlin(ContextReceiverSinceKotlin)
typealias Receiver2<R1, R2> = context(R1) R2.() -> Unit
// Not supported since `Kotlin 1.7.20`.
// typealias Receiver3<R1, R2, R3> = context(R1, R2) R3.() -> Unit

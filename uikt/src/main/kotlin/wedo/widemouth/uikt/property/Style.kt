package wedo.widemouth.uikt.property

import android.view.View

typealias DefStyle<T> = (T) -> Unit

fun <V : View> defStyle(block: V.() -> Unit): DefStyle<V> = { it.block() }
package wedo.widemouth.uikt.graphics

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * For [ColorStateList] and [StateListDrawable].
 *
 * @author WideMouth
 * @since 2022/12/7
 */
open class ViewState private constructor(vararg states: Int) {
    val stateSet: IntArray = states

    object FOCUSED : ViewState(android.R.attr.state_focused)
    object SELECTED : ViewState(android.R.attr.state_selected)
    object CHECKED : ViewState(android.R.attr.state_checked)
    object PRESSED : ViewState(android.R.attr.state_pressed)
    object ENABLED : ViewState(android.R.attr.state_enabled)

    /* Use it last since it always matches. */
    object NORMAL : ViewState(0)

    /* ![FOCUSED] means [UNFOCUSED] */
    operator fun not() = ViewState(*stateSet.map { -it }.toIntArray())

    /* [FOCUSED] + [SELECTED] indicates the state of [FOCUSED] and [SELECTED] */
    operator fun plus(other: ViewState) = ViewState(*(stateSet + other.stateSet))
}

/** [StateListDrawable] building factory. */
fun StateListDrawable(vararg stateDrawables: Pair<ViewState, Drawable>) =
    StateListDrawable().apply {
        stateDrawables.forEach { addState(it.first.stateSet, it.second) }
    }

/** [ColorStateList] building factory. */
fun ColorStateList(vararg stateColors: Pair<ViewState, Int>) =
    ColorStateList(
        stateColors.map { it.first.stateSet }.toTypedArray(),
        stateColors.map { it.second }.toIntArray()
    )

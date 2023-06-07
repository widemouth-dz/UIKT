package wedo.widemouth.uikt.property

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import wedo.widemouth.uikt.GetterUnSupported
import wedo.widemouth.uikt.GetterUnSupportedMessage

const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

const val VERTICAL = LinearLayout.VERTICAL
const val HORIZONTAL = LinearLayout.HORIZONTAL

const val PARENT_ID = ConstraintLayout.LayoutParams.PARENT_ID

const val CHAIN_PACKED = ConstraintLayout.LayoutParams.CHAIN_PACKED
const val CHAIN_SPREAD = ConstraintLayout.LayoutParams.CHAIN_SPREAD
const val CHAIN_SPREAD_INSIDE = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE

var ViewGroup.LayoutParams.size: Int
    set(value) {
        width = value
        height = value
    }
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var ViewGroup.MarginLayoutParams.margin: Int
    set(value) = setMargins(value, value, value, value)
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var ConstraintLayout.LayoutParams.matchTo: Int
    set(value) {
        width = 0
        height = 0
        centerTo = value
    }
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var ConstraintLayout.LayoutParams.centerTo: Int
    set(value) {
        centerHorizontallyTo = value
        centerVerticallyTo = value
    }
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var ConstraintLayout.LayoutParams.centerHorizontallyTo: Int
    set(value) {
        leftToLeft = value
        rightToRight = value
    }
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var ConstraintLayout.LayoutParams.centerVerticallyTo: Int
    set(value) {
        topToTop = value
        bottomToBottom = value
    }
    @Deprecated(GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

infix fun ConstraintLayout.LayoutParams.matchTo(id: Int) {
    width = 0
    height = 0
    this centerTo id
}

infix fun ConstraintLayout.LayoutParams.centerTo(id: Int) {
    this centerHorizontallyTo id
    this centerVerticallyTo id
}

infix fun ConstraintLayout.LayoutParams.centerHorizontallyTo(id: Int) {
    leftToLeft = id
    rightToRight = id
}

infix fun ConstraintLayout.LayoutParams.centerVerticallyTo(id: Int) {
    topToTop = id
    bottomToBottom = id
}
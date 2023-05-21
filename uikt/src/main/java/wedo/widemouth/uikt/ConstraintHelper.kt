package wedo.widemouth.uikt

import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.Group
import androidx.constraintlayout.widget.Guideline


/**
 * @author WideMouth
 * @since 2023/5/21
 */

typealias _Flow = Flow
typealias _Group = Group
typealias _Guideline = Guideline
typealias _Layer = Layer
typealias _Barrier = Barrier

fun Scope<ConstraintLP>.Flow(block: WidgetReceiver<_Flow, ConstraintLP>) =
    Widget(::_Flow, block)

fun Scope<ConstraintLP>.Group(block: WidgetReceiver<_Group, ConstraintLP>) =
    Widget(::_Group, block)

fun Scope<ConstraintLP>.Guideline(block: WidgetReceiver<_Guideline, ConstraintLP>) =
    Widget(::_Guideline, block)

fun Scope<ConstraintLP>.Layer(block: WidgetReceiver<_Layer, ConstraintLP>) =
    Widget(::_Layer, block)

fun Scope<ConstraintLP>.Barrier(block: WidgetReceiver<_Barrier, ConstraintLP>) =
    Widget(::_Barrier, block)

fun Flow.referencedIds(vararg ids: Int) {
    referencedIds = ids
}

var Flow.orientation: Int
    set(value) = setOrientation(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.verticalStyle: Int
    set(value) = setVerticalStyle(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.horizontalStyle: Int
    set(value) = setHorizontalStyle(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.wrapMode: Int
    set(value) = setWrapMode(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.maxElementsWrap: Int
    set(value) = setMaxElementsWrap(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.verticalGap: Int
    set(value) = setVerticalGap(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.horizontalGap: Int
    set(value) = setHorizontalGap(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.verticalAlign: Int
    set(value) = setVerticalAlign(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.horizontalAlign: Int
    set(value) = setHorizontalAlign(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.firstVerticalBias: Float
    set(value) = setFirstVerticalBias(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.firstHorizontalBias: Float
    set(value) = setFirstHorizontalBias(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.verticalBias: Float
    set(value) = setVerticalBias(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.horizontalBias: Float
    set(value) = setHorizontalBias(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.firstVerticalStyle: Int
    set(value) = setFirstVerticalStyle(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Flow.firstHorizontalStyle: Int
    set(value) = setFirstHorizontalStyle(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Guideline.begin: Int
    set(value) = setGuidelineBegin(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Guideline.end: Int
    set(value) = setGuidelineEnd(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported

var Guideline.percent: Float
    set(value) = setGuidelinePercent(value)
    @Deprecated(message = GetterUnSupportedMessage, level = DeprecationLevel.ERROR)
    get() = GetterUnSupported
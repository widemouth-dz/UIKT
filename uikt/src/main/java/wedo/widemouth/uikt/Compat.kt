package wedo.widemouth.uikt

import android.view.View

/** `context(T)` is not supported before `Kotlin 1.7.20`. */
const val ContextReceiverGenericSinceKotlin = "1.7"

/** `context` is supported since `Kotlin 1.6.20`. */
const val ContextReceiverSinceKotlin = "1.6"

internal const val GetterUnSupportedMessage = "The getter is unSupported"
internal val GetterUnSupported: Nothing get() = error(GetterUnSupportedMessage)

val <T : View> T.view: T get() = this@T
val <T : Scope<L>, L> T.scope get() = this@T
val <T : LP> T.layout get() = this@T
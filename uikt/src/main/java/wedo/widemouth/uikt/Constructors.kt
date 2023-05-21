package wedo.widemouth.uikt

import android.content.Context
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import wedo.widemouth.uikt.property.WRAP_CONTENT
import java.lang.reflect.Constructor
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class Remember<P, V>(
	private val keySelector: (P) -> Any? = { it },
	private val calculation: (P) -> V
) : ReadOnlyProperty<Any?, (P) -> V> {

	private val map = mutableMapOf<Any?, V>()
	override fun getValue(thisRef: Any?, property: KProperty<*>): (P) -> V = {
		map.getOrPut(keySelector(it)) { calculation(it) }
	}
}

@PublishedApi
internal val sViewConstructor: (Class<*>) -> Constructor<*> by Remember {
	it.getConstructor(Context::class.java)
}

@PublishedApi
internal val sLayoutConstructor: (Class<*>) -> Constructor<*> by Remember {
	it.getConstructor(Int::class.java, Int::class.java)
}

@PublishedApi
internal inline fun <reified V : View> viewConstructor(context: Context): V =
	sViewConstructor(V::class.java).newInstance(context) as V

@PublishedApi
internal inline fun <reified L : LP> layoutConstructor(
	width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT
): L = sLayoutConstructor(L::class.java).newInstance(width, height) as L

@PublishedApi
internal fun _column(ctx: Context) = _Column(ctx).apply { orientation = LinearLayoutCompat.VERTICAL }
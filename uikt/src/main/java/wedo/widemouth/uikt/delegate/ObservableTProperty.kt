package wedo.widemouth.uikt.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Excerpt from [kotlin.properties.ObservableProperty],
 * The only difference: declare the type of object which owns the delegated property to customized for it.
 *
 * see [kotlin.properties.ObservableProperty], [ReadWriteProperty]
 *
 * @author WideMouth
 * @since 2022/12/4
 */
abstract class ObservableTProperty<T, V>(initialValue: V) : ReadWriteProperty<T, V> {
	private var value = initialValue

	protected open fun beforeChange(
		thisRef: T, property: KProperty<*>, oldValue: V, newValue: V
	): Boolean = true

	protected open fun afterChange(thisRef: T, property: KProperty<*>, oldValue: V, newValue: V) {}

	override fun getValue(thisRef: T, property: KProperty<*>): V = value

	override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
		val oldValue = this.value
		if (!beforeChange(thisRef, property, oldValue, value)) return
		this.value = value
		afterChange(thisRef, property, oldValue, value)
	}
}
package wedo.widemouth.uikt.delegate

import android.view.View
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * The delegates for UI related view properties like color, size.
 * Note: Call re-draw or re-layout only when value changes.
 *
 * @author WideMouth
 * @since 2022/12/4
 */
object UIDelegates {
	fun <T> simple(
		initialValue: T,
		draw: Boolean = true,
		layout: Boolean = false,
	): ReadWriteProperty<View, T> = UIProperty(initialValue, draw, layout)

	inline fun <T> observable(
		initialValue: T,
		draw: Boolean = true,
		layout: Boolean = false,
		crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
	): ReadWriteProperty<View, T> =
		object : UIProperty<T>(initialValue, draw, layout) {
			override fun afterChange(
				thisRef: View,
				property: KProperty<*>,
				oldValue: T,
				newValue: T
			) =
				onChange(property, oldValue, newValue)
		}

	inline fun <V> vetoable(
		initialValue: V,
		draw: Boolean = true,
		layout: Boolean = false,
		crossinline onChange: (value: V) -> Boolean
	): ReadWriteProperty<View, V> =
		object : UIProperty<V>(initialValue, draw, layout) {
			override fun beforeChange(
				thisRef: View,
				property: KProperty<*>,
				oldValue: V,
				newValue: V
			): Boolean = onChange(newValue)
		}


	inline fun <V> changeable(
		initialValue: V,
		draw: Boolean = true,
		layout: Boolean = false,
		crossinline beforeChange: (property: KProperty<*>, oldValue: V, newValue: V) -> Boolean,
		crossinline afterChange: (property: KProperty<*>, oldValue: V, newValue: V) -> Unit
	): ReadWriteProperty<View, V> =
		object : UIProperty<V>(initialValue, draw, layout) {
			override fun beforeChange(
				thisRef: View, property: KProperty<*>, oldValue: V, newValue: V
			): Boolean = beforeChange(property, oldValue, newValue)

			override fun afterChange(
				thisRef: View, property: KProperty<*>, oldValue: V, newValue: V
			) = afterChange(property, oldValue, newValue)
		}

}

open class UIProperty<V>(
	initialValue: V, val draw: Boolean = true, val layout: Boolean = false
) : ObservableTProperty<View, V>(initialValue) {
	override fun afterChange(thisRef: View, property: KProperty<*>, oldValue: V, newValue: V) {
		super.afterChange(thisRef, property, oldValue, newValue)
		if (oldValue != newValue) {
			if (draw) thisRef.invalidate()
			if (layout) thisRef.requestLayout()
		}
	}
}
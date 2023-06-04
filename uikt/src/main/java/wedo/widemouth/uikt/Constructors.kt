package wedo.widemouth.uikt

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import wedo.widemouth.uikt.property.WRAP_CONTENT
import java.lang.reflect.Constructor

@PublishedApi
internal val sViewConstructor: (Class<*>) -> Constructor<*> = remember {
	it.getConstructor(Context::class.java)
}

@PublishedApi
internal val sLayoutConstructor: (Class<*>) -> Constructor<*> = remember {
	it.getConstructor(Int::class.java, Int::class.java)
}

@PublishedApi
internal inline fun <reified V : View> viewConstructor(context: Context): V =
	sViewConstructor(V::class.java).newInstance(context) as V

@PublishedApi
internal inline fun <reified L : LP> layoutConstructor(
	width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT
): L = sLayoutConstructor(L::class.java).newInstance(width, height) as L

//@PublishedApi
//internal fun _column(ctx: Context) = _Column(ctx).apply { orientation = LinearLayout.VERTICAL }
package wedo.widemouth.uikt.app

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.LinearLayoutCompat
import wedo.widemouth.uikt.property.WRAP_CONTENT
import wedo.widemouth.uikt.property.dp
import wedo.widemouth.uikt.property.margin

class LinearScope : Scope() {
	override val Modify get() = LinearLayoutCompat.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}

open class Scope {
	open val Modify get() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

	fun <T : LayoutParams> T.size(width: Int, height: Int = width) = apply {
		this.width = width
		this.height = height
	}

	fun <T : LayoutParams> T.margin(width: Int, height: Int = width) = apply {
		this.width = width
		this.height = height
	}
}

fun Text(
	text: String = "",
	textSize: Float = 13f,
	textColor: Int = Color.BLACK,
	modify: ViewGroup.LayoutParams,
) {

}


fun P2( layout: MarginLayoutParams.() -> Unit={},view: View.() -> Unit) {

}

val view1 =
	P2(
		layout = {
			margin = 1.dp
		}
	) {

	}


fun scope(block: LinearScope.() -> Unit) {

}

fun test() = scope {
	Text(
		text = "Title",
		textSize = 15f,
		textColor = Color.WHITE,
		Modify.size(3.dp)
	)
}
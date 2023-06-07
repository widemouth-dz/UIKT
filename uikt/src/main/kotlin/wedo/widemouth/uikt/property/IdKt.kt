package wedo.widemouth.uikt.property

import android.view.View

fun createIds() = Ids()

fun createId() = View.generateViewId()

class Ids internal constructor() {
	operator fun component1() = createId()
	operator fun component2() = createId()
	operator fun component3() = createId()
	operator fun component4() = createId()
	operator fun component5() = createId()
	operator fun component6() = createId()
	operator fun component7() = createId()
	operator fun component8() = createId()
	operator fun component9() = createId()
	operator fun component10() = createId()
	operator fun component11() = createId()
	operator fun component12() = createId()
	operator fun component13() = createId()
	operator fun component14() = createId()
	operator fun component15() = createId()
	operator fun component16() = createId()
}
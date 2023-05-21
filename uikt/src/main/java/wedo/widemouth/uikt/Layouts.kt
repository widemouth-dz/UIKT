package wedo.widemouth.uikt

import wedo.widemouth.uikt.property.MATCH_PARENT
import wedo.widemouth.uikt.property.WRAP_CONTENT

fun marginLayout(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) =
	MarginLP(width, height)

fun linearLayout(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) =
	LinearLP(width, height)

fun boxLayout(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) =
	BoxLP(width, height)

fun constraintLayout(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) =
	ConstraintLP(width, height)

fun relativeLayout(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) =
	RelativeLP(width, height)

fun rootLayout(width: Int = MATCH_PARENT, height: Int = MATCH_PARENT) =
	marginLayout(width, height)
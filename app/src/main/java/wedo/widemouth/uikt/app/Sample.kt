package wedo.widemouth.uikt.app

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.solver.widgets.Flow
import wedo.widemouth.uikt.Box
import wedo.widemouth.uikt.Root
import wedo.widemouth.uikt.Column
import wedo.widemouth.uikt.Constraint
import wedo.widemouth.uikt.ConstraintScope
import wedo.widemouth.uikt.Flow
import wedo.widemouth.uikt.Image
import wedo.widemouth.uikt.Stub
import wedo.widemouth.uikt.decoration.IViewEffect
import wedo.widemouth.uikt.graphics.ColorStateList
import wedo.widemouth.uikt.graphics.GradientDrawable
import wedo.widemouth.uikt.graphics.StateListDrawable
import wedo.widemouth.uikt.graphics.ViewState
import wedo.widemouth.uikt.property.CHAIN_PACKED
import wedo.widemouth.uikt.property.PARENT_ID
import wedo.widemouth.uikt.property.createIds
import wedo.widemouth.uikt.property.dp
import wedo.widemouth.uikt.property.dpF
import wedo.widemouth.uikt.property.matchTo
import wedo.widemouth.uikt.property.size
import wedo.widemouth.uikt.property.src
import wedo.widemouth.uikt.boxLayout
import wedo.widemouth.uikt.merge
import wedo.widemouth.uikt.referencedIds
import wedo.widemouth.uikt.verticalStyle
import wedo.widemouth.uikt.wrapMode

val layout1 =
	Column {
		Row {
			height = 100.dp

			Text {
				text = "Title"
				textSize = 16f
			}
			Button {
				text = "OK"
				textSize = 16f
			}
		}
		Image {
			size = 100.dp
			src = R.drawable.ic_launcher_foreground
		}
	}

class GroupExt(context: Context) : FrameLayout(context)

class ImageExt(context: Context) : AppCompatImageView(context)

val layout2 =
	Root(::GroupExt, ::boxLayout) {
		Widget(::ImageExt) {
			size = 100.dp
			src = R.drawable.ic_launcher_foreground
			scaleType = ImageView.ScaleType.FIT_XY
		}
	}

val layout3 =
	Constraint {

		Image {
			src = R.drawable.ic_launcher_background
			matchTo = PARENT_ID
		}

		val (titleId, subtitleId, ivId) = createIds()

		Text {
			id = titleId
			text = "Title"
			textSize = 16f
		}

		Text {
			id = titleId
			text = "SubTitle"
			textSize = 13f
		}

		Image {
			id = ivId
			src = R.drawable.ic_launcher_foreground
		}

		Flow {
			referencedIds(titleId, subtitleId, ivId)
			wrapMode = Flow.WRAP_CHAIN
			verticalStyle = CHAIN_PACKED
		}
	}

val layout4 =
	Constraint {
		Text {
			text = "Content"
		}
		merge(emptyScope)

		merge(layout5) {

		}
	}

val emptyScope =
	ConstraintScope {
		Text {
			text = "Empty"
		}

		Image {
			size = 50.dp
			src = R.drawable.ic_launcher_foreground
		}
	}

val layout5 =
	Constraint {
		Text {
			text = "Content"
			validate()
		}

		Stub {
			inflate = emptyLayout
		}
	}

val emptyLayout =
	Box {
		Text {
			text = "Empty"
		}

		Image {
			size = 50.dp
			src = R.drawable.ic_launcher_foreground
		}
	}

val view =
	Image {

		radius = 6.dp
		hideRadiusSide = IViewEffect.HIDE_RADIUS_SIDE_TOP

		shadowColor = Color.BLUE
		shadowAlpha = 0.3f

		borderColor = Color.GRAY
		borderWidth = 2.dp

		miniWidth = 50.dp
		maxiWidth = 100.dp
		miniHeight = 50.dp
		maxiHeight = 120.dp

		bottomDividerHeight = 1.dp
		bottomDividerColor = Color.GRAY

	}

val gradientDrawable =
	GradientDrawable(
		color = Color.BLUE,
		alpha = 0.5f,
		radiusAdjustBounds = true,
		cornerRadius = 6.dpF,
		leftTopRadius = 6.dpF,
		rightTopRadius = 0f,
		leftBottomRadius = 6.dpF,
		rightBottomRadius = 0f,
		strokeColor = Color.GRAY,
		strokeWidth = 1.dp,
		width = 5.dp,
		height = 10.dp
	)

val colors =
	ColorStateList(
		ViewState.FOCUSED + !ViewState.SELECTED to Color.BLUE,
		!ViewState.FOCUSED + ViewState.SELECTED to Color.GREEN,
		ViewState.CHECKED to Color.RED,
		ViewState.PRESSED to Color.WHITE,
		ViewState.ENABLED to Color.GRAY,
		ViewState.NORMAL to Color.BLACK,
	)

val drawables =
	StateListDrawable(
		ViewState.FOCUSED + !ViewState.SELECTED to ColorDrawable(Color.BLUE),
		!ViewState.FOCUSED + ViewState.SELECTED to ColorDrawable(Color.GREEN),
		ViewState.CHECKED to ColorDrawable(Color.RED),
		ViewState.PRESSED to ColorDrawable(Color.WHITE),
		ViewState.ENABLED to ColorDrawable(Color.GRAY),
		ViewState.NORMAL to ColorDrawable(Color.BLACK),
	)


//fun <SL : LP> Scope<SL>.LazyGridColumn(block: WidgetReceiver<VerticalGridView, SL>) =
//	Widget(::VerticalGridView, block)
//
//fun <SL : LP> Scope<SL>.LazyGridRow(block: WidgetReceiver<HorizontalGridView, SL>) =
//	Widget(::HorizontalGridView, block)
//
//val layout6 =
//	Column {
//		LazyGridRow {
//			width = MATCH_PARENT
//			height = 50.dp
//		}
//
//		LazyGridColumn {
//			width = MATCH_PARENT
//			weight = 1f
//		}
//	}

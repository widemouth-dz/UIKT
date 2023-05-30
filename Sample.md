# UIKT Sample

## Common components
```
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
```

## Custom components
```
class GroupExt(context: Context) : FrameLayout(context)

class ImageExt(context: Context) : AppCompatImageView(context)

Root(::GroupExt, ::boxLayout) {

    Widget(::ImageExt) {
        size = 100.dp
        src = R.drawable.ic_launcher_foreground
        scaleType = ImageView.ScaleType.FIT_XY
    }
}
```

## Constraint layout
```
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
```

## Include
```
Constraint {
    Text { 
    	text = "Content"
    }
    
    // for XML layout.
    include(R.layout.sample)
    
    // or with a block.
    include(R.layout.sample) {
        size = 100.dp
    }
    
    // for UIKT layout.
    include(emptyLayout)
    
    // or with a block.
    include(emptyLayout) {
        size = MATCH_PARENT
    }
}

val emptyLayout =
    Constraint {
        Text {
            text = "Empty"
        }

        Image {
            size = 50.dp
            src = R.drawable.ic_launcher_foreground
        }
    }
```

## Merge
```
Constraint {
    Text { 
    	text = "Content"
    }
    
    // for XML merge
    include(R.layout.merge)
    
    // for UIKT merge
    include(emptyWidgets)
}

val emptyWidgets =
    ConstraintMerge {
        Text {
            text = "Empty"
        }

        Image {
            size = 50.dp
            src = R.drawable.ic_launcher_foreground
        }
    }
```

## Stub
```
Constraint {
    Text {
    	text = "Content"
    }
    
    val (emptyId) = createIds()
    
    Stub { 
        inflatedId = emptyId
    	inflate = emptyLayout
    	// Call Stub.inflate() to inflate emptyLayout.
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
```
## Extension
Like that for `Leanback` components.
```
fun <SL : LP> Scope<SL>.LazyGridColumn(block: WidgetReceiver<VerticalGridView, SL>) =
	Widget(::VerticalGridView, block)

fun <SL : LP> Scope<SL>.LazyGridRow(block: WidgetReceiver<HorizontalGridView, SL>) =
	Widget(::HorizontalGridView, block)

Column {
    LazyGridRow {
        width = MATCH_PARENT
        height = 50.dp
    }

    LazyGridColumn {
        width = MATCH_PARENT
        weight = 1f
    }
}
```

## View properties
In code:
```
Image { // or Box, Constraint, Column, Row ...

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
    ...
}
```
In XML:
```
<...Ext
    android:id="@+id/ext"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    
    android:maxWidth="100dp"
    android:minWidth="80dp"
    android:maxHeight="50dp"
    android:minHeight="30dp"
    
    app:shadowColor="@color/blue"
    app:shadowAlpha="0.1f"
    app:shadowElevation="10dp"
    app:borderWidth="1dp"
    app:borderColor="@color/black"
    app:radius="6dp"
    app:isRadiusAdjustBounds="true"
    ...	/>
```
## GradientDrawable

```
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
```

## StateListDrawable

```
StateListDrawable(
    ViewState.FOCUSED + !ViewState.SELECTED to ColorDrawable(Color.BLUE),
    !ViewState.FOCUSED + ViewState.SELECTED to ColorDrawable(Color.GREEN),
    ViewState.CHECKED to ColorDrawable(Color.RED),
    ViewState.PRESSED to ColorDrawable(Color.WHITE),
    ViewState.ENABLED to ColorDrawable(Color.GRAY),
    ViewState.NORMAL to ColorDrawable(Color.BLACK),
)
```

## ColorStateList

```
ColorStateList(
    ViewState.FOCUSED + !ViewState.SELECTED to Color.BLUE,
    !ViewState.FOCUSED + ViewState.SELECTED to Color.GREEN,
    ViewState.CHECKED to Color.RED,
    ViewState.PRESSED to Color.WHITE,
    ViewState.ENABLED to Color.GRAY,
    ViewState.NORMAL to Color.BLACK,
)
```

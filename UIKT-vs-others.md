# UIKT vs others

## [UIKT](./README.md) vs [Compose](https://developer.android.com/jetpack/compose)

> Jetpack Compose is Android’s recommended modern toolkit for building native UI. 
> It simplifies and accelerates UI development on Android. Quickly bring your app to life
> with less code, powerful tools, and intuitive Kotlin APIs.

We also think that Jetpack Compose is really great and is definitely the future of UI development.
Unfortunately, its requirements on the version are so restrictive, notably the `Android SDK` requires `Android 5.0`, 
that some projects may never be able to use it. 

Many of the concepts and ideas in `UIKT` are borrowed and imitated from `Compose`, such as `Scope`, `DslMarker`, `createIds`, 
and even the names, `Box`, `Column`, `Row`. To some extent, I just want to create a `Android UI DSL` 
with a writing style like Compose, `UIKT` is just that.

## [UIKT](./README.md) vs XML

XMl is the way to creating Android all UIs, including layouts, drawables, colors...

In my opinion, the best benefit is that it is highly readable, with only UI tags and their properties.
But it is not easy when writing, the UI is not highly correlated with its properties in XML, means that 
you don't know whether a property is valid for the UI unless you are really familiar with it, such as `maxWidth`.

Besides, there are a lot of discussion about XML performance, I don't think it is a big deal.
Although UIKT may be perform better than XML(or worse), it is secondary.

## [UIKT](./README.md) vs [X2C](https://github.com/iReaderAndroid/X2C)

> In order to preserve the advantages of xml and solve the performance problems it brings, we developed the X2C solution. 
> That is, during the compilation and generation of the APK, the translation of the layout that needs to be translated generates the corresponding java file, 
> so that the developer writes the layout or writes the original xml, but for the program, the runtime loads the corresponding java file. 
> We use APT (Annotation Processor Tool) + JavaPoet technology to complete the operation of the whole process during the compilation [Annotation] -> [Resolve] -> [Translate xml] -> [Generate java].

As said above, X2C is a tool that translate XML to generates the corresponding java file 
to solve the performance problem from reading, but it is no need to do it for the most XML.
For single load, the optimization it brings is very limited, or rather the performance is not a big deal in that case.

So it is usually only used in the case of scrolling with a lot of items.

Compare to X2C, UIKT is like directly writing the corresponding code file of XML, but a Kotlin file,
so the performance difference between UIKT and X2C is equivalent to that between Java and Kotlin.
However, UIKT will be slightly inferior to X2C, because some function types with `context` are not `inline`.

## [UIKT](./README.md) vs [Anko-Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts)

> `Anko-Layouts` is a Android UI DSL makes the same logic easy to read, easy to write and there is no runtime overhead. Here it is again:
```kotlin
linearLayout {
    button("Login") {
        textSize = 26f
    }.lparams(width = wrapContent) {
        horizontalMargin = dip(5)
        topMargin = dip(10)
    }
}
```
`Anko-Layouts` resemble `UIKT` highly about features, APIs, functions. Honestly, they are one thing.

Slightly different, a view need to be build with two blocks via `Anko-Layouts`, which, of course, is not a big deal.
UIKT merges them into one block, at the cost of, which is backed by `Kotlin 1.7.20` (1.6.20 && 1.7.20).
    
## [UIKT](./README.md) vs [Splitties Views DSL](https://github.com/LouisCAD/Splitties/tree/main/modules/views-dsl)

> `Splitties Views DSL` has been designed to be simple.

As that said, it is simple enough to create a single view via `Splitties`,
and better than `Anko-Layouts` in generic extensions.

In building layout trees, `Splitties Views DSL` deliberately avoids adding views implicitly, so you need to
explicitly call `add` function with the view to be added and its layout params block.

There are only UI tags and their properties in UIKT layout tress, no anything else, and views are added implicitly 
based on their location.


## Other links
- [Comparison_with_anko](https://github.com/LouisCAD/Splitties/blob/0e4fcbf67c8aea591068366f7736499fbd77f565/Comparison_with_anko.md):How Splitties differs from Anko.
- [Kotlin-UIs-vs-xml-layouts](https://github.com/LouisCAD/Splitties/blob/main/modules/views-dsl/Kotlin-UIs-vs-xml-layouts.md): Why a Kotlin Views DSL over xml layouts?
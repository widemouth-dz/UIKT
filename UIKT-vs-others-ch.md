# UIKT vs others

## [UIKT](./README-ch.md) vs [Compose](https://developer.android.com/jetpack/compose)

> Jetpack Compose is Android’s recommended modern toolkit for building native UI. 
> It simplifies and accelerates UI development on Android. Quickly bring your app to life
> with less code, powerful tools, and intuitive Kotlin APIs.

我同样认为 Jetpack Compose 非常棒，绝对是UI开发的未来。遗憾的是，它对版本的要求非常严格，特别是 `Android SDK` 需要 `Android 5.0`，
以至于某些项目可能永远无法使用它。

`UIKT`中的许多概念和想法都是从`Compose`中借鉴和效仿的，例如`Scope`，`DslMarker`，`createIds`，甚至名称`Box`，`Column`，`Row`。

在某种程度上，我只是想创建一个像Compose这样的写作风格的`Android UI DSL`，这就是`UIKT`。

## [UIKT](./README-ch.md) vs XML

XMl是创建Android所有UI的方式，包括layout，drawable，color...

在我看来，最大的好处是它具有高度可读性，只有 UI 标签及其属性。但是在编写时并不容易，UI 与其在 XML 中的属性没有高度关联，
这意味着您不知道某个属性是否对 UI 有效，除非您非常熟悉它，例如`maxWidth`。

此外，有很多关于XML性能的讨论，我认为这没什么大不了的。UIKT 的性能可能比 XML 更好（或更差），这是次要的。

## [UIKT](./README-ch.md) vs [X2C](https://github.com/iReaderAndroid/X2C)

> In order to preserve the advantages of xml and solve the performance problems it brings, we developed the X2C solution. 
> That is, during the compilation and generation of the APK, the translation of the layout that needs to be translated generates the corresponding java file, 
> so that the developer writes the layout or writes the original xml, but for the program, the runtime loads the corresponding java file. 
> We use APT (Annotation Processor Tool) + JavaPoet technology to complete the operation of the whole process during the compilation [Annotation] -> [Resolve] -> [Translate xml] -> [Generate java].

如上所述，X2C 是一个将 XML 转换为生成相应 java 文件以解决读取性能问题的工具，但对于大多数 XML 来说，没有必要这样做。

对于单次加载，它带来的优化非常有限，或者更确切地说，在这种情况下性能并不是什么大问题。因此，它通常仅在滚动中含有大量子项的情况下使用。

与 X2C 相比，UIKT 就像是直接编写 XML 对应的代码文件，但是一个 Kotlin 文件，因此 UIKT 和 X2C 的性能差异相当于 Java 和 Kotlin 的性能差异。
但是，UIKT 会略逊于 X2C，因为某些带有`context`的函数类型不是`inline`的。


## [UIKT](./README-ch.md) vs [Anko-Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts)

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

`Anko-Layouts`在特性，API和功能方面与`UIKT`非常相似。老实说，它们是一回事。

稍微不同，通过`Anko-Layouts`构建 view 需要用到两个块，这当然不是什么大问题。UIKT 将它们合并为一个块，
作为代价，它由`Kotlin 1.7.20`（1.6.20 && 1.7.20）支持。

## [UIKT](./README-ch.md) vs [Splitties Views DSL](https://github.com/LouisCAD/Splitties/tree/main/modules/views-dsl)

> `Splitties Views DSL` has been designed to be simple.

如上所述，通过 `Splitties` 创建单个视图非常简单，并且在通用扩展上比 `Anko-Layouts` 做得更好。

在构建`layout tree`时，`Splitties Views DSL` 有意避免隐式添加view，因此您需要使用要添加的view及其`layout params` block显式调用 add 函数。

UIKT 布局树中只有 UI 标签和它们的属性，没有其他任何东西，视图是根据它们的位置隐式添加的。


## Other links
- [Comparison_with_anko](https://github.com/LouisCAD/Splitties/blob/0e4fcbf67c8aea591068366f7736499fbd77f565/Comparison_with_anko.md):How Splitties differs from Anko.
- [Kotlin-UIs-vs-xml-layouts](https://github.com/LouisCAD/Splitties/blob/main/modules/views-dsl/Kotlin-UIs-vs-xml-layouts.md): Why a Kotlin Views DSL over xml layouts?
# UIKt Arch Doc

## [Context receivers](https://github.com/Kotlin/KEEP/blob/context-receivers/proposals/context-receivers.md)

UIKT使用`context receivers`来构建布局DSL，这是一个**实验性**API。
视图布局块中至少有两个 receiver，其布局和自身。但是`extension function`只支持一个 receiver，我尝试了很多可能的方法来解决它。
以下是尝试列表：

* 多个 `receiver`, 多个 `extension function`, 就像这样:

  ```
  lambda:  view.() -> Unit, layout.() -> Unit
  
  dsl:     view{ }, layout{ }
  ```

  [Anko-Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts) 遵循它，一个 view 带有两个 block，如下所示：
  
  ```
  view{
    
  }.lparams{
  
  }
  ```

* 一个 `extension function` 带有一个 `receiver` 和多个参数, 就像这样:

  ```
  lambda: view.(layout) -> Unit
  
  dsl:    view{ layout-> }
  ```

  如果这样，必须使用块中的参数前缀调用其属性，就像`XML`那样。
  
  ```
      view{ layout->
         layout.*** =
         layout.*** =
      }
  ```
它们都能够满足构建布局树的要求，但有一点瑕疵。如果它适用于多个`receiver`，这将是最佳实践。

起初，我去找了一些关于 lambda 的想法，这是一个尝试：

```
lambda: view.() -> layout.() -> Unit

dsl:    view{
              {
                ..block..
              }
          }
```

现在，内部`block`有两个隐式 receiver。之后，我发现了一个关于多个 receiver 的实验性弱关键字`context`。

在我看来，与嵌套 `lambda` 没有区别，由于其实验性质，有些东西与其他一些功能不兼容，它们是： 
- lambda 块必须是 `noinline`。
- 当有两个以上的 receiver 时，不能与`typealias`一起使用。

一些详细信息链接：

- [KEEP-259](https://github.com/Kotlin/KEEP/blob/context-receivers/proposals/context-receivers.md#detailed-design)
- [KT-51234](https://youtrack.jetbrains.com/issue/KT-51234/Context-receivers-can-be-duplicated-in-function-declaration)
- [KT-54233](https://youtrack.jetbrains.com/issue/KT-54233/Lambda-context-receiver-definitions-can-no-longer-accept-multiple-reified-generic-context-parameters)

然后，必须将具有三个 receiver 的函数类型定义为冗余的 `lambda`，并且所有这些都没有内联功能。无论如何，我们已经有多个 receiver 。


## [DslMarker]
另一个重要的注意事项是 [DslMarker]，我们不仅要考虑来自外部块的隐式 receiver ，还要考虑当前块中的多个 receiver ，
一个带有上下文 receiver 的块要求可以访问其内部隐式 receiver ，但不能访问外部，因此内部隐式 receiver 绑定不同的 [markers][DslMarker]。
see [ViewMarker], [LayoutMarker], [ScopeMarker].

## Partial applied function

构造布局至少需要两个参数（请参阅下面的代码 1）、[context][Context] 和树`block`，至少对于布局根而言。

```
Root(context, block)         // 1

PartialRoot(block)(context)  // 2
```

我想用上面的`code2`构建它，[Root] 就是这样工作的。

`PartialRoot` function:
```
fun PartialRoot(block): (Context) -> View = { ctx ->
    Root(ctx, block)
}
```
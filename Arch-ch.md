# UIKt Arch Doc

## [Context receivers](https://github.com/Kotlin/KEEP/blob/context-receivers/proposals/context-receivers.md)

UIKT使用`context receivers`来构建布局DSL，这是一个**实验性**API。
视图布局块中至少有两个接收器，其布局和自身。但是`extension function`只支持一个接收器，我尝试了很多可能的方法来解决它。
以下是尝试列表：

* 多个 `receivers`, 多个 `extension functions`, 就像这样:

  ```
  lambda:  view.() -> Unit, layout.() -> Unit
  
  dsl:     view{ }, layout{ }
  ```

  [Anko-Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts) 遵循它，一个view带有两个block，如下所示：
  
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
它们都能够满足构建布局树的要求，但有一点瑕疵。如果它适用于多个`receivers`，这将是最佳实践。

At first, I went looking for some ideas on lambda, here is an attempt:

```
lambda: view.() -> layout.() -> Unit

dsl:    view{
              {
                ..block..
              }
          }
```

Now, the inner `block` has got two implicit receivers.
After that, I found a experimental weak keyword `context` about multiple receivers.

In my opinion, no difference from the nested lambda, due to its experimental nature,
there are some things incompatible with some other features, here they are:

- The lambda block must be `noinline`.
- Cannot be used with `typealias` when there are more than two receivers.

Some links for details:

- [KEEP-259](https://github.com/Kotlin/KEEP/blob/context-receivers/proposals/context-receivers.md#detailed-design)
- [KT-51234](https://youtrack.jetbrains.com/issue/KT-51234/Context-receivers-can-be-duplicated-in-function-declaration)
- [KT-54233](https://youtrack.jetbrains.com/issue/KT-54233/Lambda-context-receiver-definitions-can-no-longer-accept-multiple-reified-generic-context-parameters)

Then, a function with three receivers have to be defined as a verbose lambda, and all these are left
without inline feature.
Anyway, we already have got multiple receivers.

## [DslMarker]

Another important note is the [DslMarker], we have to consider the implicit receivers not only from
the outer blocks,
but also the multiple receivers in current block, a block with context receivers be required that
its internal implicit receivers can access but not the external, so the internal implicit are bound
with
different [markers][DslMarker]. see [ViewMarker], [LayoutMarker], [ScopeMarker].

## Partial applied function

Constructing a layout requires at least two parameters (see code#1 below), [context][Context] and
tree block, at least for the layout root.

```
Root(context, block)         // 1

PartialRoot(block)(context)  // 2
```

I want to construct it with `code#2` above, and [Root] works just like it.

`PartialRoot` function:
```
fun PartialRoot(block): (Context) -> View = { ctx ->
    Root(ctx, block)
}
```
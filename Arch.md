# UIKt Arch Doc

## [Context receivers](https://github.com/Kotlin/KEEP/blob/context-receivers/proposals/context-receivers.md)

UIKT uses `context receivers` to build layout DSL, which is an **experimental** API.

There are at least two receivers in view layout block, its layout and itself.
But the `extension function` only supports one receiver, I tried many possible ways to solve it.
Here is the tried list:

* Multiple receivers, multiple extension functions, like that:

  ```
  lambda:  view.() -> Unit, layout.() -> Unit
  
  dsl:     view{ }, layout{ }
  ```

  [Anko-Layouts](https://github.com/Kotlin/anko/wiki/Anko-Layouts) follows it, a widget works with two blocks, like that:
  `
  ```
  view{
    
  }.lparams{
  
  }
  ```

* A extension function with one receiver and multiple params, like that:

  ```
  lambda: view.(layout) -> Unit
  
  dsl:    view{ layout-> }
  ```
  
  when following it, the property have to be called with the param prefix in the block, like `XML`.
  
  ```
      view{ layout->
         layout.*** =
         layout.*** =
      }
  ```

They are all able to meet the requirements of layout tree building with a little blemish.
What if it works with multiple receivers, it will be the best practice.

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
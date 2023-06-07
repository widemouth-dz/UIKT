# UIKT

## About

UIKT 是关于 Android UI 的`DSL`，它可以使用简单易读的 Kotlin 代码创建 UI。

这里有一个简短的介绍，[UIKT-vs-others](UIKT-vs-others-ch.md)，如果你对`Android UI DSL`还不太了解，那么根据兼容性和你的喜好选择其中一个。
关于UIKT的实现基础介绍，参见 [Arch-ch](Arch-ch) 。
## Requirement

### Kotlin

|      Feature      | Kotlin  |
|:-----------------:|:-------:|
|     `context`     | v1.6.20 |
| `context` generic | v1.7.20 |

### Plugins

以下是 Kotlin 升级建议， [Gradle | Kotlin Doc](https://kotlinlang.org/docs/gradle-configure-project.html).

最低版本

| Kotlin  | KGP     | Gradle |  AGP   |
|:-------:|---------|:------:|:------:|
| v1.7.20 | v1.7.20 | v6.7.1 | v4.0.1 |

## Configuration

添加依赖
```kotlin
implementation("com.github.widemouth-dz:uikt:main-SNAPSHOT")
ksp("com.github.widemouth-dz.uikt:compiler:main-SNAPSHOT")
```
配置`kotlinOptions`以启用`context`API。

```groovy
kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + ["-Xcontext-receivers"]
}
```
配置以下ksp参数以关闭或启用DSL组件原函数和偏应用函数生成。
```groovy
ksp {
    arg("primitive", "false")
    arg("partial", "true")
}
```

## Sample
请参阅 [Sample](./Sample.md) 了解详情.
```kotlin
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
## Others about `UIKT`

- [Arch Doc](./Arch-ch.md): 实现细节。
- [Sample Doc](./Sample.md): 详细用例。
- [UIKT-vs-others](./UIKT-vs-others-ch.md): UIKT 与其他相关项目的比较。
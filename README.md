# UIKT

## About

UIKT is a `DSL` for Android UI, which can create UIs with simple and readable Kotlin code.

Here's a brief introduction, [UIKT-vs-others](UIKT-vs-others.md) , to the related projects if you don't know much about `Android UI DSL` yet, 
then choose one of them based on compatibility and your preferences.

## Requirement

### Kotlin

|      Feature      | Kotlin  |
|:-----------------:|:-------:|
|     `context`     | v1.6.20 |
| `context` generic | v1.7.20 |

### Plugins

Here are Kotlin upgrade recommendations, [Gradle | Kotlin Doc](https://kotlinlang.org/docs/gradle-configure-project.html).

At least

| Kotlin  | KGP     | Gradle |  AGP   |
|:-------:|---------|:------:|:------:|
| v1.7.20 | v1.7.20 | v6.7.1 | v4.0.1 |

## Configuration

Configure the following `kotlinOptions` to enable `context` API.

```
kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + ["-Xcontext-receivers"]
}
```

## Sample
See [Sample](./Sample.md) for details.
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
package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.BINARY)
annotation class DslWidget(val widgetClasses: Array<KClass<*>>)
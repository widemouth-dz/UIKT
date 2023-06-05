package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
annotation class DslWidget(val values: Array<KClass<*>>)

@Retention(AnnotationRetention.SOURCE)
annotation class DslWidgetDeferred(val values: Array<KClass<*>>)
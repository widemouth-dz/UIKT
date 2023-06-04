package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
annotation class DslWidget(val widgetClasses: Array<KClass<*>>)
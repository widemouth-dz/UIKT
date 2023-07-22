package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class DslWidget(val widgetClass: KClass<*>)
package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.BINARY)
annotation class ViewEffectGroup(val widgetClasses: Array<KClass<*>>)

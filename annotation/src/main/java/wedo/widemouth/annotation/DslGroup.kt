package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.BINARY)
annotation class DslGroup(val groupClasses: Array<KClass<*>>)

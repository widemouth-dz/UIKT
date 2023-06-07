package wedo.widemouth.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
annotation class DslGroup(val groupClasses: Array<KClass<*>>)

@Retention(AnnotationRetention.SOURCE)
annotation class DslGroupDeferred(val values: Array<KClass<*>>)

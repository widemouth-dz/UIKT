package wedo.widemouth.annotation

import kotlin.reflect.KClass

annotation class ViewEffectGroup(val widgetClasses: Array<KClass<*>>)

package wedo.widemouth.uikt

@PublishedApi
internal fun <P, V> remember(
	keySelector: (P) -> Any? = { it },
	cache: MutableMap<Any?, V> = mutableMapOf(),
	function: (P) -> V,
): (P) -> V {
	return { cache.getOrPut(keySelector(it)) { function(it) } }
}

@PublishedApi
internal fun <P, V> rememberSuspend(
	keySelector: (P) -> Any? = { it },
	cache: MutableMap<Any?, V> = mutableMapOf(),
	function: suspend (P) -> V,
): suspend (P) -> V {
	return { cache.getOrPut(keySelector(it)) { function(it) } }
}


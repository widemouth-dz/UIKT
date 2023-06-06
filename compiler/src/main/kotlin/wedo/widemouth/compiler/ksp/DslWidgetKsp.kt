package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslWidget
import wedo.widemouth.annotation.DslWidgetDeferred
import wedo.widemouth.compiler.findClassesInAnnotation
import wedo.widemouth.compiler.generator.DslWidgetGenerator
import wedo.widemouth.compiler.packageName
import java.io.IOException

class DslWidgetKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
		DslWidgetKsp(environment.codeGenerator, environment.logger, environment.options)
}

class DslWidgetKsp(
	private val codeGenerator: CodeGenerator,
	private val logger: KSPLogger,
	private val options: Map<String, String>,
) : SymbolProcessor {

	override fun process(resolver: Resolver): List<KSAnnotated> {
		logger.warn("DslWidgetKsp process start")
		val symbols = resolver.getSymbolsWithAnnotation(DslWidget::class.java.name)
		var widgets = symbols.findClassesInAnnotation(DslWidget::class)

		val deferredSymbols = resolver.getSymbolsWithAnnotation(DslWidgetDeferred::class.java.name)

		val deferredWidget = deferredSymbols.map {
			it.findClassesInAnnotation(DslWidgetDeferred::class)
		}

		if (deferredWidget.any { it.none() })
			return (symbols + deferredSymbols).distinct().toList()

		val allSymbols = symbols + deferredSymbols

		if (allSymbols.none()) return emptyList()

		val generatedCodePackageName = allSymbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [DslGroup] or [DslGroupDeferred.")

		widgets += deferredWidget.flatten()

		if (widgets.none()) return emptyList()

		DslWidgetGenerator(generatedCodePackageName).generate(widgets.map { it.toClassName() }) {
			try {
				it.writeTo(codeGenerator, true)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		logger.warn("DslWidgetKsp process end")
		return emptyList()
	}


}


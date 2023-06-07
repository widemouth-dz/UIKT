package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
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
		DslWidgetKsp(environment)
}

class DslWidgetKsp(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

	private val mTasks = mapOf(
		"ScopeWidget" to DslWidgetGenerator::generateScopeWidget,
		"Widget" to DslWidgetGenerator::generateWidget,
		"WidgetWithDefaultLP" to DslWidgetGenerator::generateWidgetWithDefaultLP,
		"PartialAppliedWidget" to DslWidgetGenerator::generatePartialWidget,
		"PartialAppliedWidgetWithDefaultLP" to DslWidgetGenerator::generatePartialWidgetWithDefaultLPFun
	)

	override fun process(resolver: Resolver): List<KSAnnotated> {
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

		widgets = widgets.distinct()

		mTasks.forEach { task ->
			val fileBuilder = FileSpec.builder(generatedCodePackageName, task.key)
			widgets.map { it.toClassName() }.forEach {
				fileBuilder.addFunction(task.value(it))
			}
			try {
				fileBuilder.build().writeTo(environment.codeGenerator, true)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

		return emptyList()
	}


}


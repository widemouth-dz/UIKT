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
import wedo.widemouth.compiler.annotationsIsType
import wedo.widemouth.compiler.findClassesInArguments
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

		if (symbols.none()) return emptyList()

		var deferred = false
		val widgets = symbols
			.flatMap { symbol ->
				symbol.annotationsIsType<DslWidget>().flatMap { annotation ->
					annotation.findClassesInArguments().also { if (it.none()) deferred = true }
				}
			}
			.distinct()

		// terminate widgets.
		widgets.count()

        if (deferred) return symbols.toList()

        if (widgets.none()) return emptyList()

		val generatedCodePackageName = symbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [DslGroup] or [DslGroupDeferred.")

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


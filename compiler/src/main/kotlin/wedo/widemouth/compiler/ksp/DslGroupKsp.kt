package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.annotation.DslGroupDeferred
import wedo.widemouth.compiler.allNestedClasses
import wedo.widemouth.compiler.findClassesInAnnotation
import wedo.widemouth.compiler.generator.DslGroupGenerator
import wedo.widemouth.compiler.packageName
import java.io.IOException

class DslGroupKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
		DslGroupKsp(environment.codeGenerator, environment.logger, environment.options)
}

class DslGroupKsp(
	private val codeGenerator: CodeGenerator,
	private val logger: KSPLogger,
	private val options: Map<String, String>,
) : SymbolProcessor {

	override fun process(resolver: Resolver): List<KSAnnotated> {
		logger.warn("DslGroupKsp process start")

		val symbols = resolver.getSymbolsWithAnnotation(DslGroup::class.java.name)
		var groups = symbols.findClassesInAnnotation(DslGroup::class)

		val deferredSymbols = resolver.getSymbolsWithAnnotation(DslGroupDeferred::class.java.name)

		val deferredGroup = deferredSymbols.map { symbol ->
			symbol.findClassesInAnnotation(DslGroupDeferred::class)
		}

		if (deferredGroup.any { it.none() }) return (symbols + deferredSymbols).distinct().toList()

		val allSymbols = symbols + deferredSymbols

		if (allSymbols.none()) return emptyList()

		val generatedCodePackageName = allSymbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [DslGroup] or [DslGroupDeferred.")

		groups += deferredGroup.flatten()

		if (groups.none()) return emptyList()

		val layoutBaseClass =
			resolver.getClassDeclarationByName("android.view.ViewGroup.LayoutParams")
				?.asStarProjectedType()
				?: error("Can not find class [android.view.ViewGroup.LayoutParams] in your compiled module")

		val groupSequence = groups.map { group ->
			Pair(group.toClassName(),
				group.allNestedClasses.first { layoutBaseClass.isAssignableFrom(it.asStarProjectedType()) }
					.toClassName())
		}

		DslGroupGenerator(generatedCodePackageName).generate(groupSequence) {
			try {
				it.writeTo(codeGenerator, true)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		logger.warn("DslGroupKsp process end")
		return emptyList()
	}
}


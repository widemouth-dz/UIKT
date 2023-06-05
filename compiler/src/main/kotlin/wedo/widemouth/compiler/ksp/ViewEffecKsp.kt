package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.ViewEffectGroup
import wedo.widemouth.compiler.findClassesInAnnotation
import wedo.widemouth.compiler.generator.ViewEffectGenerator
import java.io.IOException

class ViewEffectKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
		return ViewEffectKsp(environment.codeGenerator, environment.logger, environment.options)
	}
}

class ViewEffectKsp(
	private val codeGenerator: CodeGenerator,
	private val logger: KSPLogger,
	private val options: Map<String, String>,
) : SymbolProcessor {

	override fun process(resolver: Resolver): List<KSAnnotated> {
		logger.warn("ViewEffectKsp process start")
		val symbols = resolver.getSymbolsWithAnnotation(ViewEffectGroup::class.java.name)

		val viewEffectWidgets: Sequence<KSClassDeclaration> =
			symbols.findClassesInAnnotation(ViewEffectGroup::class)

		if (viewEffectWidgets.none()) return emptyList()

		ViewEffectGenerator.generate(viewEffectWidgets.map { it.toClassName() }) {
			try {
				it.writeTo(codeGenerator, false)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

		logger.warn("ViewEffectKsp process end")
		return emptyList()
	}
}


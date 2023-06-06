package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.ViewEffectWidget
import wedo.widemouth.compiler.findClassesInAnnotation
import wedo.widemouth.compiler.generator.ViewEffectGenerator
import wedo.widemouth.compiler.packageName
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
		val symbols = resolver.getSymbolsWithAnnotation(ViewEffectWidget::class.java.name)

		if (symbols.none()) return emptyList()

		val generatedCodePackageName = symbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [ViewEffectWidget].")

		val viewEffectWidgets: Sequence<KSClassDeclaration> =
			symbols.findClassesInAnnotation(ViewEffectWidget::class)

		if (viewEffectWidgets.none()) return emptyList()

		viewEffectWidgets.map { it.toClassName() }.forEach {
			try {
				val type = ViewEffectGenerator.generate(it)
				FileSpec.builder(generatedCodePackageName, type.name!!).addType(type).build()
					.writeTo(codeGenerator, false)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

		logger.warn("ViewEffectKsp process end")
		return emptyList()
	}
}


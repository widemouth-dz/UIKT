package wedo.widemouth.compiler.ksp

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
import wedo.widemouth.compiler.findClassesInAnnotationIsType
import wedo.widemouth.compiler.generator.ViewEffectGenerator
import wedo.widemouth.compiler.packageName
import java.io.IOException

class ViewEffectKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
		return ViewEffectKsp(environment)
	}
}

class ViewEffectKsp(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

	override fun process(resolver: Resolver): List<KSAnnotated> {
		val symbols = resolver.getSymbolsWithAnnotation(ViewEffectWidget::class.java.name)

		if (symbols.none()) return emptyList()

		val generatedCodePackageName = symbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [ViewEffectWidget].")

		val viewEffectWidgets: Sequence<KSClassDeclaration> =
			symbols.flatMap { it.findClassesInAnnotationIsType<ViewEffectWidget>() }

		if (viewEffectWidgets.none()) return emptyList()

		viewEffectWidgets.map { it.toClassName() }.forEach {
			val type = ViewEffectGenerator.generateViewEffectExt(it)
			val file = FileSpec.builder(generatedCodePackageName, type.name!!).addType(type).build()
			try {
				file.writeTo(environment.codeGenerator, false)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		return emptyList()
	}
}


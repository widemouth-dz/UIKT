package wedo.widemouth.compiler.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslWidget
import java.io.IOException

//@AutoService(SymbolProcessorProvider::class)
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
        logger.warn("$this DslWidgetKsp process start")
        val symbols = resolver.getSymbolsWithAnnotation(DslWidget::class.java.canonicalName)

        val dslWidgets = mutableSetOf<KSType>()

        symbols.forEach { symbol ->
            symbol.annotations.filter { it.shortName.asString() == DslWidget::class.simpleName }
                .forEach { annotation ->
                    annotation.arguments.forEach { argument ->
                        @Suppress("UNCHECKED_CAST")
                        when (argument.name?.asString()) {
                            "widgetClasses" -> dslWidgets.addAll(argument.value as ArrayList<KSType>)
                        }
                    }
                }
        }

        logger.warn("DslWidgetKsp dslWidgets size = ${dslWidgets.size}")

        if (dslWidgets.any { it.isError }) return symbols.toList()

        WidgetPoet.process(dslWidgets.asSequence().map { it.toClassName() }) {
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


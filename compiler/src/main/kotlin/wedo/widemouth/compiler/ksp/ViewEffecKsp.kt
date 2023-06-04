package wedo.widemouth.compiler.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
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
import wedo.widemouth.annotation.ViewEffectGroup
import java.io.IOException

//@AutoService(SymbolProcessorProvider::class)
class ViewEffectKspProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.warn("ViewEffectKspProvider create $this")

        return ViewEffectKsp(environment.codeGenerator, environment.logger, environment.options)
    }
}

class ViewEffectKsp(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {

    var invoked = false

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("$this ViewEffectKsp process start")

//        logger.warn("ViewEffectKsp process generatedFile.size = ${codeGenerator.generatedFile.size}")

        val symbols = resolver.getSymbolsWithAnnotation(ViewEffectGroup::class.qualifiedName!!)

        logger.warn("ViewEffectKsp process symbols size = ${symbols.toList().size}")

        val viewEffectWidgets = mutableSetOf<KSType>()


//        val deferred = symbols.filter { !it.validate() }

//        symbols.forEach {
//            it.getAnnotationsByType(ViewEffectGroup::class).first().widgetClasses
//        }
        symbols.forEach { symbol ->
            symbol.annotations.filter { it.shortName.asString() == ViewEffectGroup::class.simpleName }
                .forEach { annotation ->
                    annotation.arguments.forEach { argument ->
                        @Suppress("UNCHECKED_CAST")
                        when (argument.name?.asString()) {
                            "widgetClasses" -> viewEffectWidgets.addAll(argument.value as ArrayList<KSType>)
                        }
                    }
                }
        }

        logger.warn("ViewEffectKsp process viewEffectWidgets size = ${viewEffectWidgets.size}")

        ViewEffectPoet.process(viewEffectWidgets.asSequence().map { it.toClassName() }) {
            try {
                it.writeTo(codeGenerator, false)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        logger.warn("ViewEffectKsp process end")
        return emptyList()
    }

    override fun finish() {
        super.finish()
    }

    override fun onError() {
        super.onError()
    }
}


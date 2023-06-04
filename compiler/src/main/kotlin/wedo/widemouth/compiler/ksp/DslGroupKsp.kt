package wedo.widemouth.compiler.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslGroup
import java.io.IOException

//@AutoService(SymbolProcessorProvider::class)
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
        logger.warn("$this DslGroupKsp process start")
        val symbols = resolver.getSymbolsWithAnnotation(DslGroup::class.java.canonicalName)

        val dslGroups = mutableSetOf<KSType>()

//        if(symbols.any { !it.validate() }) error("symbols any validate")

        symbols.forEach { symbol ->
            symbol.annotations.filter { it.shortName.asString() == DslGroup::class.simpleName }
                .forEach { annotation ->
                    annotation.arguments.forEach { argument ->
                        @Suppress("UNCHECKED_CAST")
                        when (argument.name?.asString()) {
                            "groupClasses" -> dslGroups.addAll(argument.value as ArrayList<KSType>)
                        }
                    }
                }
        }

        logger.warn("DslGroupKsp dslGroups size = ${dslGroups.size}")

//        error("dslGroup size = ${dslGroups.size}")

        if (dslGroups.any { it.isError }) return symbols.toList()

        val groupSequence =
            dslGroups.asSequence().map { Pair(it.toClassName(), it.layoutKSType.toClassName()) }
        GroupPoet.process(groupSequence) {
            try {
                it.writeTo(codeGenerator, true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        logger.warn("DslGroupKsp process end")
        return emptyList()
    }


    private val KSType.layoutKSType: KSType
        get() {
            val classDeclaration = declaration as KSClassDeclaration
            return classDeclaration.layoutKSType ?: classDeclaration.getAllSuperTypes()
                .firstNotNullOf { it.layoutKSType }
        }

    private val KSClassDeclaration.layoutKSType: KSType?
        get() = declarations.filterIsInstance<KSClassDeclaration>()
            .firstOrNull { it.isLayoutType }?.asType(emptyList())

    private val KSClassDeclaration.isLayoutType: Boolean
        get() = superTypes.any {
            (it.resolve().declaration as KSClassDeclaration).qualifiedName!!.asString() ==
                    "android.view.ViewGroup.MarginLayoutParams"
        }

}


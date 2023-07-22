package wedo.widemouth.compiler.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.compiler.allNestedClasses
import wedo.widemouth.compiler.annotationsIsType
import wedo.widemouth.compiler.findClassesInArguments
import wedo.widemouth.compiler.generator.DslGroupGenerator
import wedo.widemouth.compiler.generator.DslGroupGenerator.sReceiverSuffix
import wedo.widemouth.compiler.packageName
import java.io.IOException

class DslGroupKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
		DslGroupKsp(environment)
}

class DslGroupKsp(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

	private val mTasks =
		mapOf(
			"ScopeGroup" to DslGroupGenerator::generateScopeGroup,
			"Group" to DslGroupGenerator::generateGroup,
			"GroupWithDefaultLP" to DslGroupGenerator::generateGroupWithDefaultLP,
			"PartialAppliedGroup" to DslGroupGenerator::generatePartialAppliedGroup,
			"PartialAppliedGroupWithDefaultLP" to DslGroupGenerator::generatePartialAppliedGroupWithDefaultLP
		)

	override fun process(resolver: Resolver): List<KSAnnotated> {

		val symbols = resolver.getSymbolsWithAnnotation(DslGroup::class.java.name)

		if (symbols.none()) return emptyList()

		var deferred = false

		val groups = symbols.flatMap { symbol ->
			symbol.annotationsIsType<DslGroup>().flatMap { annotation ->
				annotation.findClassesInArguments().also { if (it.none()) deferred = true }
			}
		}.distinct()

		// terminate groups.
		val groupCount = groups.count()

		if (deferred) return symbols.toList()

		if (groupCount == 0) return emptyList()

		val generatedCodePackageName = symbols.firstNotNullOfOrNull { it.packageName }
			?: error("Can not find packageName from symbols with [DslGroup].")

		val layoutBaseClass =
			resolver.getClassDeclarationByName("android.view.ViewGroup.LayoutParams")
				?.asStarProjectedType()
				?: error("Can not find class [android.view.ViewGroup.LayoutParams] in your compiled module")

		val pairs = groups.map { group ->
			Pair(group.toClassName(),
				group.allNestedClasses.first { layoutBaseClass.isAssignableFrom(it.asStarProjectedType()) }
					.toClassName())
		}

		val groupReceiverFileBuilder =
			FileSpec.scriptBuilder("GroupReceiver", generatedCodePackageName)

		pairs.forEach {
			groupReceiverFileBuilder.addCode(
				DslGroupGenerator.generateGroupReceiver(it.first, it.second)
			)
		}

		groupReceiverFileBuilder.build().write()

		mTasks.forEach { task ->
			val fileBuilder = FileSpec.builder(generatedCodePackageName, task.key)
			pairs.forEach {
				val funSpec = task.value(it.first, it.second) {
					ClassName(generatedCodePackageName, "$simpleName$sReceiverSuffix")
				}
				fileBuilder.addFunction(funSpec)
			}

			fileBuilder.build().write()
		}

		return emptyList()
	}

	private fun FileSpec.write() {
		try {
			writeTo(environment.codeGenerator, true)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}


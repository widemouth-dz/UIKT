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
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ExperimentalKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslGroup
import wedo.widemouth.annotation.DslWidget
import java.io.IOException
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.superclasses

@AutoService(SymbolProcessorProvider::class)
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
		val symbols = resolver.getSymbolsWithAnnotation(DslGroup::class.java.canonicalName)
		val mDslWidgets = mutableSetOf<KSType>()
		symbols.forEach { symbol ->
			symbol.annotations.filter { it.shortName.asString() == DslGroup::class.simpleName }
				.forEach { annotation ->
					annotation.arguments.forEach { argument ->
						@Suppress("UNCHECKED_CAST")
						when (argument.name?.asString()) {
							"groupClasses" -> mDslWidgets.addAll(argument.value as ArrayList<KSType>)
						}
					}
				}
		}

		mapOf<String, (ClassName) -> FunSpec>(
			"Group" to ::buildGroupFun,
		).forEach { task ->
			val fileBuilder = FileSpec.builder(ClassName(sPackage, task.key))
			mDslWidgets.asSequence().map { it.toClassName() }.forEach {
				fileBuilder.addFunction(task.value(it))
			}
			try {
				fileBuilder.build().writeTo(codeGenerator, false)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

		return symbols.toList()
	}

	private fun buildGroupFun(groupName: ClassName): FunSpec {
		val layoutParamsName = groupName.layoutParamsName
		val sGroupBlockParameter =
			groupReceiverParameterBuilder(groupName, sWidgetLayoutTypeVariableName,layoutParamsName)

		groupName.nestedClass("LayoutParams").reflectionName()
		return FunSpec.builder(groupName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(sContextParameter)
			.addParameter(sWidgetLayoutBuilderParameter)
			.addParameter(sGroupBlockParameter)
			.returns(groupName)
			.addStatement(
				"return %M(%L,·%L,·%L,·%L)",
				sGroupFunName,
				sContextParameter.name,
				groupName.constructorReference(),
				sWidgetLayoutBuilderParameter.name,
				sGroupBlockParameter.name
			).build()
	}

	private val ClassName.layoutParamsName: ClassName
		get() {
//			val sLayoutParamsClass = Class.forName(sLayoutParamsName.reflectionName()).kotlin
			val kClass = Class.forName(reflectionName()).kotlin
			kClass.superclasses.forEach { superclass ->
				superclass.nestedClasses.forEach { nestedClass ->
//					if (nestedClass.isSubclassOf(sLayoutParamsClass)) return nestedClass.asClassName()
				}
			}
			error("Can not find LayoutParams class from $simpleName")
		}


	private fun buildWidgetPartialFun(widgetName: ClassName): FunSpec {
		val sWidgetBlockParameter =
			sWidgetBlockParameterWithNoInlineBuilder(widgetName, sWidgetLayoutTypeVariableName)
		return FunSpec.builder(widgetName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(sWidgetLayoutBuilderParameter)
			.addParameter(sWidgetBlockParameter)
			.returns(sViewBuilderName.parameterizedBy(widgetName))
			.addStatement(
				"return %M(%L,·${sWidgetLayoutBuilderParameter.name},·${sWidgetBlockParameter.name})",
				sGroupFunName, widgetName.constructorReference()
			).build()
	}

	private fun buildWidgetPartialFunWithDefaultLP(widgetName: ClassName): FunSpec {
		val sWidgetBlockParameter = sWidgetBlockParameterBuilder(widgetName, sMarginLPName)
		return FunSpec.builder(widgetName.simpleName)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(sWidgetBlockParameter)
			.returns(sViewBuilderName.parameterizedBy(widgetName))
			.addStatement(
				"return %M(%L,·%L,·${sWidgetBlockParameter.name})",
				sGroupFunName, widgetName.constructorReference(), sMarginLayoutFunName.reference()
			).build()
	}

	companion object {

		private const val sPackage = "wedo.widemouth.generated"
		private const val sUiktPackageName = "wedo.widemouth.uikt"
		private const val sWidget = "Widgets"

		private val sLayoutParamsName = ClassName("android.view", "ViewGroup","LayoutParams")


		private val sLPName = ClassName(sUiktPackageName, "LP")

		private const val sWidgetLayoutTypeVariable = "VL"
		private val sWidgetLayoutTypeVariableName = TypeVariableName(sWidgetLayoutTypeVariable)
		private val sWidgetLayoutWithBoundTypeVariableName =
			TypeVariableName(sWidgetLayoutTypeVariable, sLPName)

		private val sContextName = ClassName("android.content", "Context")
		private val sContextParameter = ParameterSpec.builder("ctx", sContextName).build()

		private val sWidgetProviderName = ClassName(sPackage, sWidget)

		private val sLayoutBuilderName = ClassName(sUiktPackageName, "LayoutBuilder")
		private val sViewBuilderName = ClassName(sUiktPackageName, "ViewBuilder")

		private val sWidgetLayoutName =
			sLayoutBuilderName.parameterizedBy(sWidgetLayoutTypeVariableName)

		private val sWidgetLayoutBuilderParameter =
			ParameterSpec.builder("widgetLayoutBuilder", sWidgetLayoutName)
				.addModifiers(KModifier.CROSSINLINE).build()

		private val sWidgetReceiverName = ClassName(sUiktPackageName, "WidgetReceiver")

		private val sWidgetBlockParameterWithNoInlineBuilder =
			{ widgetTypeName: TypeName, widgetLayoutTypeName: TypeName ->
				ParameterSpec.builder(
					"block",
					sWidgetReceiverName.parameterizedBy(widgetTypeName, widgetLayoutTypeName)
				).addModifiers(KModifier.NOINLINE).build()
			}

		private val sScopeName = ClassName(sUiktPackageName, "Scope")
		private val sScopeMarkerName = ClassName(sUiktPackageName, "ScopeMarker")
		private val sViewMarkerName = ClassName(sUiktPackageName, "ViewMarker")
		private val sLayoutMarkerName = ClassName(sUiktPackageName, "LayoutMarker")

		@OptIn(ExperimentalKotlinPoetApi::class)
		private val groupReceiverParameterBuilder =
			{ groupTypeName: TypeName, groupLayoutTypeName: TypeName, groupScopeLayoutTypeName: TypeName ->
				val groupLayoutType = groupLayoutTypeName.copy(
					annotations = listOf(AnnotationSpec.builder(sLayoutMarkerName).build())
				)
				val groupType = groupTypeName.copy(
					annotations = listOf(AnnotationSpec.builder(sViewMarkerName).build())
				)
				val scopeType = sScopeName.parameterizedBy(groupScopeLayoutTypeName).copy(
					annotations = listOf(AnnotationSpec.builder(sScopeMarkerName).build())
				)
				ParameterSpec.builder(
					"block",
					LambdaTypeName.get(
						receiver = groupLayoutType,
						returnType = Unit::class.asTypeName(),
						contextReceivers = listOf(groupType, scopeType)
					)
				).build()
			}

		private val sWidgetBlockParameterBuilder =
			{ widgetTypeName: TypeName, widgetLayoutTypeName: TypeName ->
				ParameterSpec.builder(
					"block",
					sWidgetReceiverName.parameterizedBy(widgetTypeName, widgetLayoutTypeName)
				).build()
			}

		private val sGroupFunName = MemberName(sUiktPackageName, "Group")
		private val sMarginLayoutFunName = MemberName(sUiktPackageName, "marginLayout")

		private val sMarginLPName = ClassName(sUiktPackageName, "MarginLP")

	}
}


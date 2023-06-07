package wedo.widemouth.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName

/**
 * @author WideMouth
 * @since 2023/6/3
 */
object DslWidgetGenerator{

	fun generateScopeWidget(widgetName: ClassName): FunSpec {
		val widgetReceiverName =
			sWidgetReceiverName.parameterizedBy(widgetName, sScopeLayoutTypeVariableName)

		val widgetBlockParameter = ParameterSpec.builder("block", widgetReceiverName).build()

		return FunSpec.builder(widgetName.simpleName)
			.addTypeVariable(sScopeLayoutWithBoundTypeVariableName)
			.receiver(sScopeName.parameterizedBy(sScopeLayoutTypeVariableName))
			.addParameter(widgetBlockParameter)
			.returns(widgetName)
			.addStatement(
				"return %M(%L,·%L)",
				sWidgetFunName, widgetName.constructorReference(), widgetBlockParameter.name
			).build()
	}

	fun generateWidget(widgetName: ClassName): FunSpec {
		val widgetBlockParameter = ParameterSpec.builder(
			"block", sWidgetReceiverName.parameterizedBy(widgetName, sWidgetLayoutTypeVariableName)
		).addModifiers(KModifier.NOINLINE).build()
		return FunSpec.builder(widgetName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(sContextParameter)
			.addParameter(sWidgetLayoutBuilderParameter)
			.addParameter(widgetBlockParameter)
			.returns(widgetName)
			.addStatement(
				"return %M(%L,·%L,·%L,·%L)",
				sWidgetFunName,
				sContextParameter.name,
				widgetName.constructorReference(),
				sWidgetLayoutBuilderParameter.name,
				widgetBlockParameter.name
			).build()
	}

	fun generateWidgetWithDefaultLP(widgetName: ClassName): FunSpec {
		val widgetBlockParameter = ParameterSpec.builder(
			"block", sWidgetReceiverName.parameterizedBy(widgetName, sMarginLPName)
		).build()
		return FunSpec.builder(widgetName.simpleName)
			.addParameter(sContextParameter)
			.addParameter(widgetBlockParameter)
			.returns(widgetName)
			.addStatement(
				"return %M(%L,·%L,·%L,·%L)",
				sWidgetFunName,
				sContextParameter.name,
				widgetName.constructorReference(),
				sMarginLayoutFunName.reference(),
				widgetBlockParameter.name
			).build()
	}

	fun generatePartialWidget(widgetName: ClassName): FunSpec {
		val widgetReceiverName =
			sWidgetReceiverName.parameterizedBy(widgetName, sWidgetLayoutTypeVariableName)

		val widgetBlockParameter = ParameterSpec.builder("block", widgetReceiverName)
			.addModifiers(KModifier.NOINLINE).build()

		return FunSpec.builder(widgetName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(sWidgetLayoutBuilderParameter)
			.addParameter(widgetBlockParameter)
			.returns(sViewBuilderName.parameterizedBy(widgetName))
			.addStatement(
				"return %M(%L,·%L,·%L)",
				sWidgetFunName,
				widgetName.constructorReference(),
				sWidgetLayoutBuilderParameter.name,
				widgetBlockParameter.name
			).build()
	}

	fun generatePartialWidgetWithDefaultLPFun(widgetName: ClassName): FunSpec {
		val widgetReceiverName =
			sWidgetReceiverName.parameterizedBy(widgetName, sMarginLPName)
		val widgetBlockParameter = ParameterSpec.builder("block", widgetReceiverName).build()
		return FunSpec.builder(widgetName.simpleName)
			.addTypeVariable(sWidgetLayoutWithBoundTypeVariableName)
			.addParameter(widgetBlockParameter)
			.returns(sViewBuilderName.parameterizedBy(widgetName))
			.addStatement(
				"return %M(%L,·%L,·%L)",
				sWidgetFunName,
				widgetName.constructorReference(),
				sMarginLayoutFunName.reference(),
				widgetBlockParameter.name
			).build()
	}


	private const val sUiktPackageName = "wedo.widemouth.uikt"

	private val sLPName = ClassName(sUiktPackageName, "LP")

	private const val sWidgetLayoutTypeVariable = "VL"
	private val sWidgetLayoutTypeVariableName = TypeVariableName(sWidgetLayoutTypeVariable)
	private val sWidgetLayoutWithBoundTypeVariableName =
		TypeVariableName(sWidgetLayoutTypeVariable, sLPName)

	private val sScopeName = ClassName(sUiktPackageName, "Scope")
	private const val sScopeLayoutTypeVariable = "SL"
	private val sScopeLayoutTypeVariableName = TypeVariableName(sScopeLayoutTypeVariable)
	private val sScopeLayoutWithBoundTypeVariableName =
		TypeVariableName(sScopeLayoutTypeVariable, sLPName)

	private val sContextName = ClassName("android.content", "Context")
	private val sContextParameter = ParameterSpec.builder("ctx", sContextName).build()


	private val sLayoutBuilderName = ClassName(sUiktPackageName, "LayoutBuilder")
	private val sViewBuilderName = ClassName(sUiktPackageName, "ViewBuilder")

	private val sWidgetLayoutName =
		sLayoutBuilderName.parameterizedBy(sWidgetLayoutTypeVariableName)

	private val sWidgetLayoutBuilderParameter =
		ParameterSpec.builder("widgetLayoutBuilder", sWidgetLayoutName)
			.addModifiers(KModifier.CROSSINLINE).build()

	private val sWidgetReceiverName = ClassName(sUiktPackageName, "WidgetReceiver")

	private val sWidgetFunName = MemberName(sUiktPackageName, "Widget")
	private val sMarginLayoutFunName = MemberName(sUiktPackageName, "marginLayout")

	private val sMarginLPName = ClassName(sUiktPackageName, "MarginLP")

}
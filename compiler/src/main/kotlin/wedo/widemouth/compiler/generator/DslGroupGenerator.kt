package wedo.widemouth.compiler.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
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
object DslGroupGenerator {

	fun generateGroupReceiver(groupName: ClassName, layoutParamsName: ClassName): CodeBlock =
		CodeBlock.of(
			"typealias %L$sReceiverSuffix<SL> = context((@%T·%T),·(@%T·%T<%T>)) (@%T·SL).()·->·Unit\n\n",
			groupName.simpleName,
			sViewMarkerName,
			groupName,
			sScopeMarkerName,
			sScopeName,
			layoutParamsName,
			sLayoutMarkerName,
		)

	fun generateScopeGroup(
		groupName: ClassName,
		layoutParamsName: ClassName,
		receiverName: ClassName.() -> ClassName,
	): FunSpec {
		val sGroupBlockParameter = ParameterSpec.builder(
			"block", groupName.receiverName().parameterizedBy(sScopeLayoutTypeVariableName)
		).build()

		return FunSpec.builder(groupName.simpleName)
			.addTypeVariable(sScopeLayoutWithBoundTypeVariableName)
			.receiver(sScopeName.parameterizedBy(sScopeLayoutTypeVariableName))
			.addParameter(sGroupBlockParameter)
			.returns(groupName)
			.addStatement(
				"return %M(%L,·%L,·%L)",
				sGroupFunName,
				groupName.constructorReference(),
				layoutParamsName.constructorReference(),
				sGroupBlockParameter.name
			).build()
	}

	fun generateGroup(
		groupName: ClassName,
		layoutParamsName: ClassName,
		receiverName: ClassName.() -> ClassName,
	): FunSpec {
		val sGroupBlockParameter = ParameterSpec.builder(
			"block", groupName.receiverName().parameterizedBy(sGroupLayoutTypeVariableName)
		).addModifiers(KModifier.NOINLINE).build()

		return FunSpec.builder(groupName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sGroupLayoutWithBoundTypeVariableName)
			.addParameter(sContextParameter)
			.addParameter(sGroupLayoutBuilderParameter)
			.addParameter(sGroupBlockParameter)
			.returns(groupName)
			.addStatement(
				"return %M(%L,·%L,·%L,·%L,·%L)",
				sRootFunName,
				sContextParameter.name,
				groupName.constructorReference(),
				layoutParamsName.constructorReference(),
				sGroupLayoutBuilderParameter.name,
				sGroupBlockParameter.name
			).build()
	}

	fun generateGroupWithDefaultLP(
		groupName: ClassName,
		layoutParamsName: ClassName,
		receiverName: ClassName.() -> ClassName,
	): FunSpec {
		val sGroupBlockParameter = ParameterSpec.builder(
			"block", groupName.receiverName().parameterizedBy(sMarginLPName)
		).build()

		return FunSpec.builder(groupName.simpleName)
			.addParameter(sContextParameter)
			.addParameter(sGroupBlockParameter)
			.returns(groupName)
			.addStatement(
				"return %M(%L,·%L,·%L,·%L,·%L)",
				sRootFunName,
				sContextParameter.name,
				groupName.constructorReference(),
				layoutParamsName.constructorReference(),
				sRootLayoutFunName.reference(),
				sGroupBlockParameter.name
			).build()
	}

	fun generatePartialAppliedGroup(
		groupName: ClassName,
		layoutParamsName: ClassName,
		receiverName: ClassName.() -> ClassName,
	): FunSpec {
		val groupBlockParameter = ParameterSpec.builder(
			"block", groupName.receiverName().parameterizedBy(sGroupLayoutTypeVariableName)
		).addModifiers(KModifier.NOINLINE).build()

		val groupLayoutBuilderParameter =
			sGroupLayoutBuilderParameter.toBuilder().addModifiers(KModifier.CROSSINLINE).build()

		return FunSpec.builder(groupName.simpleName)
			.addModifiers(KModifier.INLINE)
			.addTypeVariable(sGroupLayoutWithBoundTypeVariableName)
			.addParameter(groupLayoutBuilderParameter)
			.addParameter(groupBlockParameter)
			.returns(sScopeViewBuilderName.parameterizedBy(groupName))
			.addStatement(
				"return %M(%L,·%L,·%L,·%L)",
				sRootFunName,
				groupName.constructorReference(),
				layoutParamsName.constructorReference(),
				sGroupLayoutBuilderParameter.name,
				groupBlockParameter.name
			).build()
	}

	fun generatePartialAppliedGroupWithDefaultLP(
		groupName: ClassName,
		layoutParamsName: ClassName,
		receiverName: ClassName.() -> ClassName,
	): FunSpec {
		val groupBlockParameter = ParameterSpec.builder(
			"block", groupName.receiverName().parameterizedBy(sMarginLPName)
		).build()

		return FunSpec.builder(groupName.simpleName)
			.addTypeVariable(sGroupLayoutWithBoundTypeVariableName)
			.addParameter(groupBlockParameter)
			.returns(sScopeViewBuilderName.parameterizedBy(groupName))
			.addStatement(
				"return %M(%L,·%L,·%L,·%L)",
				sRootFunName,
				groupName.constructorReference(),
				layoutParamsName.constructorReference(),
				sRootLayoutFunName.reference(),
				groupBlockParameter.name
			).build()
	}

	const val sReceiverSuffix = "Receiver"

	private const val sUiktPackageName = "wedo.widemouth.uikt"

	private val sLPName = ClassName(sUiktPackageName, "LP")

	private const val sGroupLayoutTypeVariable = "GL"
	private val sGroupLayoutTypeVariableName = TypeVariableName(sGroupLayoutTypeVariable)
	private val sGroupLayoutWithBoundTypeVariableName =
		TypeVariableName(sGroupLayoutTypeVariable, sLPName)

	private val sContextName = ClassName("android.content", "Context")
	private val sContextParameter = ParameterSpec.builder("ctx", sContextName).build()

	private val sLayoutBuilderName = ClassName(sUiktPackageName, "LayoutBuilder")
	private val sScopeViewBuilderName = ClassName(sUiktPackageName, "ScopeViewBuilder")

	private val sGroupLayoutName =
		sLayoutBuilderName.parameterizedBy(sGroupLayoutTypeVariableName)

	private val sGroupLayoutBuilderParameter =
		ParameterSpec.builder("groupLayoutBuilder", sGroupLayoutName).build()

	private val sScopeMarkerName = ClassName(sUiktPackageName, "ScopeMarker")
	private val sViewMarkerName = ClassName(sUiktPackageName, "ViewMarker")
	private val sLayoutMarkerName = ClassName(sUiktPackageName, "LayoutMarker")

	private val sRootFunName = MemberName(sUiktPackageName, "Root")
	private val sGroupFunName = MemberName(sUiktPackageName, "Group")
	private val sRootLayoutFunName = MemberName(sUiktPackageName, "rootLayout")

	private val sMarginLPName = ClassName(sUiktPackageName, "MarginLP")

	private val sScopeName = ClassName(sUiktPackageName, "Scope")
	private const val sScopeLayoutTypeVariable = "SL"
	private val sScopeLayoutTypeVariableName = TypeVariableName(sScopeLayoutTypeVariable)
	private val sScopeLayoutWithBoundTypeVariableName =
		TypeVariableName(sScopeLayoutTypeVariable, sLPName)

}
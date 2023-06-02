package wedo.widemouth.compiler.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import wedo.widemouth.annotation.DslGroup
import java.io.IOException

@AutoService(SymbolProcessorProvider::class)
class ViewEffectKspProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
		ViewEffectKsp(environment.codeGenerator, environment.logger, environment.options)
}

class ViewEffectKsp(
	private val codeGenerator: CodeGenerator,
	private val logger: KSPLogger,
	private val options: Map<String, String>,
) : SymbolProcessor {

	override fun process(resolver: Resolver): List<KSAnnotated> {
		val symbols = resolver.getSymbolsWithAnnotation(DslGroup::class.java.canonicalName)
		val mViewEffectWidgets = mutableSetOf<KSType>()
		symbols.forEach { symbol -> symbol.accept(ViewEffectVisitor(), mViewEffectWidgets) }
		mViewEffectWidgets.forEach(::buildViewEffectWidget)
		return symbols.toList()
	}

	private fun buildViewEffectWidget(widgetType: KSType) {
		val widgetName = widgetType.toClassName()

		val nameExt = "${widgetName.simpleName}Ext"

		logger.logging("$TAG: buildViewEffectWidget: ${widgetName.simpleName} --> $nameExt")

		val widgetExtName = ClassName(sViewEffectPackageName, nameExt)

		val contextParameter = ParameterSpec("context", sContextName)
		val attrsParameter = ParameterSpec.builder("attrs", sAttributeSetName.copy(nullable = true))
			.defaultValue("%L", "null").build()
		val defStyleAttrParameter =
			ParameterSpec.builder("defStyleAttr", Int::class).defaultValue("%L", "0").build()
		val defStyleResParameter =
			ParameterSpec.builder("defStyleRes", Int::class).defaultValue("%L", "0").build()
		val primaryConstructor = FunSpec.constructorBuilder()
			.addParameter(contextParameter)
			.addParameter(attrsParameter)
			.addParameter(defStyleAttrParameter)
			.addParameter(defStyleResParameter)
			.addAnnotation(JvmOverloads::class)
			.build()


		val viewEffectHelperDelegate = CodeBlock.of(
			"%T(%L, %L, %L, %L)", sViewEffectHelperName, contextParameter.name,
			attrsParameter.name,
			defStyleAttrParameter.name,
			defStyleResParameter.name
		)

		val initBlock = CodeBlock.builder()
			.addStatement("@Suppress(\"LeakingThis\")")
			.addStatement("owner = this")
			.build()

		val widthMeasureSpecParameter = ParameterSpec("widthMeasureSpec", Int::class.asTypeName())
		val heightMeasureSpecParameter = ParameterSpec("heightMeasureSpec", Int::class.asTypeName())
		val onMeasureFun = FunSpec.builder("onMeasure")
			.addModifiers(KModifier.OVERRIDE)
			.addParameter(widthMeasureSpecParameter)
			.addParameter(heightMeasureSpecParameter)
			.beginControlFlow(
				"measureDelegate(%L, %L)",
				widthMeasureSpecParameter.name,
				heightMeasureSpecParameter.name
			)
			.addStatement("%L, %L ->", "widthMeasuredSpec", "heightMeasuredSpec")
			.addStatement("super.onMeasure(%L, %L)", "widthMeasuredSpec", "heightMeasuredSpec")
			.endControlFlow()
			.build()

		val canvasParameter = ParameterSpec("canvas", sCanvasName)

		val dispatchDrawFun = FunSpec.builder("dispatchDraw")
			.addModifiers(KModifier.OVERRIDE)
			.addParameter(canvasParameter)
			.addStatement("super.dispatchDraw(%L)", canvasParameter.name)
			.addStatement("drawDividers(%L)", canvasParameter.name)
			.addStatement("drawDividers(%L)", canvasParameter.name)
			.build()

		val widgetExtType = TypeSpec.classBuilder(widgetExtName)
			.addModifiers(KModifier.OPEN)
			.primaryConstructor(primaryConstructor)
			.superclass(widgetName)
			.addSuperclassConstructorParameter(
				"%L, %L, %L",
				contextParameter.name,
				attrsParameter.name,
				defStyleAttrParameter.name
			)
			.addSuperinterface(sIViewEffectName, viewEffectHelperDelegate)
			.addInitializerBlock(initBlock)
			.addFunction(onMeasureFun)
			.addFunction(dispatchDrawFun)
			.build()

		try {
			FileSpec.builder(widgetExtName).addType(widgetExtType).build()
				.writeTo(codeGenerator, false)
		} catch (e: IOException) {
			e.printStackTrace()
		}

	}

	inner class ViewEffectVisitor : KSDefaultVisitor<MutableSet<KSType>, Unit>() {
		override fun visitClassDeclaration(
			classDeclaration: KSClassDeclaration, data: MutableSet<KSType>,
		) {
			classDeclaration.annotations.forEach { annotation ->
				annotation.arguments.forEach { argument ->
					logger.info("WideMouth" + argument.value!!.javaClass.toString())
					@Suppress("UNCHECKED_CAST")
					when (argument.name?.asString()) {
						"widgetClasses" -> data.addAll(argument.value as ArrayList<KSType>)
					}
				}
			}
		}

		override fun defaultHandler(node: KSNode, data: MutableSet<KSType>) {}
	}


	companion object {
		private const val TAG = "ViewEffectKspProcessor"

		private const val sViewEffectPackageName = "wedo.widemouth.uikt.decoration"
		private val sIViewEffectName = ClassName(sViewEffectPackageName, "IViewEffect")
		private val sViewEffectHelperName = ClassName(sViewEffectPackageName, "ViewEffectHelper")

		private val sContextName = ClassName("android.content", "Context")
		private val sAttributeSetName = ClassName("android.util", "AttributeSet")
		private val sCanvasName = ClassName("android.graphics", "Canvas")
	}
}


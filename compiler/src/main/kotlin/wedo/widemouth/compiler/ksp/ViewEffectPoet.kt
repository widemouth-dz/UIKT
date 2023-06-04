package wedo.widemouth.compiler.ksp

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName

/**
 * @author WideMouth
 * @since 2023/6/3
 */
object ViewEffectPoet {

    fun process(widgets: Sequence<ClassName>, fileProcessor: (FileSpec) -> Unit) {
        widgets.forEach {
            fileProcessor(buildViewEffectWidget(it))
        }
    }

    private fun buildViewEffectWidget(widgetName: ClassName): FileSpec {

        val nameExt = "${widgetName.simpleName}Ext"

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

        return FileSpec.builder(sViewEffectPackageName, nameExt).addType(widgetExtType).build()
    }


    private const val sViewEffectPackageName = "wedo.widemouth.uikt.decoration"
    private val sIViewEffectName = ClassName(sViewEffectPackageName, "IViewEffect")
    private val sViewEffectHelperName = ClassName(sViewEffectPackageName, "ViewEffectHelper")

    private val sContextName = ClassName("android.content", "Context")
    private val sAttributeSetName = ClassName("android.util", "AttributeSet")
    private val sCanvasName = ClassName("android.graphics", "Canvas")

}
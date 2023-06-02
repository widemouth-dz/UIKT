package wedo.widemouth.compiler

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.tools.Diagnostic

abstract class BaseProcessor : AbstractProcessor() {

	protected lateinit var mFiler: Filer
	protected lateinit var mMessager: Messager

	override fun init(processingEnv: ProcessingEnvironment) {
		super.init(processingEnv)
		mFiler = processingEnv.filer
		mMessager = processingEnv.messager
	}

	override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

	protected fun error(msg: String, vararg args: Any) {
		mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, *args))
	}

	protected fun info(msg: String, vararg args: Any) {
		mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, *args))
	}
}
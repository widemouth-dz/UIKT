package wedo.widemouth.compiler

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType

inline fun <reified T : Annotation> KSAnnotated.findClassesInAnnotationIsType(): Sequence<KSClassDeclaration> =
	annotationsIsType<T>().flatMap { it.findClassesInArguments() }

inline fun <reified T : Annotation> KSAnnotated.annotationsIsType(): Sequence<KSAnnotation> =
	T::class.let { annotationKClass ->
		annotations.filter {
			it.shortName.getShortName() == annotationKClass.simpleName &&
					it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationKClass.qualifiedName
		}
	}

fun KSAnnotation.findClassesInArguments(): List<KSClassDeclaration> =
	arguments
		.flatMap {
			when (val value = it.value) {
				is List<*> -> value.asSequence()
				else -> sequenceOf(value)
			}
		}
		.filterIsInstance<KSType>()
		.map { it.declaration }
		.filterIsInstance<KSClassDeclaration>()

val KSClassDeclaration.allNestedClasses: Sequence<KSClassDeclaration>
	get() = declarations.filterIsInstance<KSClassDeclaration>() +
			getAllSuperTypes()
				.map { it.declaration }
				.filterIsInstance<KSClassDeclaration>()
				.flatMap { it.declarations }.filterIsInstance<KSClassDeclaration>()

val KSAnnotated.packageName: String?
	get() = when (this) {
		is KSDeclaration -> packageName
		is KSFile -> packageName
		else -> null
	}?.asString()
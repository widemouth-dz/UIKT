package wedo.widemouth.compiler

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyAccessor
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.symbol.KSPropertySetter
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSValueParameter
import kotlin.reflect.KClass

fun Sequence<KSAnnotated>.findClassesInAnnotation(
	annotation: KClass<out Annotation>,
): Sequence<KSClassDeclaration> = flatMap { it.findClassesInAnnotation(annotation) }

fun KSAnnotated.findClassesInAnnotation(
	annotation: KClass<out Annotation>,
): Sequence<KSClassDeclaration> =
	annotations
		.filter { it.shortName.asString() == annotation.simpleName }
		.flatMap { it.arguments }
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
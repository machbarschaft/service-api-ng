package com.colivery.serviceaping.rest.v1.dto

import org.springframework.stereotype.Component
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [EnumValidator::class])
annotation class EnumValue(
        val enumClass: KClass<out Enum<*>>,
        val message: String = "Invalid value",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

@Component
class EnumValidator : ConstraintValidator<EnumValue, String> {
    private lateinit var acceptedValues: List<String>

    override fun initialize(annotation: EnumValue) {
        acceptedValues = annotation.enumClass.java.enumConstants
                .map { it.toString().toUpperCase() }
                .toList()
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}

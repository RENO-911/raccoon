package nl.rm.raccoon.domain

interface Validator {
    fun isValid(question: Question): ValidationResult
}

sealed interface ValidationResult {
    val isValid: Boolean

    object Valid: ValidationResult {
        override val isValid = true
    }

    data class Invalid(
        val message: String
    ): ValidationResult {
        override val isValid = false
    }
}

class Validators(
    validators: List<Validator>
): Validator, List<Validator> by validators {
    override fun isValid(question: Question): ValidationResult {
        // TODO: We call it twice, but I ain't worried bout no performance, fk for-loops ^^
        return firstOrNull { it.isValid(question).isValid }?.isValid(question) ?: ValidationResult.Valid
    }
}

fun validators(
    vararg validators: Validator
): Validators {
    return Validators(validators.toList())
}

class LambdaValidator(
    private val message: String,
    val validator: Question.() -> Boolean
): Validator {
    override fun isValid(question: Question): ValidationResult {
        return if (question.validator()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message)
        }
    }
}

object AlwaysValidValidator: Validator {
    override fun isValid(question: Question): ValidationResult {
        return ValidationResult.Valid
    }
}

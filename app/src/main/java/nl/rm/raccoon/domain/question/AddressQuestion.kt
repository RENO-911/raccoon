package nl.rm.raccoon.domain.question

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.ValidationResult
import nl.rm.raccoon.domain.Validator

data class AddressQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    override val isValidWhen: Validator
) : Question {

    val postalCode: String
        get() = answer?.value ?: ""

    val houseNumber: String
        get() = answer?.value ?: ""

    override val isValid: ValidationResult
        get() = isValidWhen.isValid(this)

    override var answer: Answer? = null
        private set

    override fun answer(answer: Answer) {
        this.answer = answer
    }
}
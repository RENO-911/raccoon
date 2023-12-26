package nl.rm.raccoon.domain.question

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.ValidationResult
import nl.rm.raccoon.domain.Validator

interface Question {
    val id: String
    val title: String
    val answer: Answer?
    val isRelevant: () -> Boolean
    val isValidWhen: Validator
    val isValid: ValidationResult

    fun answer(answer: Answer)

    val interactionState: InteractionState
        get() = InteractionState.Untouched

    enum class InteractionState {
        Untouched,
        Touched,
    }
}

interface LimitedOptionsQuestion : Question {
    val options: Set<Answer>
}
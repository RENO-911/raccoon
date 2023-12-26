package nl.rm.raccoon.domain.question

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.LambdaValidator
import nl.rm.raccoon.domain.LimitedOptionsQuestion
import nl.rm.raccoon.domain.ValidationResult
import nl.rm.raccoon.domain.Validator

data class MultiSelectQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    override val options: Set<Answer>
) : LimitedOptionsQuestion {

    override val isValidWhen: Validator
        get() {
            return LambdaValidator(
                message = "Please select one of the available options",
                validator = {
                    // TODO: Check this possibly broken implementation :D
                    options.map { it.value }.containsAll(answer?.value?.split(",") ?: emptyList())                }
            )
        }

    override val isValid: ValidationResult
        get() = isValidWhen.isValid(this)

    private val _answer = mutableSetOf<Answer>()
    override val answer: Answer?
        get() = if (_answer.isEmpty()) null else AnswerString(_answer.joinToString(",") { it.value })

    override fun answer(answer: Answer) {
        if (_answer.contains(answer)) {
            _answer.remove(answer)
        } else {
            _answer.add(answer)
        }
    }
}
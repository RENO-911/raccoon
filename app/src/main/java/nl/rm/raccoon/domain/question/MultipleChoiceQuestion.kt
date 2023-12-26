package nl.rm.raccoon.domain.question

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.LambdaValidator
import nl.rm.raccoon.domain.ValidationResult
import nl.rm.raccoon.domain.Validator

data class MultipleChoiceQuestion(
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
                    options.contains(answer)
                }
            )
        }

    override val isValid: ValidationResult
        get() = isValidWhen.isValid(this)

    override var answer: Answer? = null
        private set

    override fun answer(answer: Answer) {
        this.answer = answer
    }
}
package nl.rm.raccoon.domain

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

data class OpenQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    override val isValidWhen: Validator
) : Question {

    override val isValid: ValidationResult
        get() = isValidWhen.isValid(this)

    override var answer: Answer? = null
        private set

    override fun answer(answer: Answer) {
        this.answer = answer
    }
}

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

data class PhotoQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    override val isValidWhen: Validator
) : Question {

    override val isValid: ValidationResult
        get() = isValidWhen.isValid(this)

    override var answer: Answer? = null
        private set

    override fun answer(answer: Answer) {
        this.answer = answer
    }
}
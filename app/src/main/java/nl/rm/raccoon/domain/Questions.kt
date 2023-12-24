package nl.rm.raccoon.domain

interface Question {
    val id: String
    val title: String
    val answer: String?
    val isRelevant: () -> Boolean
    val isValidWhen: Question.() -> Boolean
    val isValid: Boolean

    fun answer(answer: String)
}

data class MultipleChoiceQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    val options: Set<String>
) : Question {

    override val isValidWhen: Question.() -> Boolean = {
        options.contains(answer)
    }

    override val isValid: Boolean
        get() = isValidWhen()

    override var answer: String? = null
        private set

    override fun answer(answer: String) {
        this.answer = answer
    }
}

data class OpenQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    override val isValidWhen: Question.() -> Boolean = { true }
) : Question {

    override val isValid: Boolean
        get() = isValidWhen()

    override var answer: String? = null
        private set

    override fun answer(answer: String) {
        this.answer = answer
    }
}

data class MultiSelectQuestion(
    override val id: String,
    override val title: String,
    override val isRelevant: () -> Boolean = { true },
    val options: Set<String>
) : Question {

    override val isValidWhen: Question.() -> Boolean = {
        options.containsAll(answer?.split(",") ?: emptyList())
    }

    override val isValid: Boolean
        get() = isValidWhen()

    private val _answer = mutableSetOf<String>()
    override val answer: String?
        get() = if (_answer.isEmpty()) null else _answer.joinToString(",")

    override fun answer(answer: String) {
        if (_answer.contains(answer)) {
            _answer.remove(answer)
        } else {
            _answer.add(answer)
        }
    }
}
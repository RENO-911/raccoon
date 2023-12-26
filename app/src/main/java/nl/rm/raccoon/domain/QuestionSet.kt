package nl.rm.raccoon.domain

interface QuestionSet : Set<Question> {
    val isValid: Boolean
        get() = all { it.isValid is ValidationResult.Valid }
}

class QuestionSetImpl(
    private val questions: Set<Question>
) : QuestionSet, Set<Question> by questions
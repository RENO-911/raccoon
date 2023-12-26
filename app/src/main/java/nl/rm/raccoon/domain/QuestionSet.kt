package nl.rm.raccoon.domain

import nl.rm.raccoon.domain.question.Question

interface QuestionSet : Set<Question> {
    val isValid: Boolean
        get() = all { it.isValid is ValidationResult.Valid }
}

class QuestionSetImpl(
    private val questions: Set<Question>
) : QuestionSet, Set<Question> by questions
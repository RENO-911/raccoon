package nl.rm.raccoon.domain

import nl.rm.raccoon.domain.question.Question

interface Survey {
    val questions: List<Question>
    val sets: List<QuestionSet>

    val isValid: Boolean
        get() = sets.all { it.isValid }

    fun answer(question: Question, answer: Answer) {
        question.answer(answer)
}
}

data class SurveyImpl(
    override val sets: List<QuestionSet>
) : Survey {
    override val questions: List<Question>
        get() = sets.flatten()

    override val isValid: Boolean
        get() = sets.all { it.isValid }
}
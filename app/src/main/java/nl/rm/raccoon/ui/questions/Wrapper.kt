package nl.rm.raccoon.ui.questions

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.question.Question

data class QuestionWrapper<T>(
    val question: T,
    val answer: Answer? = question.answer
) where T : Question

fun <T : Question> wrap(question: T): QuestionWrapper<T> = QuestionWrapper(question)
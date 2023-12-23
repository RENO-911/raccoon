package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.Question
import nl.rm.raccoon.domain.QuestionSet
import nl.rm.raccoon.domain.QuestionSetImpl

class QuestionSetBuilder() {
    private val _questions = mutableSetOf<Question>()
    val questions: Set<Question> = _questions

    fun addQuestion(question: Question) {
        _questions.add(question)
    }
}

fun SurveyBuilder.set(
    init: QuestionSetBuilder.() -> Unit
): QuestionSet {
   val builder = QuestionSetBuilder()
   with(builder) {
       init()

       val build = QuestionSetImpl(questions)
       this@set.addSet(build)
       return build
   }
}
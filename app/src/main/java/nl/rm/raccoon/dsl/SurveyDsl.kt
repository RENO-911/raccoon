package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.QuestionSet
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.domain.SurveyImpl

class SurveyBuilder() {
    private val _questions = mutableSetOf<QuestionSet>()
    val questions: Set<QuestionSet> = _questions
    lateinit var title: String

    fun addSet(question: QuestionSet) {
        _questions.add(question)
    }
}

fun Survey(
    init: SurveyBuilder.() -> Unit
): Survey {
   val builder = SurveyBuilder()
   with(builder) {
       init()

       return SurveyImpl(
           questions.toList()
       )
   }
}
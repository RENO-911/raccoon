package nl.rm.raccoon.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import nl.rm.raccoon.domain.MultiSelectQuestion
import nl.rm.raccoon.domain.MultipleChoiceQuestion
import nl.rm.raccoon.domain.OpenQuestion
import nl.rm.raccoon.domain.Question

interface QuestionFieldFactory {
    @Composable
    operator fun invoke(
        question: QuestionWrapper<*>
    ): @Composable Unit
}
@Suppress("UNCHECKED_CAST")
class QuestionFieldFactoryImpl(
    private val onAnswer: (Question, String) -> Unit
) : QuestionFieldFactory {
    @Composable
    override fun invoke(question: QuestionWrapper<*>) {
        if (!question.question.isRelevant()) {
            return
        }

        when (question.question) {
          is MultipleChoiceQuestion -> MultipleChoiceQuestionField(question as QuestionWrapper<MultipleChoiceQuestion>, onAnswer)
          is MultiSelectQuestion -> MultiSelectQuestionField(question as QuestionWrapper<MultiSelectQuestion>, onAnswer)
          is OpenQuestion ->   OpenQuestionField(question as QuestionWrapper<OpenQuestion>, onAnswer)
        }

        if (!question.question.isValid) {
            Text("Invalid answer")
        }
    }

}
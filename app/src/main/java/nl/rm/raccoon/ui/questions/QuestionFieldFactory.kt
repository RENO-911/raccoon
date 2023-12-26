package nl.rm.raccoon.ui.questions

import androidx.compose.runtime.Composable
import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.ValidationResult
import nl.rm.raccoon.domain.question.AddressQuestion
import nl.rm.raccoon.domain.question.MultiSelectQuestion
import nl.rm.raccoon.domain.question.MultipleChoiceQuestion
import nl.rm.raccoon.domain.question.OpenQuestion
import nl.rm.raccoon.domain.question.PhotoQuestion
import nl.rm.raccoon.domain.question.Question
import nl.rm.raccoon.ui.examples.InvalidAnswer
import nl.rm.raccoon.ui.examples.QuestionColumn

interface QuestionFieldFactory {
    @Composable
    operator fun invoke(
        question: QuestionWrapper<*>
    ): @Composable Unit
}
@Suppress("UNCHECKED_CAST")
class QuestionFieldFactoryImpl(
    private val onAnswer: (Question, Answer) -> Unit
) : QuestionFieldFactory {
    @Composable
    override fun invoke(question: QuestionWrapper<*>) {
        QuestionColumn(question = question) {

            if (!question.question.isRelevant()) {
                return@QuestionColumn
            }

            when (question.question) {
                is MultipleChoiceQuestion -> MultipleChoiceQuestionField(
                    question as QuestionWrapper<MultipleChoiceQuestion>,
                    onAnswer
                )

                is MultiSelectQuestion -> MultiSelectQuestionField(
                    question as QuestionWrapper<MultiSelectQuestion>,
                    onAnswer
                )

                is OpenQuestion -> OpenQuestionField(
                    question as QuestionWrapper<OpenQuestion>,
                    onAnswer
                )

                is PhotoQuestion -> PhotoQuestionField(
                    question as QuestionWrapper<PhotoQuestion>,
                    onAnswer
                )

                is AddressQuestion -> AddressQuestionField(

                )
            }

            val validationResult = question.question.isValid
            if (validationResult is ValidationResult.Invalid) {
                InvalidAnswer(validationResult.message)
            }
        }
    }
}
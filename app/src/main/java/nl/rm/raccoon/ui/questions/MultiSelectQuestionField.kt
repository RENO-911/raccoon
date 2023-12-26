package nl.rm.raccoon.ui.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import nl.rm.raccoon.domain.MultiSelectQuestion
import nl.rm.raccoon.ui.QuestionWrapper

@Composable
fun MultiSelectQuestionField(
    state: QuestionWrapper<MultiSelectQuestion>,
    onAnswer: OnAnswerLambda
) {
    Column(

    ) {
        Text(state.question.title)
        for (option in state.question.options) {
            Row {
                Checkbox(
                    checked = state.question.answer?.value?.contains(option.value) ?: false,
                    onCheckedChange = { onAnswer(state.question, option) }
                )
                Text(option.value)
            }
        }
    }
}
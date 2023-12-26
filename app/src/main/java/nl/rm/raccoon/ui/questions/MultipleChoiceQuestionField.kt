package nl.rm.raccoon.ui.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import nl.rm.raccoon.domain.question.MultipleChoiceQuestion

@Composable
fun MultipleChoiceQuestionField(
    state: QuestionWrapper<MultipleChoiceQuestion>,
    onAnswer: OnAnswerLambda
) {
    Column {
        Text(state.question.title)
        for (option in state.question.options) {
            Row {
                RadioButton(
                    selected = option == state.question.answer,
                    onClick = {
                        onAnswer(state.question, option)
                    })
                Text(option.value)
            }
        }
    }
}
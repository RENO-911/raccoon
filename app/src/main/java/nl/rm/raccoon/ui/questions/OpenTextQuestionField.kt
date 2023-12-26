package nl.rm.raccoon.ui.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.OpenQuestion
import nl.rm.raccoon.ui.QuestionWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenQuestionField(
    state: QuestionWrapper<OpenQuestion>,
    onAnswer: OnAnswerLambda
) {
    Column(
    ) {
        Text(state.question.title)
        TextField(
            value = state.question.answer?.value ?: "",
            onValueChange = {
                onAnswer(state.question, AnswerString(it))
            }
        )
    }
}
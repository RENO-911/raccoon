package nl.rm.raccoon.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.rm.raccoon.domain.MultiSelectQuestion
import nl.rm.raccoon.domain.MultipleChoiceQuestion
import nl.rm.raccoon.domain.OpenQuestion
import nl.rm.raccoon.domain.Question
import nl.rm.raccoon.domain.QuestionSet
import nl.rm.raccoon.domain.QuestionSetImpl
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.domain.SurveyImpl
import nl.rm.raccoon.dsl.exampleSurvey

@Composable
fun Survey() {
    val survey by remember { mutableStateOf(exampleSurvey()) }
    var surveyWrapper by remember { mutableStateOf(wrap(survey)) }

    val scrollState = rememberScrollState()

    fun answer(question: Question, answer: String) {
        survey.answer(question, answer)
        surveyWrapper = wrap(survey)
    }

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        for(section in surveyWrapper.sets) {
            QuestionSection(section, ::answer)
        }
    }
}

data class QuestionWrapper<T>(
    val question: T,
    val answer: String? = question.answer
) where T : Question

fun <T : Question> wrap(question: T): QuestionWrapper<T> = QuestionWrapper(question)

data class QuestionSetWrapper(
    val set: QuestionSet,
    val questions: List<QuestionWrapper<*>>
)

fun wrap(set: QuestionSet): QuestionSetWrapper = QuestionSetWrapper(
    set = set,
    questions = set.map(::wrap)
)

data class SurveyWrapper(
    val survey: Survey,
    val sets: List<QuestionSetWrapper>,
    val questions: List<QuestionWrapper<*>>
)

fun wrap(survey: Survey): SurveyWrapper {
    val wrapOnce = survey.sets.map(::wrap)
    return SurveyWrapper(
        survey = survey,
        sets = wrapOnce,
        questions = wrapOnce.flatMap { it.questions }
    )
}

@Composable
fun QuestionSection(
    set: QuestionSetWrapper,
    onAnswer: (Question, String) -> Unit) {
    Column {
        for(question in set.questions) {
           // QuestionField(question, onAnswer)
            // TODO
        }
    }
}

@Composable
fun MultipleChoiceQuestionField(
    state: QuestionWrapper<MultipleChoiceQuestion>,
    onAnswer: (Question, String) -> Unit) {
    Column {
        Text(state.question.title)
        for (option in state.question.options) {
            Row {
                RadioButton(
                    selected = option == state.question.answer,
                    onClick = {
                        onAnswer(state.question, option)
                    })
                Text(option)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenQuestionField(
    state: QuestionWrapper<OpenQuestion>,
    onAnswer: (Question, String) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(state.question.title)
        TextField(
            value = state.question.answer ?: "",
            onValueChange = {
                onAnswer(state.question, it)
            }
        )
    }
}

@Composable
fun MultiSelectQuestionField(
    state: QuestionWrapper<MultiSelectQuestion>,
    onAnswer: (Question, String) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(state.question.title)
        for (option in state.question.options) {
            Row {
                Checkbox(
                    checked = state.question.answer?.contains(option) ?: false,
                    onCheckedChange = { onAnswer(state.question, option) }
                )
                Text(option)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSurvey() {
    Survey()
}
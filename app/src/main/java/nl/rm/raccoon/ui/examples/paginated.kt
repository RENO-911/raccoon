package nl.rm.raccoon.ui.examples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import nl.rm.raccoon.domain.Question
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.dsl.exampleSurvey
import nl.rm.raccoon.ui.QuestionFieldFactory
import nl.rm.raccoon.ui.QuestionFieldFactoryImpl
import nl.rm.raccoon.ui.QuestionSetWrapper
import nl.rm.raccoon.ui.wrap


@Preview
@Composable
fun ExamplePaginatedSurvey() {
    PaginatedSurvey(exampleSurvey())
}

////////////////////////////////////////////////////
/// Section: Survey
////////////////////////////////////////////////////
@Composable
fun PaginatedSurvey(survey: Survey) {
    var wrappedSurvey by remember { mutableStateOf(wrap(survey)) }
    var currentPage by remember { mutableStateOf(0) }

    fun answer(question: Question, answer: String) {
        survey.answer(question, answer)
        wrappedSurvey = wrap(survey)
    }

    val questionFieldFactory = QuestionFieldFactoryImpl(::answer)

    fun back() {
        if (currentPage > 0) currentPage--
    }

    fun forward() {
        if (currentPage < (wrappedSurvey.sets.size - 1)) currentPage++
    }

    QuestionSetPage(
        questionFieldFactory = questionFieldFactory,
        set = wrappedSurvey.sets[currentPage],
        onAnswer = ::answer,
        onBack = ::back,
        onForward = ::forward
    )

}


////////////////////////////////////////////////////
/// Section: Sets
////////////////////////////////////////////////////
@Composable
fun QuestionSetPage(
    questionFieldFactory: QuestionFieldFactory,
    set: QuestionSetWrapper,
    onAnswer: (Question, String) -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            for (question in set.questions) {
                questionFieldFactory(question)
            }
        }
        QuestionSetPageNavigationBar(
            onBack,
            onForward
        )
    }
}

@Composable
fun QuestionSetPageNavigationBar(
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onBack
        ) {
            Text("Back")
        }
        Button(
            onClick = onForward
        ) {
            Text("Next")
        }
    }
}
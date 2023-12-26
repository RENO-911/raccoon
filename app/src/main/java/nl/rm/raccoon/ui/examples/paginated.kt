package nl.rm.raccoon.ui.examples

import android.graphics.ImageDecoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.rm.raccoon.R
import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.PhotoQuestion
import nl.rm.raccoon.domain.Question
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.dsl.exampleSurvey
import nl.rm.raccoon.dsl.singlePhotoQuestionSurvey
import nl.rm.raccoon.ui.OnAnswerLambda
import nl.rm.raccoon.ui.QuestionFieldFactory
import nl.rm.raccoon.ui.QuestionFieldFactoryImpl
import nl.rm.raccoon.ui.QuestionSetWrapper
import nl.rm.raccoon.ui.QuestionWrapper
import nl.rm.raccoon.ui.SurveyWrapper
import nl.rm.raccoon.ui.wrap
import java.io.File


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

    fun answer(question: Question, answer: Answer) {
        survey.answer(question, answer)
        wrappedSurvey = wrap(survey)
    }

    val questionFieldFactory = QuestionFieldFactoryImpl(::answer)

    fun back() {
        if (currentPage > 0) currentPage--
    }

    fun forward() {
        if (currentPage < (wrappedSurvey.sets.size)) currentPage++
    }

    if (currentPage == wrappedSurvey.sets.size) {
        SurveySummaryPage(
            survey = wrappedSurvey,
            onBack = ::back
        )
        return
    }
    QuestionSetPage(
        questionFieldFactory = questionFieldFactory,
        set = wrappedSurvey.sets[currentPage],
        onAnswer = ::answer,
        onBack = ::back,
        onForward = ::forward
    )

}

@Composable
fun SurveySummaryPage(survey: SurveyWrapper, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(

        ) {
            for (question in survey.questions.filter { it.question.isRelevant() }) {
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                Text(text = question.question.title, fontWeight = FontWeight.Bold)
                if (question.question is PhotoQuestion && question.answer != null) {
                    val bitmap = ImageDecoder.createSource(File(question.answer.value)).let {
                        ImageDecoder
                            .decodeBitmap(it)
                            .asImageBitmap()
                    }
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Selected photo"
                    )
                } else {
                    Text(text = question.answer?.value ?: "No answer")
                }
                if (question.answer?.properties?.isEmpty() == false) {
                    for (property in question.answer.properties) {
                        Text(text = "${property.key}: ${property.value}", style = TextStyle(fontStyle = FontStyle.Italic))
                    }
                }
            }

            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Divider()
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Text("Thank you for filling out this survey!")
        }

        Button(
            onClick = onBack
        ) {
            Text("Back")
        }
    }

}


////////////////////////////////////////////////////
/// Section: Sets
////////////////////////////////////////////////////
@Composable
fun QuestionSetPage(
    questionFieldFactory: QuestionFieldFactory,
    set: QuestionSetWrapper,
    onAnswer: OnAnswerLambda,
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
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp),
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

@Composable
fun QuestionColumn(
    question: QuestionWrapper<*>,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun InvalidAnswer() {
    Row {
       Text(
           text = "Invalid answer",
           color = Color.Red
       )
    }
}
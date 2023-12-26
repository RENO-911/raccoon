package nl.rm.raccoon.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import nl.rm.raccoon.client.compress
import nl.rm.raccoon.client.getUriForFile
import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.QuestionSet
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.domain.question.Question
import nl.rm.raccoon.dsl.exampleSurvey
import nl.rm.raccoon.ui.questions.OnAnswerLambda
import nl.rm.raccoon.ui.questions.QuestionWrapper
import nl.rm.raccoon.ui.questions.wrap
import java.io.File
import kotlin.io.path.createTempDirectory

@Composable
fun Survey() {
    val survey by remember { mutableStateOf(exampleSurvey()) }
    var surveyWrapper by remember { mutableStateOf(wrap(survey)) }

    val scrollState = rememberScrollState()

    fun answer(question: Question, answer: Answer) {
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
    onAnswer: OnAnswerLambda
) {
    Column {
        for(question in set.questions) {
           // QuestionField(question, onAnswer)
            // TODO
        }
    }
}


fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

@Preview
@Composable
fun PreviewSurvey() {
    Survey()
}
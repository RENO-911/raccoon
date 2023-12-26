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
import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.MultiSelectQuestion
import nl.rm.raccoon.domain.MultipleChoiceQuestion
import nl.rm.raccoon.domain.OpenQuestion
import nl.rm.raccoon.domain.PhotoQuestion
import nl.rm.raccoon.domain.Question
import nl.rm.raccoon.domain.QuestionSet
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.dsl.exampleSurvey
import java.io.File
import kotlin.io.path.createTempDirectory

internal typealias OnAnswerLambda = (Question, Answer) -> Unit

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

data class QuestionWrapper<T>(
    val question: T,
    val answer: Answer? = question.answer
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
    onAnswer: OnAnswerLambda
) {
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


sealed class PhotoQuestionFieldState() {
    object NoneTaken : PhotoQuestionFieldState()
    object Taking: PhotoQuestionFieldState()
    data class Preview(val file: File): PhotoQuestionFieldState()
}
@Composable
fun PhotoQuestionField(
    state: QuestionWrapper<PhotoQuestion>,
    onAnswer: OnAnswerLambda,
) {
    val context = LocalContext.current.findActivity()
    val cachedFileName = "${state.question.id}_upload.tmp"
    val uncompressedFolderName = "original"
    val compressedFolderName = "compressed"

    var viewState by remember { mutableStateOf<PhotoQuestionFieldState>(PhotoQuestionFieldState.NoneTaken) }

    val uncompressedFolder = createTempDirectory(uncompressedFolderName)
    val compressedFolder = createTempDirectory(compressedFolderName)
    val uncompressedUri = kotlin.io.path.createTempFile("uncompressed", null).toFile()
    var compressedUri = kotlin.io.path.createTempFile("compressed", null).toFile()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
        if (it) {
            try {
                compressedUri = compress(context, uncompressedUri, compressedUri, 50)
                viewState = PhotoQuestionFieldState.Preview(compressedUri)
            } catch (ex: Exception) {
                Log.e("PhotoQuestionField", "Failed to compress image", ex)
            }
        } else {
            Log.e("PhotoQuestionField", "Failed to take picture")
        }
    }

    Column(
    ) {
        Text(state.question.title)
        when(viewState) {
            PhotoQuestionFieldState.NoneTaken -> {
                Button(
                    onClick = {
                        launcher.launch(getUriForFile(context, uncompressedUri))
                    }
                ) {
                    Text("Take picture")
                }
            }
            PhotoQuestionFieldState.Taking -> {
                Text("Taking picture")
            }
            is PhotoQuestionFieldState.Preview -> {
                val bitmapSource = ImageDecoder.createSource((viewState as PhotoQuestionFieldState.Preview).file)
                    val factory = BitmapFactory.Options().apply {
                    inJustDecodeBounds = false
                    inPreferredConfig = Bitmap.Config.RGB_565
                    inSampleSize = 4
                }
                val bitmap = ImageDecoder.decodeBitmap(bitmapSource)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Photo"
                )
            }
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
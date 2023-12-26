package nl.rm.raccoon.ui.questions

import android.graphics.ImageDecoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import nl.rm.raccoon.client.compress
import nl.rm.raccoon.client.getUriForFile
import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.PhotoQuestion
import nl.rm.raccoon.ui.QuestionWrapper
import nl.rm.raccoon.ui.findActivity
import java.io.File


@Composable
fun PhotoQuestionField(
    state: QuestionWrapper<PhotoQuestion>,
    onAnswer: OnAnswerLambda,
) {
    val context = LocalContext.current.findActivity()

    val uncompressedUri = kotlin.io.path.createTempFile("uncompressed", null).toFile()
    var compressedUri = kotlin.io.path.createTempFile("compressed", null).toFile()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
        if (it) {
            try {
                compressedUri = compress(context, uncompressedUri, compressedUri, 50)
                onAnswer(state.question, AnswerString(compressedUri.absolutePath))
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
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                launcher.launch(getUriForFile(context, uncompressedUri))
            }
        ) {
            Text("Take picture")
        }

        if (state.answer != null) {
            val bitmap = ImageDecoder.createSource(File(state.answer.value)).let {
                ImageDecoder
                    .decodeBitmap(it)
                    .asImageBitmap()
            }
            Image(
                bitmap = bitmap,
                contentDescription = "Selected photo"
            )
        }
    }
}
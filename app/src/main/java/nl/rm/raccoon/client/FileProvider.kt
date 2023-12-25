package nl.rm.raccoon.client

import androidx.core.content.FileProvider
import nl.rm.raccoon.R


val FP_SURVEY_IMAGES_FOLDER = "survey_images"
val FP_AUTHORITY = "nl.rm.raccoon.fileprovider"

class MyFileProvider : FileProvider(R.xml.file_provider_paths)
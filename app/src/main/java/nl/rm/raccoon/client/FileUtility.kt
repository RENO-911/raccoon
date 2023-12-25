package nl.rm.raccoon.client

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val TAG = "FileUtility"

fun getUriForFile(context: Context, file: File): Uri {
    return FileProvider.getUriForFile(context, FP_AUTHORITY, file)
}

fun compress(
    context: Context,
    uncompressedFile: File,
    compressedFile: File,
    quality: Int): File {
    try {
        val factory = BitmapFactory.Options().apply {
            inJustDecodeBounds = false
            inPreferredConfig = Bitmap.Config.RGB_565
            inSampleSize = 4
        }
        val bitmap = BitmapFactory.decodeFile(uncompressedFile.path, factory)
        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        val bitmapdata = bos.toByteArray()
        val fos = FileOutputStream(compressedFile)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return compressedFile
    } catch (ex: Exception) {
        throw ex
    }
}
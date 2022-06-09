package ss.team16.nthulostfound.data.repository

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ss.team16.nthulostfound.data.source.ImgurApi
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository
import java.io.File
import java.io.FileOutputStream

class UploadImagesRepositoryImgurImpl(
    private val imgurApi: ImgurApi,
    private val context: Context
): UploadImagesRepository{
    override suspend fun uploadImage(
        uri: Uri,
        title: String?
    ): Result<UploadedImage> {
        return try {
            val file = copyStreamToFile(uri, context.contentResolver)

            val filePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                file.asRequestBody()
            )

            val response = imgurApi.uploadImage(
                filePart,
                name = title?.toRequestBody() ?: file.name.toRequestBody()
            )

            if (response.isSuccessful) {
                Log.d(TAG, "uploadImage: $response")
                val upload = response.body()!!.upload
                val uploadImage = UploadedImage(upload.link)
                Result.success(uploadImage)
            } else {
                Result.failure(Exception("Imgur 發生錯誤"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Creates a temporary file from a Uri, preparing it for upload.
     */
    private fun copyStreamToFile(uri: Uri, contentResolver: ContentResolver): File {
        val outputFile = File.createTempFile("upload-image-temp", null)

        contentResolver.openInputStream(uri)?.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
        return outputFile
    }
}
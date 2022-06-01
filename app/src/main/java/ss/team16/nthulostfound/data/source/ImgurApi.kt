package ss.team16.nthulostfound.data.source

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ss.team16.nthulostfound.data.model.ImgurUploadResponse

interface ImgurApi {
    /**
     * An anonymous image upload endpoint:
     * https://apidocs.imgur.com/?version=latest#c85c9dfc-7487-4de2-9ecd-66f727cf3139
     */
    @Multipart
    @Headers("Authorization: Client-ID $CLIENT_ID")
    @POST("image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody? = null
    ): Response<ImgurUploadResponse>

    companion object {
        const val baseUrl = "https://api.imgur.com/3/"
        const val CLIENT_ID = "340a2bf5f554be8"
    }
}

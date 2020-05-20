package woogear.kwon.gccproject.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import woogear.kwon.gccproject.model.GccResponse

interface GccService {
    @GET("/App/json/{json}")
    fun getPlaces(
        @Path("json") path: String
    ): Call<GccResponse>
}
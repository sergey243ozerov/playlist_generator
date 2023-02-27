package domain

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import model.Response
import model.Track

class GeneratorRepository(private val client: HttpClient) {

    private companion object{
        const val PATH = "/generate_playlist?duration=%s"
    }
    suspend fun getTracks(duration: Int): List<Track>{
        return client.get(PATH.format(duration)).body<Response>().result
    }
}
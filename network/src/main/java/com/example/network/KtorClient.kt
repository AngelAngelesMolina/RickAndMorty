package com.example.network

import com.example.network.models.domain.Character
import com.example.network.models.domain.CharacterPage
import com.example.network.models.domain.Episode
import com.example.network.models.remote.RemoteCharacter
import com.example.network.models.remote.RemoteCharacterPage
import com.example.network.models.remote.RemoteEpisode
import com.example.network.models.remote.toDomainCharacter
import com.example.network.models.remote.toDomainCharacterPage
import com.example.network.models.remote.toDomainEpisode
import com.example.network.utils.ApiOperation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url("https://rickandmortyapi.com/api/") }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

    }

    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("character/$id").body<RemoteCharacter>().toDomainCharacter().also {
                characterCache[id] = it
            }
        }
    }

    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return safeApiCall {
            client.get("episode/$episodeId").body<RemoteEpisode>().toDomainEpisode()
        }
    }

    suspend fun getEpisodes(episodesIds: List<Int>): ApiOperation<List<Episode>> {
        return if (episodesIds.size == 1) {
            getEpisode(episodesIds[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = episodesIds.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated").body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }
    suspend fun getCharacterByPage(
        pageNumber: Int
    ): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>().toDomainCharacterPage()
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(e)
        }
    }
}


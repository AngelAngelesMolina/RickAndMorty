package com.example.rickandmorty.repositories

import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.network.models.domain.CharacterPage
import com.example.network.utils.ApiOperation
import javax.inject.Inject


class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> =
        ktorClient.getCharacter(characterId)

    suspend fun fetchCharacterPage(
        page: Int,
//        params: Map<String, String> = emptyMap()
    ): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(pageNumber = page)
    }
}
package com.example.rickandmorty.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.rickandmorty.components.character.CharacterDetailsNamePlateComponent
import com.example.rickandmorty.components.common.DataPoint
import com.example.rickandmorty.components.common.DataPointComponent
import com.example.rickandmorty.components.common.LoadingState
import com.example.rickandmorty.ui.theme.RickAction
import kotlinx.coroutines.delay

@Composable
fun CharacterDetailsScreen(
    ktorClient: KtorClient,
    characterId: Int,
) {
    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints: List<DataPoint> by remember {
        derivedStateOf {
            buildList {
                character?.let {
                    add(DataPoint("Last know location", character!!.location.name))
                    add(DataPoint("Species", character!!.species))
                    add(DataPoint("Gender", character!!.gender.displayName))
                    character!!.type.takeIf { it.isNotEmpty() }
                        ?.let { type -> add(DataPoint("Type", type)) }
                    add(DataPoint("Origin", character!!.origin.name))
                    add(DataPoint("Episode count", character!!.episodeIds.size.toString()))

                }
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        delay(500)
        character = ktorClient.getCharacter(characterId)
    })

    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(all = 16.dp)) {
        if (character == null) {
            item {
                LoadingState()
                return@item
            }
        }
        item {
            character?.let {
                CharacterDetailsNamePlateComponent(
                    name = character!!.name,
                    status = character!!.status
                )
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            character?.let {
                AsyncImage(
                    model = character!!.imageUrl ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                )
            }
        }
        //data points
        items(characterDataPoints) {
            Spacer(modifier = Modifier.height(32.dp))
            DataPointComponent(dataPoint = it)
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        //Button
        item {
            Text(
                text = "View all the episodes",
                color = RickAction,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .border(width = 1.dp, color = RickAction, shape = RoundedCornerShape(12.dp))
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        // TODO:
                    }
                    .padding(vertical = 8.dp)
            )
        }


    }
}
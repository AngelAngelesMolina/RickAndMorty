package com.example.rickandmorty.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.network.models.domain.Episode
import com.example.rickandmorty.components.common.CharacterImage
import com.example.rickandmorty.components.common.CharacterNameComponent
import com.example.rickandmorty.components.common.DataPoint
import com.example.rickandmorty.components.common.DataPointComponent
import com.example.rickandmorty.components.common.LoadingState
import com.example.rickandmorty.components.common.SimpleToolbar
import com.example.rickandmorty.components.episode.EpisodeRowComponent
import com.example.rickandmorty.ui.theme.RickPrimary
import com.example.rickandmorty.ui.theme.RickTextPrimary
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeScreen(characterId: Int, ktorClient: KtorClient, onBackClicked: () -> Unit) {

    var characterState by remember {
        mutableStateOf<Character?>(null)
    }
    var episodesState by remember {
        mutableStateOf<List<Episode>>(emptyList())
    }

    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId).onSuccess { character ->
            characterState = character
            launch {
                ktorClient.getEpisodes(character.episodeIds).onSuccess { episodes ->
                    episodesState = episodes
                }.onFailure {

                }
            }
        }.onFailure { exc ->
            // TODO: handling exception
        }
    })

    characterState?.let { character ->
        MainScreen(character = character, episodes = episodesState, onBackClicked)
    } ?: LoadingState()

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(character: Character, episodes: List<Episode>, onBackClicked: () -> Unit) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        SimpleToolbar("Episodes", onBackAction = onBackClicked)
        LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item { CharacterNameComponent(name = character.name) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { 
                LazyRow {
                    episodeBySeasonMap.forEach { entry ->
                        val title = "Temp ${entry.key}"
                        val desc = "${entry.value.size} ep"
                        item {
                            DataPointComponent(dataPoint = DataPoint(title, desc))
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }
            item { CharacterImage(imageUrl = character.imageUrl) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            episodes.groupBy { it.seasonNumber }.forEach { mapEntry ->
                stickyHeader {
                    Spacer(modifier = Modifier.height(40.dp))
                    SeasonHeader(seasonNumber = mapEntry.key)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(mapEntry.value) { ep ->
                    EpisodeRowComponent(episode = ep)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}

@Composable
fun SeasonHeader(seasonNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = RickPrimary)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {

        Text(
            text = "Temporada $seasonNumber",
            color = RickTextPrimary,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = RickTextPrimary, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 4.dp)
        )
    }


}

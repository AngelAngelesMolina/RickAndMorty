package com.example.rickandmorty.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.rickandmorty.components.character.CharacterDetailsNamePlateComponent
import com.example.rickandmorty.components.common.DataPoint
import com.example.rickandmorty.components.common.DataPointComponent
import com.example.rickandmorty.components.common.LoadingState
import com.example.rickandmorty.ui.theme.RickAction
import com.example.rickandmorty.viewmodels.CharacterDetailsViewModel


sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ) : CharacterDetailsViewState
}

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onEpisodeClick: (Int) -> Unit
) {


    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })
    val state by viewModel.stateFlow.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(all = 16.dp)) {
        when(val viewState = state){
            is CharacterDetailsViewState.Error -> {
                item {
                    LoadingState()
                    return@item
                }
            }
            CharacterDetailsViewState.Loading -> {
                // TODO:
            }
            is CharacterDetailsViewState.Success -> {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    viewState.character.let {
                        CharacterDetailsNamePlateComponent(
                            name = viewState.character.name,
                            status = viewState.character.status
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    AsyncImage(
                        model = viewState.character.imageUrl ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                    )
                }
                //data points
                items(viewState.characterDataPoints) {
                    Spacer(modifier = Modifier.height(32.dp))
                    DataPointComponent(dataPoint = it)
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
                //Button
                item {
                    Text(
                        text = "Ver todos los episodios",
                        color = RickAction,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .border(width = 1.dp, color = RickAction, shape = RoundedCornerShape(12.dp))
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onEpisodeClick(characterId)
                            }
                            .padding(vertical = 8.dp)
                    )
                }

            }
        }


    }
}
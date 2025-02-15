package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.network.KtorClient
import com.example.rickandmorty.screens.CharacterDetailsScreen
import com.example.rickandmorty.screens.CharacterEpisodeScreen
import com.example.rickandmorty.screens.HomeScreen
import com.example.rickandmorty.ui.theme.RickPrimary
import com.example.rickandmorty.ui.theme.SimpleRickTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var ktorClient: KtorClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SimpleRickTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = RickPrimary) {
                    NavHost(navController = navController, startDestination = "home_screen") {
                        composable(route = "home_screen") {
                            HomeScreen(onCharacterSelected = { charId ->
                                navController.navigate("character_details/$charId")
                            })
                        }
                        composable(
                            "character_details/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) { entry ->
                            val characterId = entry.arguments?.getInt("characterId") ?: 0
                            CharacterDetailsScreen(
                                characterId,

                                onEpisodeClick = { navController.navigate("character_episodes/$it") },
                                onBackClic = { navController.navigateUp() }
                            )
                        }
                        composable(
                            route = "character_episodes/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) { backstackEntry ->
                            val characterId = backstackEntry.arguments?.getInt("characterId") ?: 0
                            CharacterEpisodeScreen(
                                ktorClient = ktorClient,
                                characterId = characterId,
                                onBackClicked = {navController.navigateUp()}
                            )
                        }
                    }
                }
            }
        }
    }
}


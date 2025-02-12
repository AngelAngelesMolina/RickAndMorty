package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.network.KtorClient
import com.example.rickandmorty.screens.CharacterDetailsScreen
import com.example.rickandmorty.ui.theme.RickPrimary
import com.example.rickandmorty.ui.theme.SimpleRickTheme

class MainActivity : ComponentActivity() {
    private val ktorClient = KtorClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            var character by remember {
//                mutableStateOf<Character?>(null)
//            }
//            val scope = rememberCoroutineScope()
//            LaunchedEffect(key1 = Unit, block = {
//                character = ktorClient.getCharacter(2)
//            })
          /*  SimpleRickTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {

                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
//                        Text(text = character?.name ?: "No caracter")
                    }
                }
            }*/
            SimpleRickTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = RickPrimary) {
                    CharacterDetailsScreen(ktorClient = ktorClient, characterId = 2)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

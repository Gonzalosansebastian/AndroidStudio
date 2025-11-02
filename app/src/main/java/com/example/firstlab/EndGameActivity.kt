package com.example.firstlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstlab.ui.theme.FirstLabTheme

class EndGameActivity : ComponentActivity() {

    companion object {
        const val USER_NAME_KEY = "user_name"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Recuperar los valores enviados a través del Intent
        val score = intent.getIntExtra(MainActivity.SCORE_KEY, 0)
        val level = intent.getIntExtra(MainActivity.LEVEL_KEY, 0)
        val reachedLevel10 = intent.getBooleanExtra(MainActivity.REACHED_LEVEL_10_KEY, false)
        val userName = intent.getStringExtra("nombre_usuario") ?: "Jugador"

        // Establecer el contenido usando Jetpack Compose
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_activity_end_game)) }) }
                ) { innerPadding ->
                    // Mostrar los valores recibidos en una columna
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (reachedLevel10) {
                                stringResource(R.string.congratulations_message)
                            } else {
                                stringResource(R.string.game_ended_message)
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = stringResource(R.string.game_over), style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "${stringResource(R.string.score_label)}: $score",
                            style = MaterialTheme.typography.headlineMedium)
                        Text("${stringResource(R.string.level_label)}: $level",
                            style = MaterialTheme.typography.headlineMedium)

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gato),

                                contentDescription = stringResource(R.string.launcher_title),
                                modifier = Modifier.size(120.dp)
                            )
                            Button(
                                onClick = {
                                    // Acción al presionar el botón
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            "Puntuacion del jugador $userName"
                                        )
                                        putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            "El jugador $userName ha obtenido la puntación de $score puntos y ha alcanzado el nivel $level"
                                        )
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Compartir puntuación")
                                    startActivity(shareIntent)
                                }
                            ) {
                                Text(stringResource(R.string.send_data_button))
                            }
                        }
                    }
                }
            }
        }
    }
}

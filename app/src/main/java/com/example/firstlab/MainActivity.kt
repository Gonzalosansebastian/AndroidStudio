package com.example.firstlab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstlab.ui.theme.FirstLabTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val SCORE_KEY = "score"
        const val LEVEL_KEY = "level"
    }

    private lateinit var userName : String
    private var score : Int = 0
    private var level : Int = 0


    private val incrementScoreAndLevel : (Int) -> Map<String,Int> = { inc ->

        val randomIncrement = if (level > 0 ){
            (1..level).random()

        } else {
            1
        }

        score += randomIncrement
        level = score / 10
        //mapOf(Pair("score", score), Pair("level", level))
        mapOf(SCORE_KEY to score, LEVEL_KEY to level)
    }

    private val decrementScoreandLevel: (Int) -> Map<String,Int> = { dec ->
        val decrementValue = level * 2

        score = maxOf(0,score - decrementValue)
        level = score / 10
        mapOf(SCORE_KEY to score, LEVEL_KEY to level)
    }

    private val goToEndGameActivity : () -> Unit =  {
        val intent = Intent(this, EndGameActivity::class.java)
        intent.putExtra(SCORE_KEY, score)
        intent.putExtra(LEVEL_KEY, level)
        startActivity(intent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = intent.getStringExtra("nombre_usuario") ?: "Jugador"

        savedInstanceState?.let { instance ->
            score = instance.getInt(SCORE_KEY,0)
            level = instance.getInt(LEVEL_KEY,0)
        }

        enableEdgeToEdge()
        setContent {
            FirstLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CenterAlignedTopAppBar( title = { Text(text = "Super Game Counter") } ) }
                ) { innerPadding ->
                    GameStateDisplay(name = userName, score, level, Modifier.padding(innerPadding), incrementScoreAndLevel, goToEndGameActivity,
                        onDecButtonClick = decrementScoreandLevel)
                }
            }
        }
    }

    // Método llamado cuando la actividad está a punto de hacerse visible
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: La actividad está a punto de ser visible.")
    }

    // Método llamado cuando la actividad se ha hecho visible
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: La actividad está en primer plano y se puede interactuar con ella.")
    }

    // Método llamado cuando otra actividad toma el control, poniendo esta actividad en segundo plano
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: La actividad está en segundo plano.")
    }

    // Método llamado cuando la actividad ya no es visible para el usuario
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: La actividad ya no es visible.")
    }

    // Método llamado justo antes de que la actividad sea destruida
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: La actividad ha sido destruida.")
    }

    // Método llamado antes de que la actividad pase a segundo plano y guarde el estado
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(LEVEL_KEY, level)
        Log.d(TAG, "onSaveInstanceState: Guardando el estado de la actividad.")
    }

    // Método llamado cuando se restaura el estado previamente guardado
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: Restaurando el estado de la actividad.")
    }
}

@Composable
fun GameStateDisplay(
    name: String,
    initScore: Int,
    initLevel: Int,
    modifier: Modifier = Modifier,
    onIncButtonClick : (Int) -> Map<String,Int>,
    onEndGameButtonClick : () -> Unit,
    onDecButtonClick: (Int) -> Map<String, Int>
) {
    var score by remember { mutableIntStateOf(initScore) }
    var level by remember { mutableIntStateOf(initLevel) }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Primera fila con un elemento
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Greeting(name)
        }

        // Segunda fila con dos columnas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Primera columna con dos elementos y separador
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Red)
                    .padding(8.dp)
            ) {
                ShowVariables("Score", score)
                Spacer(Modifier.height(8.dp))
                ShowVariables("Level", level)
            }

            Spacer(Modifier.width(8.dp))

            // Segunda columna con un elemento
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardButton("Increase Score") {
                    val result = onIncButtonClick(1)
                    score = result[MainActivity.SCORE_KEY]!!
                    level = result[MainActivity.LEVEL_KEY]!!

                }
                Spacer(Modifier.height(8.dp))

                // NUEVO: Botón para decrementar
                StandardButton(
                    label = "Decrease Score",
                    onClick = {
                        val result = onDecButtonClick(1)
                        score = result[MainActivity.SCORE_KEY]!!
                        level = result[MainActivity.LEVEL_KEY]!!
                    }
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            StandardButton("End Game") {
                // Acción al hacer clic: abrir EndGameActivity
                onEndGameButtonClick()
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

@Composable
fun ShowVariables(
    label: String,
    value : Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$label -> $value",
        modifier = modifier // Se pasa el modificador para ajustar la apariencia o el layout
    )
}

@Composable
fun StandardButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    // Composable que muestra un botón estándar con un texto
    Button(onClick = onClick) {
        Text(
            text = label,
            modifier = modifier // Se pasa el modificador para ajustar la apariencia o el layout
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstLabTheme {
        Greeting("Android")
    }
}
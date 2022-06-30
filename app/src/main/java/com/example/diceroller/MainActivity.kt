package com.example.diceroller

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.diceroller.ui.theme.DiceRollerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import java.lang.Math.random

class MainActivity : ComponentActivity() {

    // Easier method to do it using Jetpack Compose

    // 1. Declare a mutablestableof string
    // 2. update the string with value in the text field
    // 3. get the value the dice has been rolled to
    // 4. convert the mutlablestateof string to int
    // 5. check if dice rolled value and the int value are similar
    // 6. If similar Toast

    val rolled = MutableStateFlow("")
    val prediction = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            rolled.collectLatest {
                if (it.isBlank()) return@collectLatest

                val value = prediction.value
                if (value.isBlank()) return@collectLatest

                Toast.makeText(
                    this@MainActivity,
                    if (it.equals(value, true))
                        "Congrats" else "Sorry", Toast.LENGTH_SHORT
                ).show()

            }
        }

        setContent {
            DiceRollerTheme {
                DiceRollerApp(
                    rolled = { rolled.value = it.toString() },
                    prediction = { prediction.value = it }
                )
            }
        }
    }
}

@Composable
fun TextField(click: (text: String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp, 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(value = text, onValueChange = {
            text = it
            click.invoke(it)
        })
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
//Will represent UI components
fun DiceWithButtonAndImage(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center),
    rolled: (value: Int) -> Unit
) {
    var result by remember { mutableStateOf(1) }
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(imageResource), contentDescription = result.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            result = (1..6).random()
            rolled.invoke(result)
        })
        // Button is clickable and gives a random number
        {
            Text(stringResource(R.string.roll))
        }
    }

}

//@Preview(showBackground = true)
@Composable
fun DiceRollerApp(rolled: (value: Int) -> Unit, prediction: (value: String) -> Unit) {
    DiceWithButtonAndImage() { rolled.invoke(it) }
    TextField() { prediction.invoke(it) }
}

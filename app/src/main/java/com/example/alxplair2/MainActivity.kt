package com.example.alxplair2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.compose.ui.text.android.style.LineHeightSpan
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alxplair2.ui.theme.AlxPLair2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlxPLair2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(Modifier.height(50.dp).fillMaxWidth().padding(50.dp)) {
                        Greeting("Android")
                        Button(onClick = { /*TODO*/ }) {

                        }

                        
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(InternalPlatformTextApi::class)
@Composable
fun Button1() {
    Button(onClick = { /*TODO*/ },shape = CutCornerShape(10)) {
        Text(text = "click")



    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AlxPLair2Theme {
        Greeting("Android")
    }
}
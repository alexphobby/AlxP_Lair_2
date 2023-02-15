package com.example.alxplair2

//import androidx.compose.foundation.pager.rememberPagerState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alexplair.MqttUtilities
import com.example.alexplair.MyCallBack
import com.example.alexplair.MyHome
import com.example.alxplair2.ui.theme.AlxPLair2Theme

class MainActivity : ComponentActivity() {
    var mqtt = MqttUtilities
    var my = MyCallBack
    var myhome = MyHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mqtt.Connect()
        setContent {
            AlxPLair2Theme {
                app()
                //val pagerState = remember { mutableStateOf(1) }
                //TopAppBarSample()
                //loadMainPage()

                //tabsWithSwiping()
//tabsWithSwiping()
            }
        }
    }


    @Composable
    fun PageContent() {
        val clicked = remember { mutableStateOf(0) }
        val selectedTab = remember { mutableStateOf(0) }
        val selectedColor = remember { mutableStateOf(Color.Black) }



        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
                .padding(all = 0.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxSize()
                    .background(Color.Black),
            ) {
                TabRow(
                    selectedTabIndex = selectedTab.value,
                    backgroundColor = Color.DarkGray,
                    modifier = Modifier
                        .height(30.dp),
                        //.clip(RoundedCornerShape(10.dp))
                    contentColor = Color.Gray

                ) {


                    Tab(
                        selected = selectedTab.value == 0,
                        onClick = {
                            clicked.value = 1
                            selectedTab.value = 0

                        },
                    ) {
                        Text(
                            text = "Button 1",
                            color = Color.White
                        )

                    }
                    Tab(
                        selected = selectedTab.value == 1,
                        onClick = {
                            clicked.value = 2
                            selectedTab.value = 1

                        },//, modifier = Modifier.absolutePadding(left = 30.dp)
                        modifier = Modifier.background(Color.DarkGray)
                    ) {
                        Text(
                            //modifier = Modifier.background(Color.White),
                            text = "Button 2",
                            color = Color.White
                        )

                    }

                    Tab(
                        selected = selectedTab.value == 2,
                        onClick = {
                            clicked.value = 3
                            selectedTab.value = 2

                        },//, modifier = Modifier.absolutePadding(left = 30.dp)
                        modifier = Modifier.background(Color.DarkGray)
                    ) {
                        Text(
                            //modifier = Modifier.background(Color.White),
                            text = "Button 2",
                            color = Color.White
                        )

                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    //Image(painter= painterResource(id = androidx.core.R.drawable.notification_template_icon_bg),"test")
                    Column() {
                        //Text(modifier = Modifier.background(Color.Gray), text = "Text 1")
                        //Text(modifier = Modifier.background(Color.LightGray), text = "Text 2")
                        when (selectedTab.value) {

                            0 -> show1()
                            1 -> show2()
                            2 -> show3()

                        }
                    }

                }

            }
        }

    }


    @Composable
    @Preview
    private fun app() {
        var gradientColor1 = Color.Black
        var gradientColor2 = Color.DarkGray
        var gradient = arrayOf(0.3f to gradientColor1, 1.0f to gradientColor2)

        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "AlxP's Lair", color = Color.White)
                    },
                    backgroundColor = Color.Transparent,
                    modifier = Modifier
                        .background(brush = Brush.horizontalGradient(colorStops = gradient))
                        .height(50.dp),
                    elevation = 0.dp,
                )
            }

        ) {


            Box(
                modifier = Modifier
                //.padding(top = 50.dp)
            ) {
                PageContent()
            }
        }

    }
}



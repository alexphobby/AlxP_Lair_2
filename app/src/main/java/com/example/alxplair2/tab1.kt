package com.example.alxplair2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.alexplair.MqttUtilities
import com.example.alexplair.MyCallBack

@Composable
fun show1() {
    var cardSize = 300.dp

    var checkedState by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(0f) }
    var sliderPositionMemory by remember { mutableStateOf(0f) }
    var memoryIsVisible = false //by remember { mutableStateOf(false) }

    var mqtt = MqttUtilities
    var myCallBack = MyCallBack

    var isChanged = myCallBack.arrived
    Card(
        modifier = Modifier
            .size(cardSize)
            .padding(start = 30.dp),
        //    backgroundColor = Color.DarkGray
    )

    {


        if (isChanged) {
            //Thread.sleep(1000)
            //myCallBack.arrived = false
            Column() {
            }

            Text("Temp: ${myCallBack.myhome.temperature.toString()}")

            Text("Humidity: ${myCallBack.myhome.humidity.toString()} %")
            Text("Light: ${myCallBack.myhome.light.toString()} lux")

        }

//        Image(painter = painterResource(id = R.drawable.munte_cu_copiii),
//            contentDescription = "test munte",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.clip(RoundedCornerShape(20.dp))
//        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Column() {

                Row() {
                    Text(
                        text = "Switch lights",
                        modifier = Modifier.padding(14.dp),
                        color = Color.White
                    )
                    Switch(checked = sliderPosition > 0,
                        modifier = Modifier
                        //    .padding(10.dp)
                        ,
                        onCheckedChange = {
                            Log.e("LOG", "mqtt ${it.toString()}")

                            if (!it) {
                                sliderPosition = 0f
                                Log.e("LOG", "mqtt slider to 0 ${sliderPosition.toString()}")
                            } else {
                                if (sliderPositionMemory > 0) {
                                    sliderPosition = sliderPositionMemory
                                } else {
                                    sliderPosition = 0.5f
                                }
                                Log.e("LOG", "mqtt slider to ${sliderPosition.toString()}")
                            }
                            checkedState = it
                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState
                            mqtt.Connect()
                            mqtt.publishMessage(
                                "lights:${(sliderPosition * 100).toInt()}",
                                "toCameraMica"
                            )


                        }

                        //.scale(1.3f)

                    )
                    if (memoryIsVisible) {

                        Text(
                            text = "M: ${(sliderPositionMemory * 100).toInt()}",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp, end = 10.dp),
                            color = Color.White
                        )
                    }


                }
                Row() {


                    Slider(value = sliderPosition,
                        onValueChange = {
                            Log.e("LOG", "mqtt ValueChanged ${sliderPosition.toString()}")

                            sliderPosition = it;
                            if (it > 0) {
                                sliderPositionMemory = it

                            }
                            mqtt.Connect()
                            mqtt.publishMessage(
                                "lights:${(sliderPosition * 100).toInt()}",
                                "toCameraMica"
                            )
                        },
                        onValueChangeFinished = {
                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState
                            Log.e("LOG", "mqtt ValueChangedFinished ${sliderPosition.toString()}")
                        }
                    )

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(0.8f to Color.Transparent, 1f to Color.Black),
                            startY = 0.3f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .offset(0.dp, cardSize - 20.dp)

            ) {
//                Text(text = "111111111", color = Color.LightGray, fontSize = 20.sp)



            }
        }
    }

    mqtt.requestTemperature()


}
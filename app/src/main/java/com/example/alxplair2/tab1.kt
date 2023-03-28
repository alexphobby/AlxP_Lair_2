package com.example.alxplair2

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import kotlin.random.Random
fun getCards() {
    return
}
@Composable
fun Tab1(vm: MainViewModel) {

    var scrollMemory by rememberSaveable { mutableStateOf(0f) }
    val testMutable by vm.roomMutable.observeAsState(0)
    //val roomsMutable by rememberSaveable { mutableStateListOf(Room)}

    //val rl =
    //var roomListMutable = remember { mutableStateListOf<Room>() }
    //roomListMutable = rl
    //val home by mainViewModel.mqttData.collectAsState()

//    val tmp by mainViewModel.temperatureData.collectAsState()
    //var roomList = mainViewModel.roomList

    //Column(modifier = Modifier.verticalScroll(state = ScrollState(scrollMemory.toInt()))) {
    //   CardBirou("a36_cam_medie",mainViewModel)
    //  CardBirou("a36_cam_mica",mainViewModel)
    //CardBirou("a36_cam_mica",mainViewModel)
    //Text(vm.roomsMutable?.count().toString())

    if(vm.roomsMutable == null)
        return



        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(vm.roomsMutable!!) {index,item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                Text(
//                    "\uD83C\uDF3F  Plants in Cosmetics",
//                    style = MaterialTheme.typography.bodyMedium, color = Color.White

                    //)
                    //Log.e("Compose","mqtt ${item .temperature}")
                    //Text(item.temperature.toString(), color = Color.Black)
                    CardBirou(
                        cardRoomName = item.roomName,
                        ambientLight = item.ambientLight,
                        dimPercent = item.dimPercent,
                        temperature = item.temperature,
                        humidity = item.humidity,
                        mainViewModel = vm
                    )
                    //Text(item.temperature.toString(), color = Color.White)
                    //CardBirou(item.value.
                    //   .roomName,item.ambientLight,item.dimPercent,item.temperature,item.humidity,mainViewModel)
                }
            }

        }
    }








// mqtt.requestTemperature()




@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardBirou(cardRoomName:String,
              ambientLight:Double,
              dimPercent:Int,
              temperature:Double,
              humidity:Double,
              mainViewModel: MainViewModel) {

    /*val roomName by mainViewModel.roomName.collectAsState()

    val ambientLight by mainViewModel.ambientLight.collectAsState()

    val dimPercent by mainViewModel.dimPercent.collectAsState()
    val dimPercentSlider by mainViewModel.dimPercentSlider.collectAsState()

    val temperature by mainViewModel.temperature.collectAsState()
    val humidity by mainViewModel.humidity.collectAsState()
*/
    val cardHeight = 0
    var cardSize = 300.dp

    var checkedState by rememberSaveable { mutableStateOf(false) }
    var memoryIsVisible = false //by remember { mutableStateOf(false) }

    //var dimPercentSliderChanged by rememberSaveable { mutableStateOf(false) }

    //var dimSliderValue = 0

    val cardModifier = Modifier
        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        .fillMaxWidth()
        .height(260.dp)

    val boxModifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)

    Log.e(ContentValues.TAG, "mqtt composinng: RN: $cardRoomName")
    Card(
        modifier = cardModifier
            .onSizeChanged {

                cardSize = cardHeight.dp + 100.dp

            }
    )

    {
        var sliderPosition by rememberSaveable { mutableStateOf(0f) } //dimPercent.toFloat()/100) }
        var sliderPositionMemory by rememberSaveable { mutableStateOf(0f) }

        //sliderPosition = dimPercent.toFloat()/100


        Log.e(
            "LOG",
            "mqtt recompose tab "
        )

//


//        Image(painter = painterResource(id = R.drawable.munte_cu_copiii),
//            contentDescription = "test munte",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.clip(RoundedCornerShape(20.dp))
//        )
        Box(
            modifier = boxModifier
        ) {
            Column() {
                Row() {
                    Text(
                        text=cardRoomName,
                        modifier = Modifier.padding(all = 10.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                var context = LocalContext.current


                Row() {
                    Text(
                        text = "Switch lights",
                        modifier = Modifier.padding(14.dp),
                        color = Color.White
                    )
                    Switch(checked = dimPercent > 0,
                        modifier = Modifier
                        //    .padding(10.dp)
                        ,
                        onCheckedChange = {
                            Log.e("LOG", "mqtt ${it.toString()}")

                            if (!it) {

                                mainViewModel.updateDimPercentToRoomName(cardRoomName,0)
                                Log.e("LOG", "mqtt Switch off")

//                                sliderPosition = 0f
//                                Log.e("LOG", "mqtt slider to 0 ${sliderPosition.toString()}")
                                //mainViewModel.mqtt.setLightsPercent((sliderPosition*100).toInt())
                            } else {
                                Log.e("LOG", "mqtt Switch on")

                                if (sliderPositionMemory > 0) {
                                    sliderPosition = sliderPositionMemory
                                    Log.e("LOG", "mqtt Switch memory")
                                } else {
                                    sliderPosition = 0.5f
                                }
                                mainViewModel.updateDimPercentToRoomName(cardRoomName,(sliderPosition * 100).roundToInt())
                                //Log.e("LOG", "mqtt Switch Move slider to: ${dimPercent.toString()}")

                                //Log.e("LOG", "mqtt slider to ${sliderPosition.toString()}")
                            }
                            //mainViewModel.mqtt.setLightsPercent(dimPercent)
                            checkedState = it
                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState


                        }

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


                    //Text(sliderPosition.toString(),modifier=Modifier.padding(start = (sliderPosition *100).dp))

                    Slider(
                        value = dimPercent.toFloat() / 100, //sliderPosition,
                        onValueChange = {
                            //mainViewModel.mqtt.setLightsPercent(sliderPosition.toInt())
                            //Log.e("LOG", "mqtt ValueChanged ${(sliderPosition*100).toString()}")
                            //sliderPosition = it
                            mainViewModel.updateDimPercentToRoomName(cardRoomName,(it * 100).roundToInt())
                            //mainViewModel.initState = true
                            //mainViewModel.updateDimPercentSlider(it);
                            if (it > 0) {
                                sliderPositionMemory = it

                            }
                            Log.e(
                                "LOG",
                                "mqtt ValueChangedFinished ${((it * 100).roundToInt()).toString()}"
                            )
                            mainViewModel.mqtt.setLightsPercent(mainViewModel.getDeviceNameFromRoomName(cardRoomName),(it * 100).roundToInt())

                        },
                        onValueChangeFinished = {
                            //dimPercentSliderChanged =true
                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState
                            //mainViewModel.updateDimPercentSlider(dimPercentSlider);
                            //mainViewModel.mqtt.requestTemperature()


                        }//,steps=5
                    )

                }


                Row() {
                    if (true) {
                        Column() {

                            //Text("Changed $tmp", color = Color.White)

                            Text(
                                "Temp: ${temperature.toString()}",
                                modifier = Modifier.padding(start = 20.dp),
                                color = Color.White
                            )

                            Text(
                                "Humidity: ${humidity.toString()} %",
                                modifier = Modifier.padding(start = 20.dp),
                                color = Color.White
                            )
                            Text(
                                "Ambient light: ${ambientLight.toString()} lux",
                                modifier = Modifier.padding(start = 20.dp),
                                color = Color.White
                            )


                            Row() {

                                Text(
                                    "Light: ",
                                    modifier = Modifier
                                        .padding(start = 20.dp),
                                    color = Color.White
                                )
                                //AnimatedContent(targetState = dimPercent) { targetState ->
                                Crossfade(targetState = dimPercent) { targetState ->
                                    //    Text(" ${dimPercent.toString()} %",
                                    Text(
                                        " ${targetState.toString()} %",
                                        modifier = Modifier,
                                        color = Color.White
                                    )

                                    //sliderPosition = (dimPercent.toFloat() / 100)
//                                    if (mainViewModel.initState && dimPercent > -1) {
//                                        Log.e(
//                                            "LOG",
//                                            "mqtt InitState ${(dimPercent).toString()}"
//                                        )
//                                        sliderPosition = (dimPercent.toFloat() / 100)
//                                        mainViewModel.initState = false
//                                    }


                                }

                            }

                            Box(modifier = Modifier.height(30.dp))
                        }

                    } else {
                        Text("no change ${Random.nextInt()}", color = Color.White)
                    }


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




}


//fun CardDormitor(mainViewModel:MainViewModel) {
//    val ambientLight by mainViewModel.ambientLight.collectAsState()
//    val dimPercent by mainViewModel.dimPercent.collectAsState()
//    val temperature by mainViewModel.temperature.collectAsState()
//    val humidity by mainViewModel.humidity.collectAsState()
//
//    val cardHeight = 0
//    var cardSize = 300.dp
//
//    var checkedState by rememberSaveable { mutableStateOf(false) }
//    var memoryIsVisible = false //by remember { mutableStateOf(false) }
//
//
//    val cardModifier = Modifier
//        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
//        .fillMaxWidth()
//
//        .height(260.dp)
//    val boxModifier = Modifier
//        .fillMaxSize()
//        .background(Color.DarkGray)
//
//    Card(
//        modifier = cardModifier
//            .onSizeChanged {
//
//                cardSize =cardHeight.dp +100.dp
//
//            }
//    )
//
//    {
//        var sliderPosition by rememberSaveable { mutableStateOf(0f) }
//        var sliderPositionMemory by rememberSaveable { mutableStateOf(0f) }
//
//        sliderPosition = dimPercent.toFloat()/100
//
//
////        Image(painter = painterResource(id = R.drawable.munte_cu_copiii),
////            contentDescription = "test munte",
////            contentScale = ContentScale.Crop,
////            modifier = Modifier.clip(RoundedCornerShape(20.dp))
////        )
//        Box(
//            modifier = boxModifier
//        ) {
//            Column() {
//                Row() {
//                    Text("Birou",modifier = Modifier.padding( all=10.dp),color=Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                }
//
//                var context = LocalContext.current
//                Row() {
//                    Text(
//                        text = "Switch lights",
//                        modifier = Modifier.padding(14.dp),
//                        color = Color.White
//                    )
//                    Switch(checked = sliderPosition > 0,
//                        modifier = Modifier
//                        //    .padding(10.dp)
//                        ,
//                        onCheckedChange = {
//                            Log.e("LOG", "mqtt ${it.toString()}")
//
//                            if (!it) {
//                                sliderPosition = 0f
//                                Log.e("LOG", "mqtt slider to 0 ${sliderPosition.toString()}")
//                                //mainViewModel.mqtt.setLightsPercent((sliderPosition*100).toInt())
//                            } else {
//                                if (sliderPositionMemory > 0) {
//                                    sliderPosition = sliderPositionMemory
//                                } else {
//                                    sliderPosition = 0.5f
//                                }
//                                Log.e("LOG", "mqtt slider to ${sliderPosition.toString()}")
//                            }
//                            Log.e("LOG", "mqtt Switch Move slider to: ${sliderPosition.toString()}")
//                            mainViewModel.mqtt.setLightsPercent((sliderPosition*100).toInt())
//                            checkedState = it
//                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState
//                            //mqtt.Connect()
//                            //mqtt.publishMessage(
//                            //    "lights:${(sliderPosition * 100).toInt()}",
//                            //    "toCameraMica"
//                            //)
//
//
//                        }
//
//                    )
//                    if (memoryIsVisible) {
//
//                        Text(
//                            text = "M: ${(sliderPositionMemory * 100).toInt()}",
//                            textAlign = TextAlign.End,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 14.dp, end = 10.dp),
//                            color = Color.White
//                        )
//                    }
//
//
//                }
//                Row() {
//
//
//                    //Text(sliderPosition.toString(),modifier=Modifier.padding(start = (sliderPosition *100).dp))
//
//                    Slider(
//                        value = sliderPosition,
//                        onValueChange = {
//                            //mainViewModel.mqtt.setLightsPercent(sliderPosition.toInt())
//                            //Log.e("LOG", "mqtt ValueChanged ${(sliderPosition*100).toString()}")
//                            sliderPosition = it;
//                            if (it > 0) {
//                                sliderPositionMemory = it
//
//                            }
//
//                        },
//                        onValueChangeFinished = {
//                            memoryIsVisible = sliderPositionMemory > 0 && !checkedState
//                            mainViewModel.mqtt.setLightsPercent((sliderPosition*100).toInt())
//                            //mainViewModel.mqtt.requestTemperature()
//                            Log.e("LOG", "mqtt ValueChangedFinished ${(sliderPosition * 100).toString()}")
//                        }//,steps=5
//                    )
//
//                }
//
//                Row() {
//                    if (true) {
//                        Column() {
//
//                            //Text("Changed $tmp", color = Color.White)
//
//                            Text("Temp: ${temperature.toString()}",modifier=Modifier.padding(start=20.dp), color = Color.White)
//                            Text("Humidity: ${humidity.toString()} %",modifier=Modifier.padding(start=20.dp), color = Color.White)
//                            Text("Ambient light: ${ambientLight.toString()} lux",modifier=Modifier.padding(start=20.dp), color = Color.White)
//                            Text("Light: ${dimPercent.toString()} %",modifier=Modifier.padding(start=20.dp), color = Color.White)
//
//                            Box(modifier = Modifier.height(30.dp))
//                        }
//
//                    }
//                    else {
//                        Text("no change ${Random.nextInt()}", color = Color.White)
//                    }
//
//
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colorStops = arrayOf(0.8f to Color.Transparent, 1f to Color.Black),
//                            startY = 0.3f
//                        )
//                    )
//            )
//
//            Box(
//                modifier = Modifier
//                    .offset(0.dp, cardSize - 20.dp)
//
//            ) {
////                Text(text = "111111111", color = Color.LightGray, fontSize = 20.sp)
//
//
//
//            }
//        }
//    }
//}

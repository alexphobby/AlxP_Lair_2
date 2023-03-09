package com.example.alxplair2

//import androidx.compose.foundation.pager.rememberPagerState
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.unit.dp
import com.example.alxplair2.ui.theme.AlxPLair2Theme
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.random.Random

class MainActivity : ComponentActivity(),MqttCallback {

    //var myCallBack = MyCallBack
    //var myhome = MyHome()

    var temperature = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //mqtt.cb = cb
        //mqtt.Connect()

        setContent {
            AlxPLair2Theme {

//                var signInRequest = BeginSignInRequest.builder()
//                    .setGoogleIdTokenRequestOptions(
//                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                            .setSupported(true)
//                            // Your server's client ID, not your Android client ID.
//                            .setServerClientId(getString(R.string.your_web_client_id))
//                            // Only show accounts previously used to sign in.
//                            .setFilterByAuthorizedAccounts(true)
//                            .build())
//                    .

                app(MainViewModel())
//                Greeting(name = "Mutable")
                //val pagerState = remember { mutableStateOf(1) }
                //TopAppBarSample()
                //loadMainPage()

                //tabsWithSwiping()
//tabsWithSwiping()
            }
        }
    }

    @Composable
    fun PageContent(mainViewModel: MainViewModel = MainViewModel()) {
        val clicked = remember { mutableStateOf(0) }
        val selectedTab = remember { mutableStateOf(0) }
        val selectedColor = remember { mutableStateOf(Color.Black) }
        //var isChanged by remember( myCallBack) {
         //   mutableStateOf(myCallBack.arrived)

            // Log.e("LOG", "mqtt")
       // }

        //var arrived by remember { mutableStateOf(cb.arrived) }



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
                            text = "Ambient",
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
                            text = "Altele",
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
                            text = "Not Used",
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

                            0 -> Tab1(false,mainViewModel)
                            1 -> show2()
                            2 -> show3()

                        }
                    }

                }

            }
        }

    }


    @Composable
    private fun app(mainViewModel: MainViewModel = MainViewModel()) {
        var gradientColor1 = Color.Black
        var gradientColor2 = Color.DarkGray
        var gradient = arrayOf(0.3f to gradientColor1, 1.0f to gradientColor2)

        mainViewModel.init() // Init MQTT connection



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
                PageContent(mainViewModel)
            }
        }

    }

    override fun connectionLost(cause: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.e("LOG", "mqtt interface callback receive")
        this.temperature = Random(10).nextInt()
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Log.e("LOG", "mqtt interface callback delivery")
    }

//    override fun onReceive() {
//        Log.e("LOG", "mqtt interface callback")
//    }
}



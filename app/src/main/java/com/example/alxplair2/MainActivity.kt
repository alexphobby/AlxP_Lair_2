package com.example.alxplair2

//import androidx.compose.foundation.pager.rememberPagerState

// for a 'val' variable

// for a `var` variable also add

import android.content.ContentValues
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.alxplair2.ui.theme.AlxPLair2Theme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()

    override fun onStop() {
        super.onStop()
        Log.e(ContentValues.TAG,"mqtt onStop")
        lifecycleScope.launch {

            vm.mqtt.client.disconnect()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.e(ContentValues.TAG,"mqtt onStart")
        lifecycleScope.launch {
            vm.init()
            vm.refresh()
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

                //app(MainViewModel())

                //Log.e(ContentValues.TAG,"mqtt onCreate setContent")


                //vm.add()
                //testMutableList(vm)
                PageContent(vm)
            }

                //Tab1(vm)
//                Greeting(name = "Mutable")
                //val pagerState = remember { mutableStateOf(1) }
                //TopAppBarSample()
                //loadMainPage()

                //tabsWithSwiping()
//tabsWithSwiping()
            }
        }

//        vm.init()
//
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.e(ContentValues.TAG,"mqtt onCreate")

        //  }

 //   @OptIn(ExperimentalMaterialApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)

        //mqtt.cb = cb
        //mqtt.Connect()


    }

    @Composable
    fun PageContent(mainViewModel: MainViewModel = MainViewModel()) {
        val clicked = remember { mutableStateOf(0) }
        val selectedTab = remember { mutableStateOf(0) }

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
                    Column {
                        //Text(modifier = Modifier.background(Color.Gray), text = "Text 1")
                        //Text(modifier = Modifier.background(Color.LightGray), text = "Text 2")
                        when (selectedTab.value) {

                            0 -> Tab1(mainViewModel)
                            1 -> show2()
                            2 -> show3()

                        }
                    }

                }

            }
        }

    }


}



package com.example.alexplair

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.alxplair2.MainViewModel
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.SSLSocketFactory

public data class MyHome (
    var rooms:List<Room> = listOf(Room())
)

public data class Room (
    var roomName:String="",
    var deviceName:String="",
    var topic:Topic = Topic("to/$deviceName","from/$deviceName"),
    var ambientLight: Double = 0.0,
    var humidity: Double = 0.0,
    var cameraMicaLed: Boolean = false,
    var dimPercent: Int = -1,
    var temperature: Double = 0.0
)

public data class Topic(
    var topicForReceive:String = "",
    var topicForSending:String = ""
)

public class MqttUtilities(val cb:MqttCallback) {
    public lateinit var client: MqttClient
    public lateinit var options: MqttConnectOptions

    public var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    public fun Connect(): Boolean {
        if (::client.isInitialized)
            return true


        //cb = MyCallBack
        client = MqttClient(
            "ssl://fc284e6f2eba4ea29babdcdc98e95188.s1.eu.hivemq.cloud:8883",
            MqttClient.generateClientId(),
            MemoryPersistence()
        )

        options = MqttConnectOptions()
        options.userName = "apanoiu"
        options.password = "Mqtt741852".toCharArray()
        options.socketFactory = SSLSocketFactory.getDefault()


        try {
            client.connect(options)

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "mqtt error, ${e.toString()}")
            return false
        }

        //var cb = MyCallBack()

        client.setCallback(cb)
        val current = LocalDateTime.now().format(formatter)

        //room//.value?.forEach {
            //Log.d("DEBUG", "mqtt subscribing to: ${room.value?.topic?.topicForSending.toString()}")
//        client.subscribe("from_a36_cam_mica")
        //client.subscribe("from_a_baie")
        //}
        client.subscribe("from/#")

        //client.subscribe("a36_cam_mica")
        //client.subscribe("a36_cam_medie")
        //client.subscribe("a36_cam_mare")
        //client.subscribe("fromCMica")

        var message = MqttMessage("Connected at $current".toByteArray(StandardCharsets.UTF_8))
        client.publish("android", message)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun publishMessage(message: String, topic: String): String {
        var result: String = ""

// while (!this::client.isInitialized || !client.isConnected) {
//     result+="need to connect\n"
//     Connect()
//     Thread.sleep(3000)
        Log.d(
            "DEBUG",
            "mqtt publishMessage ${message}-${topic} ${this::client.isInitialized.toString()}"
        )
        Log.d("DEBUG", "mqtt ${client.isConnected.toString()}")
//     Log.d("DEBUG","Reconnecting to mqtt")
// }
//
        val current = LocalDateTime.now().format(formatter)

        var mqttMessage = MqttMessage(message.toByteArray(StandardCharsets.UTF_8))
        if (!client.isConnected)
            client.connect(options)

        if (client.isConnected) {
            client.publish(topic, mqttMessage)
            result = "isConnected,published\n"
            return result
        }
        result = "issues"
        return result
    }

    fun requestStatuses() {
        //Log.d("DEBUG","mqtt ${this::client.isInitialized.toString()}")
        //publishMessage("sendTemperature", "to/#")
        publishMessage("sendTemperature", "to/a36_cam_mica")
        publishMessage("sendTemperature", "to/a_baie")
    }

    fun setLightsPercent(deviceName:String,percent:Int) {
        publishMessage("lights:$percent", "to/${deviceName}")
        requestStatuses()
        Log.e("DEBUG", "mqtt setLightsPercent: $percent")
    }
}

public class TemperatureCallback(viewModel:MainViewModel) : MqttCallback  {
    public var arrived = false
    //public var messageText = "x"
    public var myhome = MyHome()
    var viewModel=viewModel
    public var rnd:Int=0
    public var message = ""
    var room=viewModel.roomMutable

    override fun connectionLost(cause: Throwable?) {
        Log.e(ContentValues.TAG, "mqtt Connection Lost")
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        this.message = message.toString()
//        Log.e(ContentValues.TAG,"mqtt messageArrived: ${this.message} on topic: $topic")
        try {

            val device = topic?.replace("from/","") ?: return

            val (key,value) = message.toString().split(":")
            Log.e(ContentValues.TAG,"mqtt messageArrived, topic: ${topic}, device: ${device}: key: ${key} -> $value")

            when (key) {
                        "name" -> {
                            viewModel.updateRoomName(device, value)
                        }
                        "random" -> {
                            Log.e(
                                ContentValues.TAG,
                                "mqtt messageArrived, update random: -> $value"
                            )
                            viewModel.updateTemperatureToRoom(device, value.toDouble())
                        }
                        "temperature" -> {
                            Log.e(ContentValues.TAG,"mqtt messageArrived, update temperature: ${device} -> $value")

                            viewModel.updateTemperatureToRoom(device,value.toDouble())
                            //viewModel._rooms//.update { it}
                            //arrived=true
                        }
                        "humidity" -> {
                            viewModel.updateHumidityToRoom(device,value.toDouble())
                        }
                        "ambient" -> {
                            viewModel.updateAmbientLightToRoom(device,value.toDouble())
                        }
                        "dim" -> {
                            viewModel.updateDimPercentToRoom(device,value.toInt())
                        }

                    }}
        catch (e:Exception) {
                    Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
                }

//        viewModel.roomListMutable.value?.forEach {
//            //Log.e(ContentValues.TAG,"mqtt messageArrived, checking room:: ${it.roomName}")
//            if (topic == it.topic.topicForSending) {
//                Log.e(ContentValues.TAG,"mqtt messageArrived: ${this.message} for: ${it.roomName}")
//                try {
//                    var (k,v) = message.toString().split(":")
//                    //Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
//                    when (k) {
//                        "temperature" -> {
//                            Log.e(ContentValues.TAG,"mqtt messageArrived, update temperature: ${it.roomName} -> $v")
//                            it.temperature = v.toDouble()
//
//                            viewModel.updateTemperatureToRoom(it.roomName,v.toDouble())
//                            //viewModel._rooms//.update { it}
//                            //arrived=true
//                        }
//                        "humidity" -> {
//                            it.humidity = v.toDouble()
//                            viewModel.updateHumidityToRoom(it.roomName,it.humidity)
//                        }
//                        "ambient" -> {
//                            it.ambientLight = v.toDouble()
//                            viewModel.updateAmbientLightToRoom(it.roomName,it.ambientLight)
//                        }
//                        "dim" -> {
//                            it.dimPercent = v.toInt()//.toDouble()*100).toInt()
//                            viewModel.updateDimPercentToRoom(it.roomName,it.dimPercent)
//                        }
//                    }
//
//                    //viewModel.updateMqttData(myhome)
//                    //vm.addTemperature(rnd)
//
//                }
//                catch (e:Exception) {
//                    Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
//                }
//            }
//
//
//        }

        //var rnd = Random.nextInt(0,100)
        //Log.e(ContentValues.TAG,"mqtt messageArrived: $message")
//        _cbFunction(rnd)
        //vm.addTemperature(message.toString())

        //arrived = false
        //messageText = message.toString()

        //var rnd = Random.nextInt(0,1000)
        //myhome.rnd = Random.toString()

        if (topic == "a36_cam_medie") {

        }

    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Log.e(ContentValues.TAG,"mqtt delivered")
    }



}
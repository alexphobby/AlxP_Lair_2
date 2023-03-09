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
import kotlin.random.Random

public data class MyHome (
    var rooms:List<Room> = listOf(Room())
//    var topic:String = "",
//    var ambientLight: Double = 0.0,
//    var humidity: Double = 0.0,
//    var cameraMicaLed: Boolean = false,
//    var dimPercent: Int = -1,
//    var temperature: Double = 0.0
    //var rnd: String = ""
)
public data class Room (
    var roomName:String="",
    var topic:Topic = Topic("to_$roomName","from_$roomName"),
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

public class MqttUtilities(val cb:MqttCallback,topics:List<Room>) {
    public lateinit var client: MqttClient
    public lateinit var options: MqttConnectOptions
    //public lateinit var cb: MyCallBack

    @RequiresApi(Build.VERSION_CODES.O)
    public var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
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


        client.subscribe("a36_cam_mica")
        client.subscribe("a36_cam_medie")
        client.subscribe("a36_cam_mare")
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
        publishMessage("sendTemperature", "a36_cam_mica")
    }

    fun setLightsPercent(percent:Int) {
        publishMessage("lights:$percent", "a36_cam_mica")
        requestStatuses()
        Log.e("DEBUG", "mqtt setLightsPercent: $percent")
    }
}

public class TemperatureCallback(viewModel:MainViewModel) : MqttCallback  {
    public var arrived = false
    public var messageText = "x"
    public var myhome = MyHome()
    var viewModel=viewModel
    public var rnd:Int=0
    public var message = ""


    override fun connectionLost(cause: Throwable?) {
        Log.e(ContentValues.TAG, "mqtt Connection Lost")
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        this.message = message.toString()
        //var rnd = Random.nextInt(0,100)
        //Log.e(ContentValues.TAG,"mqtt messageArrived: $message")
//        _cbFunction(rnd)
        //vm.addTemperature(message.toString())

        //arrived = false
        messageText = message.toString()

        var rnd = Random.nextInt(0,1000)
        //myhome.rnd = Random.toString()

        if (topic == "a36_cam_mica") {
            try {
                var (k,v) = message.toString().split(":")
                //Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
                if (k == "temperature") {
                    myhome.rooms[0].temperature = message.toString().split(":")[1].toDouble()
                    viewModel.updateTemperature(myhome.rooms[0])
                    arrived=true
                }
                else if (k == "humidity") {
                    myhome.rooms[0].humidity = message.toString().split(":")[1].toDouble()
                    viewModel.updateHumidity(myhome.rooms[0])
                }
                else if (k == "ambient") {
                    myhome.rooms[0].ambientLight = message.toString().split(":")[1].toDouble()
                    viewModel.updateAmbientLight(myhome.rooms[0])
                }
                else if (k == "dim") {
                    myhome.rooms[0].dimPercent = (message.toString().split(":")[1]).toInt()//.toDouble()*100).toInt()
                    viewModel.updateDimPercent(myhome.rooms[0].dimPercent)
                }

                //viewModel.updateMqttData(myhome)
                //vm.addTemperature(rnd)

            }
            catch (e:Exception) {
                Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
            }
        }

    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Log.e(ContentValues.TAG,"mqtt delivered")
    }



}
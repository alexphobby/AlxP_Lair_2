package com.example.alexplair

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.SSLSocketFactory

public object MyHome {
    public var light: Double = 0.0
    public var humidity: Double = 0.0
    public var cameraMicaLed = false
    public var temperature = 0.0

}


object MyCallBack : MqttCallback {
    public var arrived = false
    public var messageText = ""
    public var myhome = MyHome


    override fun connectionLost(cause: Throwable?) {
        Log.e(ContentValues.TAG, "mqtt Connection Lost")
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.e(ContentValues.TAG,"mqtt Arrived: $message")
        arrived = true
        messageText = message.toString()


        if (topic == "fromCMica") {
            try {
                var (k,v) = message.toString().split(":")
                //Log.e(ContentValues.TAG, "mqtt error messageArrived, ${e.toString()}")
                if (k == "t") {
                    myhome.temperature = message.toString().split(":")[1].toDouble()
                }
                else if (k == "h") {
                    myhome.humidity = message.toString().split(":")[1].toDouble()
                }
                else if (k == "l") {
                    myhome.light = message.toString().split(":")[1].toDouble()
                }
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


public object MqttUtilities {
    public lateinit var client : MqttClient
    public lateinit var options:MqttConnectOptions
    public lateinit var cb : MyCallBack
    @RequiresApi(Build.VERSION_CODES.O)
    public var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    public fun Connect():Boolean {
        if (::client.isInitialized)
            return true

        cb = MyCallBack
        client = MqttClient(
            "ssl://fc284e6f2eba4ea29babdcdc98e95188.s1.eu.hivemq.cloud:8883",
            MqttClient.generateClientId(),
            MemoryPersistence()
        )

        options = MqttConnectOptions()
        options.userName = "apanoiu"
        options.password = "Mqtt741852".toCharArray()
        options.socketFactory = SSLSocketFactory.getDefault()


        try{
            client.connect(options)

        }
        catch (e:Exception) {
            Log.e(ContentValues.TAG, "mqtt error, ${e.toString()}")
            return false
        }

        //var cb = MyCallBack()

        client.setCallback(cb)
        val current = LocalDateTime.now().format(formatter)

        client.subscribe("cameraMica")
        client.subscribe("fromCMica")

        var message = MqttMessage("Connected at $current".toByteArray(StandardCharsets.UTF_8))
        client.publish("android", message)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun publishMessage(message:String,topic:String):String {
        var result:String = ""

// while (!this::client.isInitialized || !client.isConnected) {
//     result+="need to connect\n"
//     Connect()
//     Thread.sleep(3000)
     Log.d("DEBUG","mqtt publishMessage ${message}-${topic} ${this::client.isInitialized.toString()}")
     Log.d("DEBUG","mqtt ${client.isConnected.toString()}")
//     Log.d("DEBUG","Reconnecting to mqtt")
// }
//
        val current = LocalDateTime.now().format(formatter)

        var mqttMessage = MqttMessage(message.toByteArray(StandardCharsets.UTF_8))
        if(!client.isConnected)
            client.connect(options)
        
        if (client.isConnected) {
            client.publish(topic, mqttMessage)
            result+="isConnected,published\n"
            return result
        }
        result +="issues"
        return result
    }

    fun requestTemperature() {
        //Log.d("DEBUG","mqtt ${this::client.isInitialized.toString()}")
        publishMessage("sendTemperature","toCameraMica")
    }
}
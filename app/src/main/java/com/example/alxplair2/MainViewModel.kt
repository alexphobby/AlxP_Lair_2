package com.example.alxplair2

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alexplair.MqttUtilities
import com.example.alexplair.MyHome
import com.example.alexplair.Room
import com.example.alexplair.TemperatureCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel:ViewModel() {
//    private val _temperatureList:MutableList<Int> = mutableStateListOf()
//    val temperatureList :List<Int> = _temperatureList

//    private var _temperatureData = MutableStateFlow((TemperatureData()))
//    val temperatureData:StateFlow<TemperatureData> = _temperatureData.asStateFlow()

    private var _temperature = MutableStateFlow(MyHome().rooms[0].temperature)
    val temperature: StateFlow<Double> = _temperature.asStateFlow()

    private var _humidity = MutableStateFlow(MyHome().rooms[0].humidity)
    val humidity: StateFlow<Double> = _humidity.asStateFlow()

    private var _ambientLight = MutableStateFlow(MyHome().rooms[0].ambientLight)
    val ambientLight: StateFlow<Double> = _ambientLight.asStateFlow()

    private var _dimPercent = MutableStateFlow(MyHome().rooms[0].dimPercent)
    val dimPercent: StateFlow<Int> = _dimPercent.asStateFlow()

    private var _dimPercentSlider = MutableStateFlow(0f)
    val dimPercentSlider: StateFlow<Float> = _dimPercentSlider.asStateFlow()

    public var initState = true

    //lateinit var cb:MyCallBack
    lateinit var mqtt:MqttUtilities

    fun init() {
        var rooms = listOf<Room>(
            Room("a36_cam_mica"),
            Room("a36_cam_medie")
        )
        //cb = MyCallBack(){addTemperature(0)}
        mqtt = MqttUtilities(cb = TemperatureCallback(this), rooms)
        mqtt.Connect()
        mqtt.requestStatuses()

}

    fun updateTemperature(data: Room) {
        _temperature.update { data.temperature }
    }
    fun updateAmbientLight(data: Room) {
        _ambientLight.update { data.ambientLight }
    }
    fun updateDimPercent(dimPercent:Int) {
        _dimPercent.update { dimPercent }
        //mqtt.setLightsPercent(dimPercent)
        Log.e("mqtt","updateDimPercent(${dimPercent}) ")
        //initState = true
    }
    fun updateHumidity(data: Room) {
        _humidity.update { data.humidity }

    }

//        fun updateMqttData(data:MyHome) {
        //_mqttData.update { data }
        //_temperatureData.update { Random.nextInt().toString() }


    //Log.i("mqtt updateMqttData","LED: ${data.cameraMicaLedValue.toString()}, Ambient: ${data.ambientLight}, Rnd: ${temperatureData.value}")
//    }

//    fun addTemperature(temperature: MutableStateFlow<TemperatureData> = MutableStateFlow(
//        TemperatureData(0)
//    fun addTemperature(temp:Int){

//    }

}

//public class TemperatureCallback(cbFunction:(input:Int)->Unit) : MqttCallback  {


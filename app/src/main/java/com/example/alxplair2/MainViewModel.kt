package com.example.alxplair2

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
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

    private var _roomName = MutableStateFlow("")
    val roomName: StateFlow<String> = _roomName.asStateFlow()


    //list
    var sel = mutableStateOf(0)
    fun setSel(index:Int) {
        this.sel.value = index
    }


    //private var _temperature = MutableStateFlow(MyHome().rooms[0].temperature)
    //val temperature: StateFlow<Double> = _temperature.asStateFlow()
    private var _temperature = MutableStateFlow(0f.toDouble())
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
    public var roomList = listOf<Room>(
        //Room("a36_cam_mica"),
        Room("a36_cam_medie")
    )
    //private val _roomListMutable:MutableList<Room> = mutableStateListOf()
    //val roomListMutable: List<Room> = _roomListMutable

    var testMutable = MutableLiveData<Int>(0)
    //var testMutable: MutableState<Int.Companion> = _testMutable

    var roomMutable = MutableLiveData<Room>(Room("a36_cam_mica"))
    var roomsMutable = mutableStateListOf<Room>(
        //Room("a36_cam_mica"),
        //Room("a36_cam_medie"),
        Room(deviceName="a_baie"),
        Room(deviceName="a36_cam_medie")
    )

    fun add() {
       testMutable.value=testMutable.value?.plus(1)

        //_roomListMutable.add(Room("a36_cam_medie"))
        //_roomListMutable.add(Room("a36_cam_mica"))
    }
    //= MutableList<Room>((roomList) //MutableStateListOf<Room>()
    //public var  roomListMutable = MutableStateFlow(roomList)//MutableList<Room>(roomList)
    //public var _rooms = MutableStateFlow(roomList)
    //val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

//    val roomList by MutableStateFlow(listOf<Room>())


    //lateinit var cb:MyCallBack
    lateinit var mqtt:MqttUtilities

    fun init() {
        //_roomListMutable.add(Room("a36_cam_mica"))
        //_roomListMutable.add(Room("a36_cam_medie"))

        Log.e(ContentValues.TAG,"mqtt INIT")
        //cb = MyCallBack(){addTemperature(0)}
        mqtt = MqttUtilities(cb = TemperatureCallback(this))
        mqtt.Connect()
        mqtt.requestStatuses()

}

    fun getDeviceNameFromRoomName(roomName: String):String{
        val _roomIndex = roomsMutable.indexOfFirst {it.roomName == roomName }
        return roomsMutable[_roomIndex].deviceName
    }

    fun updateTemperature(data: Room) {
        _temperature.update { data.temperature }
    }
    fun updateTemperatureToRoom(deviceName:String,newTemperature:Double){
        Log.e(ContentValues.TAG,"mqtt MVM_updateTemperatureToRoom: ${roomName} for: ${newTemperature}")
        //_roomListMutable.add(Room("a36_cam_medie", temperature = 35f.toDouble())) //= roomListMutable.map { room ->
            //if (roomListMutable.value.roomName == roomName) room.copy(temperature=newTemperature)
            //else room
        //}
//        testMutable.postValue(newTemperature.toInt())
//        roomMutable.postValue(Room(roomName, temperature = newTemperature)) //roomListMutable.value.find { if (it.roomName == roomName) it.copy(temperature = newTemperature) else it}


        val _roomIndex = roomsMutable.indexOfFirst {it.deviceName == deviceName }
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(temperature = newTemperature)

//        #val _room = roomsMutable.value?.find { it.roomName == roomName }
//        #val _otherRooms = roomsMutable.value?.find { it.roomName != roomName }
//        #val _newRoom = _room
//        #_newRoom?.temperature= newTemperature
//
//        #roomsMutable.postValue(listOf(_room,_otherRooms) as List<Room>?)


        //val newList = roomsMutable.value
        //newList.toList()

        //_roomName.update { roomName }
        //_temperature.update { temperature }
    }
    fun updateHumidityToRoom(deviceName:String,newHumidity:Double){
        val _roomIndex = roomsMutable.indexOfFirst {it.deviceName == deviceName }
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(humidity = newHumidity)

//        _roomListMutable = roomListMutable.map { room ->
//            if (room.roomName == roomName) room.copy(humidity = newHumidity )
//            else room
//        }
//        _roomName.update { roomName }
//        _humidity.update { humidity }
    }
    fun updateAmbientLightToRoom(deviceName: String,newAmbientLight:Double){
        val _roomIndex = roomsMutable.indexOfFirst {it.deviceName == deviceName }
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(ambientLight = newAmbientLight)

//        _roomName.update { roomName }
//        _ambientLight.update { ambientLight }
    }
    fun updateDimPercentToRoom(deviceName:String,newDimPercent: Int){
        Log.e(ContentValues.TAG,"mqtt MVM_updateDimPercentToRoom: ${deviceName} for: ${newDimPercent}")
        val _roomIndex = roomsMutable.indexOfFirst {it.deviceName == deviceName }
        Log.e(ContentValues.TAG,"mqtt MVM_updateDimPercentToRoom: ${deviceName} Index: ${_roomIndex}")
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(dimPercent=newDimPercent)

//        _roomName.update { roomName }
//        _dimPercent.update { dimPercent }
    }

    fun updateAmbientLight(data: Room) {
//        _ambientLight.update { data.ambientLight }
    }

    fun updateRoomName(device: String, roomName: String) {
        val _roomIndex = roomsMutable.indexOfFirst {it.deviceName == device }
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(roomName = roomName)

    }

    fun updateDimPercentToRoomName(cardRoomName: String, newDimPercent: Int) {
        val _roomIndex = roomsMutable.indexOfFirst {it.roomName == cardRoomName }
        //Log.e(ContentValues.TAG,"mqtt MVM_updateDimPercentToRoom: ${deviceName} Index: ${_roomIndex}")
        roomsMutable[_roomIndex] = roomsMutable[_roomIndex].copy(dimPercent=newDimPercent)
    }
//    fun updateDimPercent(dimPercent:Int) {
////        _dimPercent.update { dimPercent }
//        //mqtt.setLightsPercent(dimPercent)
//        Log.e("mqtt","updateDimPercent(${dimPercent}) ")
//        //initState = true
//    }
//    fun updateHumidity(data: Room) {
////        _humidity.update { data.humidity }
//
//    }

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


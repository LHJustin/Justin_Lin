package com.tom.justin_lin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ChatroomModel: ViewModel() {
    var chatRooms = MutableLiveData<List<Lightyear>>()
    var mes = MutableLiveData<String>()
    fun getAllRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, Chatrooms::class.java)
            chatRooms.postValue(response.result.lightyear_list)
        }
    }
    fun getValue(Text : String){
        mes.postValue(Text)
    }
}
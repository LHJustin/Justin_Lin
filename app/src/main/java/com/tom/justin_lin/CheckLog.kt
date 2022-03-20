package com.tom.justin_lin

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckLog : ViewModel(){
    val boolean = MutableLiveData<Boolean>()
    val data = listOf<String>(" ","~","!","#","$","%","^","&","*","(",")","_","-","+","=","?","<",">",".","—","，","。","/"
        ,"\\","|","《",",","》","？",";",":","：","'","‘","；","“")

    fun check(account: String, secret: String){
        var y = 0
        data.forEach {
           y = if (account.indexOf(it)>-1 || secret.indexOf(it)> -1) y+1
            else y+0
        }
        val _boolean = if (account.length >20 || account.length <4) false
        else if (secret.length >12 || secret.length <6) false
        else if (y>0) false
        else true
        boolean.value = _boolean
    }
}
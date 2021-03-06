package com.tom.justin_lin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tom.justin_lin.databinding.FragmentRoomBinding
import com.tom.justin_lin.databinding.RowChatroomBinding
import com.tom.justin_lin.databinding.RowMessageBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class RoomFragment: Fragment() {
    private var TAG = RoomFragment::class.java.simpleName
    private val viewmodel by viewModels<ChatroomModel>()

    lateinit var binding: FragmentRoomBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("member", Context.MODE_PRIVATE)
        val num = pref.getInt("lognum", 0)
        var name:String? = ""
        if(num>-1){
            name = pref.getString("name$num", "")
        }else name = getString(R.string.someone)

        //聊天室
        binding.recyclerMessage.setHasFixedSize(true)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL , true)
        binding.recyclerMessage.scrollToPosition(-1)
        val Adapter = MessageAdapter()
        binding.recyclerMessage.adapter = Adapter
        viewmodel.mes.observe(viewLifecycleOwner) {text ->
            Adapter.submitRoom(text)
        }
        //websocket
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$name")
            .build()
        var websocket = client.newWebSocket(request, object : WebSocketListener(){
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val json = text
                if ("default_message" in json){
                    val response = Gson().fromJson(json, Message::class.java)
                    viewmodel.getValue(response.body.nickname+":"+response.body.text)
                }else if ("sys_updateRoomStatus" in json){
                    val response = Gson().fromJson(json, Into::class.java)
                    var action = response.body.entry_notice.action
                    var user = response.body.entry_notice.username
                    when(action){
                        "enter" -> viewmodel.getValue("$user"+getString(R.string.enter))
                        "leave" -> viewmodel.getValue("$user"+getString(R.string.leave))
                        else -> viewmodel.getValue("")
                    }
                }else if ("admin_all_broadcast" in json){
                    val response = Gson().fromJson(json, Notice::class.java)
                    val cn = response.body.content.cn
                    val en = response.body.content.en
                    val tw = response.body.content.tw
                    viewmodel.getValue("""en"""+en+"\n"+"""cn"""+cn+"\n"+"""tw"""+tw)
                }else if ("sys_room_endStream" in json){
                    val response = Gson().fromJson(json, End::class.java)
                    viewmodel.getValue(response.body.text)
                }

            }
        })

        //Media
        val videoView = binding.videoView
        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.hime3))
        videoView.start()

        //按傳送按鈕後
        binding.ibSend.setOnClickListener {
            val msg = binding.edMessage.text.toString()
            val j = Gson().toJson(Send("N",msg))
            websocket.send(j)
            binding.edMessage.setText("")
        }

        //按右上的按鈕會回首頁
        binding.ibOut.setOnClickListener {
            val parentactivity = requireActivity() as MainActivity
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, HomeFragment()).commit()
            }
            parentactivity.binding.roomContainer.visibility = View.GONE
            parentactivity.binding.buttonNavBar.visibility = View.VISIBLE
            parentactivity.binding.myContainer.visibility = View.VISIBLE
        }
    }
    inner class BindingViewHolder(binding: RowMessageBinding): RecyclerView.ViewHolder(binding.root){
        val chatmsg = binding.tvMessage
    }

    inner class MessageAdapter : RecyclerView.Adapter<RoomFragment.BindingViewHolder>(){
        var text = mutableListOf<String>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
            val binding = RowMessageBinding.inflate(layoutInflater, parent, false)
            return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RoomFragment.BindingViewHolder, position: Int) {
            val mes = text[position]
            holder.chatmsg.setText(mes)
        }

        override fun getItemCount(): Int {
            return text.size
        }

        fun submitRoom(room: String) {
            text.add(0,room)
            notifyDataSetChanged()
        }
    }

    data class Messager(val message:String){
    }
}


package com.tom.justin_lin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ButtonBarLayout
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tom.justin_lin.databinding.FragmentHomeBinding
import com.tom.justin_lin.databinding.RowChatroomBinding

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding
    //定義viewmodel變數是ChatroomModel
    private val viewmodel by viewModels<ChatroomModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        binding.recyclerHome.setHasFixedSize(true)
        binding.recyclerHome.layoutManager = GridLayoutManager(requireContext(), 2)
        var adapter = RoomAdapter()
        binding.recyclerHome.adapter = adapter
        //ViewModel方法
        viewmodel.chatRooms.observe(viewLifecycleOwner) {rooms ->
            adapter.submitRoom(rooms)
        }
        viewmodel.getAllRooms()
    }

    //帶出row_chatroom的binding
    inner class BindingViewHolder(binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root){
        val title = binding.chatroomTitle
        val tag = binding.chatroomTag
        val name = binding.chatroomNickname
        val headshot = binding.headShot
    }

    //
    inner class RoomAdapter : RecyclerView.Adapter<BindingViewHolder>(){
        //帶出ProgramClasses的資料
        var rooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
            val lightyear = rooms[position]
            holder.title.setText(lightyear.stream_title)
            holder.tag.setText(lightyear.tags)
            holder.name.setText(lightyear.nickname)
            Glide.with(this@HomeFragment).load(lightyear.head_photo).into(holder.headshot)
            holder.itemView.setOnClickListener {
                chatRoomClicked(lightyear)
            }
        }

        override fun getItemCount(): Int {
            return rooms.size
        }

        fun submitRoom(room: List<Lightyear>) {
            rooms.clear()
            rooms.addAll(room)
            notifyDataSetChanged()
        }
    }
    fun chatRoomClicked(lightyear: Lightyear){
        val parentactivity = requireActivity() as MainActivity
        requireActivity().supportFragmentManager.beginTransaction().run {
            replace(R.id.room_container, RoomFragment()).commit()
        }
        parentactivity.binding.buttonNavBar.visibility = View.GONE
        parentactivity.binding.myContainer.visibility = View.GONE
        parentactivity.binding.roomContainer.visibility = View.VISIBLE
    }
}
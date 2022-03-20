package com.tom.justin_lin

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tom.justin_lin.databinding.FragmentRoomBinding
import com.tom.justin_lin.databinding.RowMessageBinding

class RoomFragment: Fragment() {
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
        binding.recyclerMessage.setHasFixedSize(true)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL , true)
        binding.recyclerMessage.scrollToPosition(-1)

        //Media
        val videoView = binding.videoView
        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.hime3))
        videoView.start()

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
    inner class BindingViewHolder(binding: RowMessageBinding):RecyclerView.ViewHolder(binding.root){
        val message = binding.tvMessage
    }
}
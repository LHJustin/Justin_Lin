package com.tom.justin_lin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tom.justin_lin.databinding.FragmentPickBinding

class PickFragment: Fragment() {
    lateinit var binding : FragmentPickBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bBacksign.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, SignupFragment()).commit()
            }
        }
    }
}
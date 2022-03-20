package com.tom.justin_lin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tom.justin_lin.databinding.FragmentPersonBinding

class PersonFragment: Fragment() {
    lateinit var binding: FragmentPersonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("member", Context.MODE_PRIVATE)
        val num = pref.getInt("lognum", 0)
        val name = getString(R.string.nicknames)+pref.getString("name$num", "")
        val user = getString(R.string.accounts)+pref.getString("account$num", "")
        binding.tvName.setText(name)
        binding.tvUser.setText(user)
        binding.bLogout.setOnClickListener {
            pref.edit().putInt("lognum", -1).apply()
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, LoginFragment()).commit()
            }
        }
    }
}
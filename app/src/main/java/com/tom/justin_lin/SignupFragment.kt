package com.tom.justin_lin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tom.justin_lin.databinding.FragmentSignupBinding
import kotlin.concurrent.thread

class SignupFragment: Fragment() {
    var TAG = SignupFragment::class.java.simpleName
    val viewmodel by viewModels<CheckLog>()

    lateinit var binding : FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //定義pref是Preferences
        val pref = requireContext().getSharedPreferences("member", Context.MODE_PRIVATE)
        var number = pref.getInt("num",0)
        binding.bSend.setOnClickListener {
            var name = binding.edSignname.text.toString()
            var user = binding.edSignuser.text.toString()
            var pass = binding.edSignpass.text.toString()
            val warn = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.login_message2))
                .setPositiveButton("ok",null)
            viewmodel.check(user,pass)
            viewmodel.boolean.observe(viewLifecycleOwner){
                if (it == true){
                    thread {
                        pref.edit().putString("name$number",name)
                            .putString("account$number", user)
                            .putString("password$number", pass)
                            .putInt("num",number)
                            .putInt("lognum",number)
                            .apply()
                    }
                    number++
                    requireActivity().supportFragmentManager.beginTransaction().run {
                        replace(R.id.my_container,HomeFragment()).commit()
                    }
                }else warn.show()
            }
        }

        binding.ibPick.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, PickFragment()).commit()
            }
        }
        //回到LogIn介面
        binding.ibBacklog.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, LoginFragment()).commit()
            }
        }
    }
}
package com.tom.justin_lin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tom.justin_lin.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    lateinit var binding: FragmentLoginBinding
    //定義變數viewModel連接到CheckLog(注意是用by)
    val viewModel by viewModels<CheckLog>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("member", Context.MODE_PRIVATE)
        var number = pref.getInt("num",0)
        //checkbox勾選會記住
        var rem = false
        val checked = pref.getBoolean("remember", false)
        binding.checkBox.isChecked = checked
        binding.checkBox.setOnCheckedChangeListener { compoundButton, checked ->
            rem = checked
            pref.edit().putBoolean("remember", rem).apply()
            if (rem == false){
                pref.edit().putString("USER","").apply()
                pref.edit().putString("PASS","").apply()
            }
        }
        val prefUser = pref.getString("USER", "")
        val prefPass = pref.getString("PASS", "")
        binding.edLoguser.setText(prefUser)
        binding.edLogpass.setText(prefPass)

        //按下Log In按鈕的動作
        binding.bLogin.setOnClickListener {
            val user = binding.edLoguser.text.toString()
            val pass = binding.edLogpass.text.toString()
            //格式錯誤警告標語
            val warn = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.login_message2))
                .setPositiveButton("ok",null)
            //帳密輸錯警告標語
            val warn2 = R.string.login_message
            //協助判斷帳密的變數
            var y = 0
            //user跟pass傳入check方法裡運算
            viewModel.check(user, pass)
            //判斷帳號密碼有無符合格式
            viewModel.boolean.observe(viewLifecycleOwner) {
                if(it==true){
                    for (i in 0..number){
                        if (user == pref.getString("account$i","") && pass == pref.getString("password$i","")){
                            y++
                            pref.edit().putInt("lognum", i).apply()
                        }
                    }
                    if (y>0) {
                        if (rem){
                            pref.edit().putString("USER", user).apply()
                            pref.edit().putString("PASS", pass).apply()
                        }
                        binding.tvWarn.setText("")
                        requireActivity().supportFragmentManager.beginTransaction().run{
                            replace(R.id.my_container, HomeFragment()).commit()
                        }
                    }else {
                        binding.tvWarn.setText(warn2)
                    }
                }else warn.show()
            }
        }
        binding.bSignin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().run {
                replace(R.id.my_container, SignupFragment()).commit()
            }
        }
    }
}
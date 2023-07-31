package com.comet.mudle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), ActivityCallback {

    override fun switchRegister() {
        //commit을 해줘야됨
        supportFragmentManager.beginTransaction().replace(R.id.frame, RegisterFragment()).commit()
    }

    override fun switchMain() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, GameFragment()).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val isRegistered = viewModel.isUserExists()
        if (isRegistered)
        //회원가입이 된경우
            switchMain()
        else
            switchRegister()
    }
}

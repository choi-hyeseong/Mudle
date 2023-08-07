package com.comet.mudle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

const val LOG = "MUDLE_LOG"
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityCallback {

    private val mainViewModel : MainViewModel by viewModels()

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
        //단순 livedata만 리턴하더라도 최신 값 읽어서 return 해줌
        mainViewModel.isUserExists().observe(this) { isRegistered ->
            if (isRegistered)
            //회원가입이 된경우
                switchMain()
            else
                switchRegister()

        }

    }
}

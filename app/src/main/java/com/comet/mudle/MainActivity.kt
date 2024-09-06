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

    // oncreate 2번 호출 문제 해결
    override fun switchRegister() {
        //commit을 해줘야됨
        val fragment = supportFragmentManager.findFragmentById(R.id.frame) as? RegisterFragment?
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment ?: RegisterFragment()).commit()
    }

    override fun switchMain() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame) as? GameFragment?
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment ?: GameFragment()).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //단순 livedata만 리턴하더라도 최신 값 읽어서 return 해줌

        mainViewModel.userRegistrationLiveData.observe(this) { isRegistered ->
            if (isRegistered)
            //회원가입이 된경우
                switchMain()
            else
                switchRegister()
        }

    }
}

package com.comet.mudle

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

const val LOG = "MUDLE_LOG"
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityCallback {

    private val requiredPermissions : List<String> = listOf("android.permission.RECORD_AUDIO", "android.permission.MODIFY_AUDIO_SETTINGS") //필요한 펄미션
    private val code = 200
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
        checkPermission()
    }

    //실제 메인 메소드
    private fun init() {
        mainViewModel.userRegistrationLiveData.observe(this) { isRegistered ->
            if (isRegistered)
            //회원가입이 된경우
                switchMain()
            else
                switchRegister()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != code)
            return
        val result = grantResults.filter { it == PackageManager.PERMISSION_DENIED } //비허용된것만
        if (result.isNotEmpty()) {
            Toast.makeText(this, R.string.permission_check, Toast.LENGTH_SHORT).show()
            finish()
        }
        else
            init()
    }

    private fun checkPermission() {
        // 펄미션 허용 안된것들만
        val requirePermission = requiredPermissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (requirePermission.isNotEmpty())
            ActivityCompat.requestPermissions(this, requirePermission.toTypedArray(), code)
        else
            init()
    }


}

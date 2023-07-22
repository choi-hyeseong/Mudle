package com.comet.mudle

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.dao.PrefUserDao
import com.comet.mudle.dao.UserDao
import com.comet.mudle.model.Chat
import com.comet.mudle.model.User
import com.comet.mudle.viewmodel.MainViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.UUID


const val PREFERENCE = "USER"

class MainActivity : AppCompatActivity(), ActivityCallback {

    override fun switchRegister() {
        //commit을 해줘야됨
        supportFragmentManager.beginTransaction().replace(R.id.frame, RegisterFragment()).commit()
    }

    override fun switchMain() {
        //TODO
        supportFragmentManager.beginTransaction().replace(R.id.frame, GameFragment()).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = MainViewModel(getSharedPreferences(PREFERENCE, MODE_PRIVATE))
        val isRegistered = viewModel.isUserExists()
        if (isRegistered)
        //회원가입이 된경우
            switchMain()
        else
            switchRegister()
    }
}

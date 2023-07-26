package com.comet.mudle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comet.mudle.DependencyUtil
import com.comet.mudle.custom.ListLiveData
import com.comet.mudle.model.Chat
import com.comet.mudle.repository.UserRepository
import com.comet.mudle.type.MessageType
import com.comet.mudle.web.rest.MusicAPI
import com.comet.mudle.web.stomp.StompManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class GameViewModel : ViewModel() {


    private val retrofit = DependencyUtil.retrofit
    private val musicAPI: MusicAPI by lazy { retrofit.create(MusicAPI::class.java) }
    val stompManager : StompManager = StompManager(UserRepository(DependencyUtil.preferences))

    override fun onCleared() {
        super.onCleared()
        stompManager.close()
    }

    fun request(music: String) {
        musicAPI.requestMusic(music).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("asdf", "success")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("asdf", "fail")
            }
        })
    }

}

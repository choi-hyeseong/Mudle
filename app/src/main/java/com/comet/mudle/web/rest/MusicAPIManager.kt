package com.comet.mudle.web.rest

import android.util.Log
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.viewmodel.GameViewModel
import com.comet.mudle.web.rest.response.MusicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicAPIManager(private val gameViewModel: GameViewModel) {

    private val retrofit = DependencyUtil.retrofit
    private val musicAPI: MusicAPI by lazy { retrofit.create(MusicAPI::class.java) }

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

    fun getMusic() {
        musicAPI.getMusic().enqueue(object : Callback<MusicResponse> {
            override fun onResponse(call: Call<MusicResponse>, response: Response<MusicResponse>) {
                Log.i("asdf", "success")
                Log.i("asdf", response.body()?.content.toString())
                response.body()?.let {
                    gameViewModel.musicLiveData.postValue(Music(it.content))
                }

            }

            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                Log.i("asdf", "fail")
            }
        })
    }
}
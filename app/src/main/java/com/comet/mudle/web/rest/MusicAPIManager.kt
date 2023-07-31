package com.comet.mudle.web.rest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.web.rest.dto.UserDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.comet.mudle.web.rest.response.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class MusicAPIManager {

    private val retrofit = DependencyUtil.retrofit
    private val musicAPI: MusicAPI by lazy { retrofit.create(MusicAPI::class.java) }
    val userLiveData = MutableLiveData<UserResponseDTO>() //유저 응답값 저장후 코인 갱신 등등..
    val musicLiveData = MutableLiveData<Music>()
    val webResponse = MutableLiveData<String>()
    val registerResponse = MutableLiveData<Boolean>()

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

    fun getUser(uuid: UUID) {
        musicAPI.user(uuid).enqueue(object : Callback<ObjectResponse<UserResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<UserResponseDTO>>, response: Response<ObjectResponse<UserResponseDTO>>) {
                Log.i("asdf", "success")
                userLiveData.postValue(response.body()?.content)
            }

            override fun onFailure(call: Call<ObjectResponse<UserResponseDTO>>, t: Throwable) {
                webResponse.postValue("데이터 처리중 오류가 발생했습니다. 다시 시도하거나, 어플리케이션을 재설치 해주세요.")
                Log.i("asdf", "fail")
            }
        })
    }

    fun getMusic() {
        musicAPI.getMusic().enqueue(object : Callback<ObjectResponse<MusicResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<MusicResponseDTO>>, response: Response<ObjectResponse<MusicResponseDTO>>) {
                Log.i("asdf", "success")
                Log.i("asdf", response.body()?.content.toString())
                response.body()?.let {
                    musicLiveData.postValue(Music(it.content))
                }

            }

            override fun onFailure(call: Call<ObjectResponse<MusicResponseDTO>>, t: Throwable) {
                Log.i("asdf", "fail")
            }
        })
    }

    fun register(name : String, uuid: UUID) {
        musicAPI.register(UserDTO(name, uuid)).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                Log.w("mudle.log", response.toString())
                webResponse.postValue("회원가입이 성공하였습니다.")
                registerResponse.postValue(true)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                registerResponse.postValue(false)
                webResponse.postValue("an error occurred..")
                t.message?.let { Log.w("mudle.log", it) }
            }
        })
    }
}
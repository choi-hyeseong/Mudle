package com.comet.mudle.web.rest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.model.ServerUser
import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.comet.mudle.web.rest.response.DefaultResponse
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class MudleAPIManager {

    private val retrofit = DependencyUtil.retrofit
    private val musicAPI: MudleAPI by lazy { retrofit.create(MudleAPI::class.java) }
    private val userAPI : MudleUserAPI by lazy { retrofit.create(MudleUserAPI::class.java) }
    val userLiveData = MutableLiveData<ServerUser>() //유저 응답값 저장후 코인 갱신 등등..
    val musicLiveData = MutableLiveData<Music>()
    val webResponse = MutableLiveData<String>()

    fun request(uuid: UUID, music: String) {
        musicAPI.requestMusic(MusicRequestDTO(uuid, music)).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    webResponse.postValue(response.body()?.message)
                    //데이터 갱신
                    getUser(uuid)
                }
                else
                    webResponse.postValue(ObjectMapper().readValue(response.errorBody()?.string(), DefaultResponse::class.java).message)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                webResponse.postValue("음악 요청중 문제가 발생했습니다. 다시 시도해주세요.")
            }
        })
    }

    fun getUser(uuid: UUID) {
        userAPI.user(uuid).enqueue(object : Callback<ObjectResponse<UserResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<UserResponseDTO>>, response: Response<ObjectResponse<UserResponseDTO>>) {
                Log.i("asdf", "success")
                if (response.isSuccessful)
                    userLiveData.postValue(response.body()?.content?.let { ServerUser(it) })
                else
                    webResponse.postValue("데이터 처리중 오류가 발생했습니다. 다시 시도하거나, 어플리케이션을 재설치 해주세요.")
            }

            override fun onFailure(call: Call<ObjectResponse<UserResponseDTO>>, t: Throwable) {
                webResponse.postValue("서버와의 통신중 문제가 발생했습니다.")
                Log.i("asdf", "fail")
            }
        })
    }

    fun getMusic() {
        musicAPI.getMusic().enqueue(object : Callback<ObjectResponse<MusicResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<MusicResponseDTO>>, response: Response<ObjectResponse<MusicResponseDTO>>) {
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


    //livedata를 return하게 해서 필요할때 사용할 수 있게.
    //서버 response를 livedata에 띄워주는것도 괜찮을듯?
    fun register(name : String, uuid: UUID) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        userAPI.register(UserRequestDTO(name, uuid)).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                Log.w("mudle.log", response.toString())
                if (response.isSuccessful) {
                    webResponse.postValue("회원가입이 성공하였습니다.")
                    result.postValue(true)
                }
                else
                    webResponse.postValue("회원가입 요청중 문제가 발생했습니다.")
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                webResponse.postValue("an error occurred..")
                t.message?.let { Log.w("mudle.log", it) }
                result.postValue(false)
            }
        })
        return result
    }
}
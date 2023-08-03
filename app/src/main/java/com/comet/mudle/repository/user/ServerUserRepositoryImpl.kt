package com.comet.mudle.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.ResponseLiveDataHolder

import com.comet.mudle.repository.user.dao.MudleUserAPI
import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ServerUserRepositoryImpl(
    private val userAPI : MudleUserAPI
) : ServerUserRepository, ResponseLiveDataHolder {

    private val serverUserLiveData : MutableLiveData<ServerUser> = MutableLiveData()
    private val responseLiveData : MutableLiveData<String> = MutableLiveData()



    override fun getUser(uuid : UUID): LiveData<ServerUser> {
        renewUser(uuid)
        return serverUserLiveData
    }

    override fun register(name : String, uuid : UUID): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        userAPI.register(UserRequestDTO(name, uuid)).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                Log.w("mudle.log", response.toString())
                if (response.isSuccessful) {
                    responseLiveData.postValue("회원가입이 성공하였습니다.")
                    result.postValue(true)
                }
                else
                    responseLiveData.postValue("회원가입 요청중 문제가 발생했습니다.")
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                responseLiveData.postValue("an error occurred..")
                t.message?.let { Log.w("mudle.log", it) }
                result.postValue(false)
            }
        })
        return result
    }

    override fun renewUser(uuid: UUID) {
        Log.w("adsf", "${(serverUserLiveData as Any).toString()}")
        userAPI.user(uuid).enqueue(object : Callback<ObjectResponse<UserResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<UserResponseDTO>>, response: Response<ObjectResponse<UserResponseDTO>>) {
                Log.i("asdf", "success")
                if (response.isSuccessful)
                    serverUserLiveData.value = response.body()?.content?.let { ServerUser(it) }
                else
                    responseLiveData.postValue("데이터 처리중 오류가 발생했습니다. 다시 시도하거나, 어플리케이션을 재설치 해주세요.")
            }

            override fun onFailure(call: Call<ObjectResponse<UserResponseDTO>>, t: Throwable) {
                responseLiveData.postValue("서버와의 통신중 문제가 발생했습니다.")
                Log.i("asdf", "fail")
            }
        })
    }

    override fun getResponseLiveData(): LiveData<String> {
        return responseLiveData
    }
}
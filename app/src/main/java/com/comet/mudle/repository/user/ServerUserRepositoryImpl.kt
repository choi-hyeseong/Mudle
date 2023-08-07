package com.comet.mudle.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.LOG
import com.comet.mudle.model.ServerUser
import com.comet.mudle.repository.ResponseLiveDataHolder

import com.comet.mudle.repository.user.dao.MudleUserAPI
import com.comet.mudle.web.rest.dto.UserRequestDTO
import com.comet.mudle.web.rest.dto.UserResponseDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.ObjectResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.isSuccess
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ServerUserRepositoryImpl(
    private val userAPI: MudleUserAPI
) : ServerUserRepository, ResponseLiveDataHolder {

    private val serverUserLiveData: MutableLiveData<ServerUser> = MutableLiveData()
    private val responseLiveData: MutableLiveData<String> = MutableLiveData()

    //livedata는 나중에 post되도 되니까 일단 non suspend fun으로
    override fun getUser(uuid: UUID): LiveData<ServerUser> {
        CoroutineScope(Dispatchers.IO).launch { renewUser(uuid) }
        return serverUserLiveData
    }

    //suspend로 바꾸고 livedata 리턴해도 현재방식이랑 다를건 없음.
    override fun register(name: String, uuid: UUID): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        userAPI.register(UserRequestDTO(name, uuid)).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>) {
                Log.w(LOG, response.toString())
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

    override suspend fun renewUser(uuid: UUID) {
        val result: ApiResponse<ObjectResponse<UserResponseDTO>> = userAPI.user(uuid)
        result.onSuccess {
            serverUserLiveData.postValue(ServerUser(data.content))

        }
            .onError {
                if (statusCode == StatusCode.BadRequest)
                    responseLiveData.postValue("데이터 처리중 오류가 발생했습니다. 다시 시도하거나, 어플리케이션을 재설치 해주세요.")
                else
                    responseLiveData.postValue("잠시후 다시 시도해주세요.")
            }

    }


    override fun getResponseLiveData(): LiveData<String> {
        return responseLiveData
    }

}

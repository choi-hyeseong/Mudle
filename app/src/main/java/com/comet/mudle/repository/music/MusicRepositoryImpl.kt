package com.comet.mudle.repository.music

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.LOG
import com.comet.mudle.model.Music
import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
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
import retrofit2.Retrofit
import java.util.UUID
import java.util.concurrent.CompletableFuture

class MusicRepositoryImpl(
    private val musicAPI: MudleMusicAPI
) : MusicRepository {

    private val musicLiveData: MutableLiveData<Music> = MutableLiveData()
    private val responseLiveData: MutableLiveData<String> = MutableLiveData()
    private val objectMapper : ObjectMapper = ObjectMapper()

    override fun getMusic(): LiveData<Music> {
        CoroutineScope(Dispatchers.IO).launch { renewMusic() }
        return musicLiveData //livedata 리턴해도 처리이후 post 해줌
    }

    override fun getResponseLiveData(): LiveData<String> {
        return responseLiveData
    }

    override suspend fun requestMusic(uuid: UUID, music: String) {
        val response: ApiResponse<DefaultResponse> =
            musicAPI.requestMusic(MusicRequestDTO(uuid, music))
        response.onSuccess {
            if (isSuccess)
                responseLiveData.postValue(data.message)
        }
            .onError {
                if (statusCode == StatusCode.BadRequest)
                    responseLiveData.postValue(objectMapper.readValue(errorBody?.string(), DefaultResponse::class.java).message)
                else
                    responseLiveData.postValue("데이터 요청중 오류가 발생했습니다. 다시 시도해주세요.")
            }

    }

    override suspend fun renewMusic() {
        val response: ApiResponse<ObjectResponse<MusicResponseDTO>> = musicAPI.getMusic()
        response.onSuccess {
            if (isSuccess) {
                Log.i(LOG, data.content.toString())
                data.let {
                    musicLiveData.postValue(Music(it.content))
                }
            }
            else
                Log.i(LOG, "fail")
        }.onError {
            Log.i(LOG, "fail")
        }


    }
}

package com.comet.mudle.repository.music

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comet.mudle.DependencyUtil
import com.comet.mudle.model.Music
import com.comet.mudle.repository.user.dao.MudleMusicAPI
import com.comet.mudle.web.rest.dto.MusicRequestDTO
import com.comet.mudle.web.rest.response.DefaultResponse
import com.comet.mudle.web.rest.response.MusicResponseDTO
import com.comet.mudle.web.rest.response.ObjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
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

    override fun getMusic(): LiveData<Music> {
        renewMusic()
        return musicLiveData
    }

    override fun getResponseLiveData(): LiveData<String> {
        return responseLiveData
    }

    //callback이 왔을때 then이후 처리할 수 있게 future로
    override fun requestMusic(uuid: UUID, music: String): CompletableFuture<Boolean> {
        val result: CompletableFuture<Boolean> = CompletableFuture()
        musicAPI.requestMusic(MusicRequestDTO(uuid, music)).enqueue(object :
            Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    responseLiveData.postValue(response.body()?.message)
                    //데이터 갱신
                }
                else
                    responseLiveData.postValue(
                        ObjectMapper().readValue(
                            response.errorBody()?.string(), DefaultResponse::class.java).message)
                result.complete(true)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                responseLiveData.postValue("음악 요청중 문제가 발생했습니다. 다시 시도해주세요.")
                result.complete(false)
            }
        })
        return result
    }

    override fun renewMusic() {
        musicAPI.getMusic().enqueue(object : Callback<ObjectResponse<MusicResponseDTO>> {
            override fun onResponse(call: Call<ObjectResponse<MusicResponseDTO>>,
                                    response: Response<ObjectResponse<MusicResponseDTO>>) {
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
}
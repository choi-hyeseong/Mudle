package com.comet.mudle.service

import androidx.lifecycle.LiveData
import com.comet.mudle.model.Music
import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.music.MusicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

//후우.. 다른 reference 사용해놓고 livedata를 왜 괴롭히니.................
class MusicService(
    private val serverUserService: ServerUserService,
    private val musicRepository: MusicRepository
) {

    fun getMusic(): LiveData<Music> {
        return musicRepository.getMusic()
    }

    //이게 좀더 코드상으로 맞을듯. 리턴값이 없으면 suspend로 묶음
    suspend fun request(uuid: UUID, music: String) {
        musicRepository.requestMusic(uuid, music)
        serverUserService.renewUser(uuid)
    }

    //get같이 즉시 리턴이 필요한거는 non suspend, renew는 suspend로
    suspend fun renewMusic() {
        musicRepository.renewMusic()

    }

    fun getResponseLiveData(): LiveData<String> {
        return musicRepository.getResponseLiveData()
    }

}
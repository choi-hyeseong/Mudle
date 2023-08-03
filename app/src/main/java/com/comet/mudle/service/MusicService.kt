package com.comet.mudle.service

import androidx.lifecycle.LiveData
import com.comet.mudle.model.Music
import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.music.MusicRepositoryImpl
import java.util.UUID

//후우.. 다른 reference 사용해놓고 livedata를 왜 괴롭히니.................
class MusicService (
    private val serverUserService: ServerUserService,
    private val musicRepository: MusicRepository
) {

    fun getMusic(): LiveData<Music> {
        return musicRepository.getMusic()
    }

    fun request(uuid: UUID, music: String) {
        musicRepository.requestMusic(uuid, music).thenAccept { isSuccess ->
            if (isSuccess)
                serverUserService.renewUser(uuid)
        }
    }

    fun renewMusic() {
        musicRepository.renewMusic()
    }

    fun getResponseLiveData(): LiveData<String> {
        return musicRepository.getResponseLiveData()
    }

}
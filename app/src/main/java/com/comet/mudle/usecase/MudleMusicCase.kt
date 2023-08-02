package com.comet.mudle.usecase

import androidx.lifecycle.LiveData
import com.comet.mudle.model.Music
import com.comet.mudle.repository.music.MusicRepository
import com.comet.mudle.repository.music.MusicRepositoryImpl
import com.comet.mudle.repository.user.ServerUserRepository
import com.comet.mudle.repository.user.ServerUserRepositoryImpl
import java.util.UUID

//후우.. 다른 reference 사용해놓고 livedata를 왜 괴롭히니.................
class MudleMusicCase(private val mudleUserCase : MudleUserCase) {

    private val musicRepository : MusicRepository = MusicRepositoryImpl()

    fun getMusic() : LiveData<Music> {
        return musicRepository.getMusic()
    }

    fun request(uuid: UUID, music: String) {
        musicRepository.requestMusic(uuid, music).thenAccept { isSuccess ->
            if (isSuccess)
                mudleUserCase.renewUser(uuid)
        }
        //music repo에서 처리하는게 아니라 usecase에서 동시에 처리할 수 있게.
        // TODO 갱신 - 순차적으로 처리하기.
    }

    fun renewMusic() {
        musicRepository.renewMusic()
    }

    fun getResponseLiveData() : LiveData<String> {
        return musicRepository.getResponseLiveData()
    }

}
package com.comet.mudle

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.databinding.GameMainBinding
import com.comet.mudle.databinding.RegisterMainBinding
import com.comet.mudle.model.Chat
import com.comet.mudle.model.Music
import com.comet.mudle.recycler.ChatAdapter
import com.comet.mudle.recycler.ChatDecoration
import com.comet.mudle.type.MessageType
import com.comet.mudle.viewmodel.GameViewModel
import com.comet.mudle.viewmodel.RegisterViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GameFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var viewModel: GameViewModel
    private lateinit var player: YouTubePlayer
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // TODO databinding

        var currentMusic: Music? = null
        val binding =
            DataBindingUtil.inflate<GameMainBinding>(inflater, R.layout.game_main, container, false)

        //viewmodel init
        binding.viewModel = ViewModelProvider(this)[GameViewModel::class.java].apply {
            stompManager.chatLiveData.observe(viewLifecycleOwner) { chats ->
                updateList(chats)
            }
            stompManager.serverStatLiveData.observe(viewLifecycleOwner) { serverStatus ->
                if (serverStatus) binding.serverStat.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.server_online))
                else binding.serverStat.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.server_offline))

            }
            musicLiveData.observe(viewLifecycleOwner) { music ->
                currentMusic = music
                loadVideo(music)

            }
            stompManager.connect()
        }
        viewModel = binding.viewModel!!

        // Youtube init
        binding.youtube.let {
            lifecycle.addObserver(it)
            it.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                //추상클래스는 람다로 못바꿈
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    player = youTubePlayer
                    currentMusic?.let { music -> loadVideo(music) }
                }

                override fun onError(youTubePlayer: YouTubePlayer,
                                     error: PlayerConstants.PlayerError) {
                    binding.main.text = "대기중"
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer,
                                           state: PlayerConstants.PlayerState) {
                    Log.i("asdf", state.toString())
                    if (state == PlayerConstants.PlayerState.ENDED) binding.main.text = "대기중"
                    else if (state == PlayerConstants.PlayerState.PLAYING) binding.main.text = "재생중"
                }
            })
        }

        //background image view init
        Glide.with(this).load(R.drawable.background).fitCenter().into(binding.background)

        //button init
        binding.request.setOnClickListener {
            val edit = EditText(requireContext())
            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.request_title))
                setView(edit)
                setIcon(R.drawable.ic_launcher_foreground)
                setPositiveButton(R.string.request_button) { _, _ ->
                    Toast.makeText(
                        requireContext(), "신청 완료 " + edit.text.toString() + "", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.request(edit.text.toString().trim())
                }
                setNegativeButton(getString(R.string.request_deny)) {dialog, _ ->
                    dialog.cancel()
                }
                create()
                show()
            }

        }


        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        binding.messageInput.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) sendMessage()
            true
        }

        recyclerView = binding.recyclerView.apply {
            addItemDecoration(ChatDecoration())
            setHasFixedSize(true) //recycler view 크기는 고정, 비싸지 않게
        }

        return binding.root
    }

    private fun sendMessage() {
        val message = inputMessage.text.toString().trim()
        viewModel.stompManager.send(message)
        inputMessage.text.clear()
    }

    private fun updateList(list: List<Chat>) {
        val adapter = ChatAdapter(list)
        recyclerView.let {
            it.adapter = adapter
            it.scrollToPosition(list.size - 1)
        }
    }

    private fun loadVideo(music: Music) {
        if (::player.isInitialized)
            player.loadVideo(
                music.link,
                ((System.currentTimeMillis() - music.startTime) / 1000).toFloat())
    }
}
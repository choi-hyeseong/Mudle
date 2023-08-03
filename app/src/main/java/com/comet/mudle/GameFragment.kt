package com.comet.mudle

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.databinding.GameMainBinding
import com.comet.mudle.model.Chat
import com.comet.mudle.model.Music
import com.comet.mudle.recycler.ChatAdapter
import com.comet.mudle.recycler.ChatDecoration
import com.comet.mudle.viewmodel.GameViewModel
import com.comet.mudle.web.stomp.StompService
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GameFragment : Fragment() {

    //왜 DataBinding해놓고  lateinit을 하느냐 -> Binding lateinit 할당시 Memory leak 문제가 발생할 수 있음
    /*
    binding in fragments could lead to memory leaks, if they aren't set to null in onDestroyView. That's why we set _binding to null in onDestroyView
    그래서 lazy써서.. viewmodel 참조 가지고.. lifecycle leak 안날라나 모르겠다. 필요시 on destroy에서 null refer하면 될듯?
     */
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var player: YouTubePlayer
    private lateinit var playStatusText: TextView
    private lateinit var serverStatImage : ImageView
    private lateinit var coinText : TextView
    //viewmodel init
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }
    private var currentMusic: Music? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {



        val binding =
            DataBindingUtil.inflate<GameMainBinding>(inflater, R.layout.game_main, container, false)



        initLiveData(viewModel)

        // lateinit
        inputMessage = binding.messageInput
        playStatusText = binding.main
        serverStatImage = binding.serverStat
        coinText = binding.coin
        binding.viewModel = viewModel

        // Youtube init
        initYoutube(binding.youtube)


        //background image view init
        Glide.with(this).load(R.drawable.background).fitCenter().into(binding.background)

        //button init
        binding.request.setOnClickListener {
            setRequestButtonListener(viewModel)
        }

        binding.sendButton.setOnClickListener {
            sendMessage(viewModel.stompService)
        }

        binding.messageInput.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) sendMessage(viewModel.stompService)
            true
        }

        recyclerView = binding.recyclerView.apply {
            addItemDecoration(ChatDecoration())
            setHasFixedSize(true) //recycler view 크기는 고정, 비싸지 않게
        }

        //connect
        binding.viewModel?.let { it.stompService.connect() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.renewMusic()
    }

    private fun setRequestButtonListener(viewModel : GameViewModel) {
        val edit = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_TEXT
        }
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.request_title))
            setView(edit)
            setIcon(R.drawable.music)
            setPositiveButton(R.string.request_button) { _, _ ->
                viewModel.request(edit.text.toString().trim())
            }
            setNegativeButton(getString(R.string.request_deny)) { dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }
    private fun initYoutube(youTubePlayer: YouTubePlayerView) {
        youTubePlayer.let {
            lifecycle.addObserver(it)
            it.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                //추상클래스는 람다로 못바꿈
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    player = youTubePlayer
                    currentMusic?.let { music -> loadVideo(music) }
                }

                override fun onError(youTubePlayer: YouTubePlayer,
                                     error: PlayerConstants.PlayerError) {
                    playStatusText.text = "대기중"
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer,
                                           state: PlayerConstants.PlayerState) {
                    Log.i("asdf", state.toString())
                    if (state == PlayerConstants.PlayerState.ENDED) playStatusText.text = "대기중"
                    else if (state == PlayerConstants.PlayerState.PLAYING) playStatusText.text = "재생중"
                }
            })
        }
    }

    private fun initLiveData(viewModel: GameViewModel) {
        viewModel.apply {
            stompService.chatLiveData.observe(viewLifecycleOwner) { chats ->
                updateList(chats)
            }
            stompService.serverStatLiveData.observe(viewLifecycleOwner) { serverStatus ->
                if (serverStatus) serverStatImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.server_online))
                else serverStatImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.server_offline))

            }

            musicLiveData.observe(viewLifecycleOwner) { music ->
                currentMusic = music
                loadVideo(music)
            }

            musicResponseLiveData.observe(viewLifecycleOwner) { response ->
                Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
            }

            userResponseLiveData.observe(viewLifecycleOwner) { response ->
                Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
            }

            //user observe
            userLiveData.observe(viewLifecycleOwner) { user ->
                Log.w("adsf", "call user update")
                //getString format 사용하기
                coinText.text = getString(R.string.coin_format, user.coin)
            }
        }
    }

    private fun sendMessage(stompService: StompService) {
        val message = inputMessage.text.toString().trim()
        stompService.send(message)
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
        Log.i("log"," ${music.currentTime}")
        if (::player.isInitialized)
            player.loadVideo(
                music.link,
                music.currentTime.toFloat())
    }
}
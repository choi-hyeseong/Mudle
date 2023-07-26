package com.comet.mudle

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.model.Chat
import com.comet.mudle.viewmodel.GameViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GameFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage : EditText
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO databinding
        val view = inflater.inflate(R.layout.game_main, container, false)
        val youtube = view.findViewById<YouTubePlayerView>(R.id.youtube)
        val text = view.findViewById<TextView>(R.id.main)
        val request = view.findViewById<Button>(R.id.request)
        val sendButton = view.findViewById<Button>(R.id.sendButton)
        inputMessage = view.findViewById(R.id.messageInput)
        val serverStatImage = view.findViewById<ImageView>(R.id.serverStat)
        var player: YouTubePlayer? = null
        lifecycle.addObserver(youtube)
        youtube.apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                //추상클래스는 람다로 못바꿈
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("6ZUIwj3FgUY", 0.0f)
                    text?.text = "재생중"
                    player = youTubePlayer
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    if (state == PlayerConstants.PlayerState.ENDED)
                        text?.text = "대기중"
                }
            })
        }
        view.findViewById<ImageView>(R.id.background)?.let {
            Glide.with(this).load(R.drawable.background).fitCenter().into(it)
        }

        request.setOnClickListener {
            val edit = EditText(requireContext())
            AlertDialog.Builder(requireContext()).setTitle(getString(R.string.request_title))
                .setView(edit).setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton(R.string.request_button) { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        "신청 완료 " + edit.text.toString() + "",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.request(edit.text.toString().trim())
                }.show()
        }
        viewModel = ViewModelProvider(this)[GameViewModel::class.java].let { it ->
            val stompManager = it.stompManager
            stompManager.chatLiveData.observe(viewLifecycleOwner) { chats ->
                updateList(chats)
            }
            stompManager.serverStatLiveData.observe(viewLifecycleOwner) {serverStatus ->
                if (serverStatus)
                    serverStatImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.server_online
                        )
                    )
                else
                    serverStatImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.server_offline
                        )
                    )

            }
            stompManager.youtubeRequestLiveData.observe(viewLifecycleOwner) { link ->
                player?.loadVideo(link, 0.0f)
            }
            stompManager.connect()
            it
        }
        sendButton.setOnClickListener {
           sendMessage()
        }
        inputMessage.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN)
                sendMessage()
            false
        }
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            addItemDecoration(ChatDecoration())
            setHasFixedSize(true) //recycler view 크기는 고정, 비싸지 않게
        }
        return view
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

    private inner class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        //실질적 홀더
        private val sender: TextView = view.findViewById(R.id.sender)
        private val content: TextView = view.findViewById(R.id.contentText)

        fun bind(chat: Chat) {
            sender.text = chat.sender
            content.text = chat.message
        }
    }

    private inner class ChatAdapter(val chats: List<Chat>) : RecyclerView.Adapter<ChatHolder>() {
        //홀더가 사용할 수 있게 설정하는 부분
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
            val view = layoutInflater.inflate(R.layout.layout_chat, parent, false)
            return ChatHolder(view)
        }

        override fun getItemCount(): Int {
            return chats.size
        }

        override fun onBindViewHolder(holder: ChatHolder, position: Int) {
            holder.bind(chats[position])
        }

    }

    private inner class ChatDecoration : RecyclerView.ItemDecoration() {

        private val padding = 10;

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = padding
            outRect.left = padding
            outRect.bottom = padding
            outRect.right = padding
        }
    }
}
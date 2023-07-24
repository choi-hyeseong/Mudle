package com.comet.mudle

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.dao.PrefUserDao
import com.comet.mudle.model.Chat
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import okhttp3.OkHttpClient

class GameFragment : Fragment() {

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
            })
        }
        view.findViewById<ImageView>(R.id.background)?.let {
            Glide.with(this).load(R.drawable.background).fitCenter().into(it)
        }
        val list = listOf(
            Chat("혜성", "ㅎㅇ"),
            Chat("시럽", "ㅂㅇ"),
            Chat("야옹", "대충 엄청나게 긴 메시지........."),
            Chat("혜성", "ㅎㅇ"),
            Chat("시럽", "ㅂㅇ"),
            Chat("야옹", "대충 엄청나게 긴 메시지........."),
            Chat("혜성", "ㅎㅇ"),
            Chat("시럽", "ㅂㅇ"),
            Chat("야옹", "대충 엄청나게 긴 메시지........."),
            Chat("혜성", "ㅎㅇ"),
            Chat("시럽", "ㅂㅇ"),
            Chat("야옹", "대충 엄청나게 긴 메시지........."),
            Chat("혜성", "ㅎㅇ"),
            Chat("시럽", "ㅂㅇ"),
            Chat("야옹", "대충 엄청나게 긴 메시지.........")
        )
        val adapter = ChatAdapter(list)
        view.findViewById<RecyclerView>(R.id.recyclerView).let {
            it.adapter = adapter
            it.addItemDecoration(ChatDecoration())
            it.scrollToPosition(list.size - 1)
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
                    player?.loadVideo(edit.text.toString(), 0.0f)
                }.show()
        }
        Thread {
            val url = "ws://192.168.219.106:8080/ws"
            val intervalMillis = 1000L
            val client = OkHttpClient()
            val stomp = StompClient(client, intervalMillis).let {
                it.url = url
                it
            }
            val connection = stomp.connect().subscribe {
                Log.i("asd", "${it.type}")
            }

            val topic = stomp.join("/sub/message").subscribe {
                Log.w("asdf", it.toString())
            }

            stomp.send("/pub/message", "{\"type\":\"SYSTEM\", \"sender\" : \"Snow\", \"content\":\"test\"}").subscribe()

        }.start()
        return view
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
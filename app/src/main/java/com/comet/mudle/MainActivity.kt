package com.comet.mudle

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.dao.PrefUserDao
import com.comet.mudle.dao.UserDao
import com.comet.mudle.model.Chat
import com.comet.mudle.model.User
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.UUID


class MainActivity : AppCompatActivity(), ActivityCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<YouTubePlayerView>(R.id.youtube)
        val text = findViewById<TextView>(R.id.main)
        lifecycle.addObserver(view)
        val userDao = PrefUserDao(getPreferences(MODE_PRIVATE))
        view.apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                //추상클래스는 람다로 못바꿈
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("6ZUIwj3FgUY", 0.0f)
                    text?.text = "재생중"
                }
            })
        }
        findViewById<ImageView>(R.id.background)?.let {
            Glide.with(this).load(R.drawable.background).fitCenter().into(it)
        }
        val list =listOf(
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
        findViewById<RecyclerView>(R.id.recyclerView).let {
            it.adapter = adapter
            it.addItemDecoration(ChatDecoration())
            it.scrollToPosition(list.size - 1)
        }

    }

    override fun switchRegister() {
        TODO("Not yet implemented")
    }

    override fun switchMain() {
        //TODO
        supportFragmentManager.beginTransaction().addToBackStack(null)
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
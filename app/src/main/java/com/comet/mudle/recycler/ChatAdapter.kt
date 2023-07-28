package com.comet.mudle.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comet.mudle.R
import com.comet.mudle.model.Chat

class ChatAdapter(val chats: List<Chat>) : RecyclerView.Adapter<ChatHolder>() {
    //홀더가 사용할 수 있게 설정하는 부분
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_chat, parent, false)
        return ChatHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(chats[position])
    }

}
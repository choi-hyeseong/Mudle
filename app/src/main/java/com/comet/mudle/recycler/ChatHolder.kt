package com.comet.mudle.recycler

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.comet.mudle.R
import com.comet.mudle.model.Chat
import com.comet.mudle.type.MessageType

class ChatHolder(val view: View) : RecyclerView.ViewHolder(view) {
    //실질적 홀더
    private val sender: TextView = view.findViewById(R.id.sender)
    private val content: TextView = view.findViewById(R.id.contentText)
    private val layout : LinearLayout = view.findViewById(R.id.bubble_background)

    fun bind(chat: Chat) {
        sender.text = chat.sender
        content.text = chat.message
        if (chat.type == MessageType.ALERT) {
            layout.background =
                AppCompatResources.getDrawable(view.context, R.drawable.bubble_system)
            sender.setTextColor(Color.WHITE)
            content.setTextColor(Color.WHITE)
        }
    }
}
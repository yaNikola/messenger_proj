package com.example.telegram.ui.screens.groups

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.ui.message_recycler_view.view_holders.*
import com.example.telegram.ui.message_recycler_view.views.MessageView

class GroupChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCash = mutableListOf<MessageView>()
    private val mListHolder = mutableListOf<MessageHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCash[position].getTypeView()
    }

    override fun getItemCount(): Int = mListMessagesCash.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(mListMessagesCash[position], TYPE_GROUP)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(mListMessagesCash[holder.adapterPosition])
        mListHolder.add((holder as MessageHolder))
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDetach()
        mListHolder.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
    }

    fun addItemToBottom(item: MessageView, onSuccess: () -> Unit) {
        if (!mListMessagesCash.contains(item)) {
            mListMessagesCash.add(item)
            notifyItemInserted(mListMessagesCash.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: MessageView, onSuccess: () -> Unit) {
        if (!mListMessagesCash.contains(item)) {
            mListMessagesCash.add(item)
            mListMessagesCash.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
            onSuccess()
        }
    }

    fun onDestroy() {
        mListHolder.forEach {
            it.onDetach()
        }
    }
}




package com.example.interfaces.main

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interfaces.R
import com.example.interfaces.Session
import com.example.interfaces.Utils
import com.example.interfaces.databinding.UserContentItemBinding
import com.example.interfaces.double_click_lib.DoubleClick
import com.example.interfaces.double_click_lib.DoubleClickListener
import com.example.interfaces.models.Content
import java.util.*

class UserContentItemAdapter(): RecyclerView.Adapter<UserContentItemAdapter.ViewHolder>() {
    private var content = listOf<Content>()

    var doOnItemClick: ((Content) -> Unit)? = null
    var doOnItemDoubleClick: ((Content) -> Unit)? = null

    fun setDataSource(content: List<Content>) {
        this.content = content
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() =
        content.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: UserContentItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        private fun isInFavourite(contentItem: Content): Boolean {
            return Session.currentUser?.likes?.map {it.id}?.contains(contentItem.id) ?: false
        }

        fun bind() {
            val contentItem = content[absoluteAdapterPosition]

            binding.titleTv.text = contentItem.name
            Utils.loadImage(binding.logoIv, pictureUrl = contentItem.imgUrl)

            binding.favouriteIv.setImageDrawable(
                context.getDrawable(
                    if (isInFavourite(contentItem)) R.drawable.ic_star
                    else R.drawable.ic_star_empty
                )
            )

            binding.root.setOnClickListener(
                DoubleClick(object : DoubleClickListener {
                    override fun onSingleClickEvent(view: View?) {
                        doOnItemClick?.invoke(contentItem)
                        notifyItemChanged(absoluteAdapterPosition)
                    }

                    override fun onDoubleClickEvent(view: View?) {
                        doOnItemDoubleClick?.invoke(contentItem)
                        notifyItemChanged(absoluteAdapterPosition)
                    }

                })
            )
        }
    }
}
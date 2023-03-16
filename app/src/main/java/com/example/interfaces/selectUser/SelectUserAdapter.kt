package com.example.interfaces.selectUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.interfaces.databinding.SelectUserItemBinding
import com.example.interfaces.models.User

class SelectUserAdapter: RecyclerView.Adapter<SelectUserAdapter.ViewHolder>() {
    private var users = listOf<User>()

    var doOnItemClick: ((User) -> Unit)? = null

    fun setDataSource(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() =
        users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: SelectUserItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val user = users[absoluteAdapterPosition]

            binding.name.text = user.name

            binding.divider.isVisible = absoluteAdapterPosition != users.size

            binding.root.setOnClickListener {
                doOnItemClick?.invoke(user)
            }
        }
    }
}
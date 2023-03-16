package com.example.interfaces.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interfaces.R
import com.example.interfaces.category.CategoryFragment
import com.example.interfaces.databinding.UserContentFavouritesItemBinding
import com.example.interfaces.databinding.UserContentGamesItemBinding
import com.example.interfaces.databinding.UserContentMusicItemBinding
import com.example.interfaces.databinding.UserContentSitesItemBinding
import com.example.interfaces.databinding.UserContentTvChannelsItemBinding
import com.example.interfaces.models.Content
import com.example.interfaces.models.ContentType
import com.example.interfaces.models.User

class UserContentAdapter(
    private val navController: NavController,
    private val user: User?
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Cell(val id: Int, val type: ContentType, val title: String, val content: List<Content>)

    private var cells = listOf<Cell>()

    var doOnItemClick: ((Content) -> Unit)? = null
    var doOnItemDoubleClick: ((Content) -> Unit)? = null

    fun setDataSource(cells: List<Cell>) {
        this.cells = cells
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ContentType.GAME.value -> {
                val binding = UserContentGamesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return GamesViewHolder(binding)
            }

            ContentType.TV_CHANNEL.value -> {
                val binding = UserContentTvChannelsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return TvProgramsViewHolder(binding)
            }

            ContentType.SITE.value -> {
                val binding = UserContentSitesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SitesViewHolder(binding)
            }

            ContentType.MUSIC.value -> {
                val binding = UserContentMusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MusicViewHolder(binding)
            }

            ContentType.FAVOURITES.value -> {
                val binding = UserContentFavouritesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FavouritesViewHolder(binding)
            }

            else -> {
                Log.e(TAG, "onCreateViewHolder - wrong type")
                val binding = UserContentTvChannelsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return TvProgramsViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val cell = cells[position]
        return cell.type.value
    }

    override fun getItemCount() =
        cells.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            holder is TvProgramsViewHolder -> {
                holder.bind()
            }
            holder is GamesViewHolder -> {
                holder.bind()
            }
            holder is MusicViewHolder -> {
                holder.bind()
            }
            holder is SitesViewHolder -> {
                holder.bind()
            }
            holder is FavouritesViewHolder -> {
                holder.bind()
            }
            else -> {  }
        }
    }

    inner class TvProgramsViewHolder(private val binding: UserContentTvChannelsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val channelsAdapter = UserContentItemAdapter()

        private fun initAdapter() {
            channelsAdapter.doOnItemClick = doOnItemClick
            channelsAdapter.doOnItemDoubleClick = doOnItemDoubleClick
            binding.channelsRv.adapter = channelsAdapter
        }

        fun bind() {
            val cell = cells[absoluteAdapterPosition]

            initAdapter()

            binding.titleTv.text = cell.title
            channelsAdapter.setDataSource(cell.content)

            binding.viewAllBtn.isVisible = cell.content.size >= ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN

            binding.viewAllBtn.setOnClickListener {
                CategoryFragment.open(navController, user, ContentType.TV_CHANNEL)
            }
        }
    }

    inner class GamesViewHolder(private val binding: UserContentGamesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val gameAdapter = UserContentItemAdapter()

        private fun initAdapter() {
            gameAdapter.doOnItemClick = doOnItemClick
            gameAdapter.doOnItemDoubleClick = doOnItemDoubleClick
            binding.gamesRv.adapter = gameAdapter
            binding.gamesRv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        }

        fun bind() {
            val cell = cells[absoluteAdapterPosition]

            initAdapter()

            binding.titleTv.text = cell.title
            gameAdapter.setDataSource(cell.content)

            binding.viewAllBtn.isVisible = cell.content.size >= ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN * 2

            binding.viewAllBtn.setOnClickListener {
                CategoryFragment.open(navController, user, ContentType.GAME)
            }
        }
    }

    inner class MusicViewHolder(private val binding: UserContentMusicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val musicAdapter = UserContentItemAdapter()

        private fun initAdapter() {
            musicAdapter.doOnItemClick = doOnItemClick
            musicAdapter.doOnItemDoubleClick = doOnItemDoubleClick
            binding.musicRv.adapter = musicAdapter
        }

        fun bind() {
            val cell = cells[absoluteAdapterPosition]

            initAdapter()

            binding.titleTv.text = cell.title
            musicAdapter.setDataSource(cell.content)

            binding.viewAllBtn.isVisible = cell.content.size >= ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN

            binding.viewAllBtn.setOnClickListener {
                CategoryFragment.open(navController, user, ContentType.MUSIC)
            }
        }
    }

    inner class FavouritesViewHolder(private val binding: UserContentFavouritesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val favouritesAdapter = UserContentItemAdapter()

        private fun initAdapter() {
            favouritesAdapter.doOnItemClick = doOnItemClick
            favouritesAdapter.doOnItemDoubleClick = doOnItemDoubleClick
            binding.musicRv.adapter = favouritesAdapter
        }

        fun bind() {
            val cell = cells[absoluteAdapterPosition]

            initAdapter()

            binding.titleTv.text = cell.title
            favouritesAdapter.setDataSource(cell.content)

            binding.viewAllBtn.isVisible = cell.content.size >= ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN

            binding.viewAllBtn.setOnClickListener {
                CategoryFragment.open(navController, user, ContentType.FAVOURITES)
            }
        }
    }

    inner class SitesViewHolder(private val binding: UserContentSitesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val sitesAdapter = UserContentItemAdapter()

        private fun initAdapter() {
            sitesAdapter.doOnItemClick = doOnItemClick
            sitesAdapter.doOnItemDoubleClick = doOnItemDoubleClick
            binding.sitesRv.adapter = sitesAdapter
        }

        fun bind() {
            val cell = cells[absoluteAdapterPosition]

            initAdapter()

            binding.titleTv.text = cell.title
            sitesAdapter.setDataSource(cell.content)

            binding.viewAllBtn.isVisible = cell.content.size >= ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN

            binding.viewAllBtn.setOnClickListener {
                CategoryFragment.open(navController, user, ContentType.SITE)
            }
        }
    }

    companion object {
        private const val TAG = "UserContentAdapter"
        private const val ITEMS_COUNT_TO_SHOW_VIEW_ALL_BTN = 5
    }
}
package com.example.interfaces.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.interfaces.R
import com.example.interfaces.Utils
import com.example.interfaces.databinding.FragmentCategoryBinding
import com.example.interfaces.main.UserContentItemAdapter
import com.example.interfaces.models.ContentType
import com.example.interfaces.models.User

class CategoryFragment : Fragment() {
    private var binding: FragmentCategoryBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(CategoryViewModel::class.java) }
    private val contentAdapter by lazy { UserContentItemAdapter() }

    private var isRemouteActive = false
        set(value) {
            showOrHideRemoute(show = value)
            field = value
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillToolbar()
        initObservers()
        initAdapter()
        initRemoute()
        setOnClicks()

        viewModel.loadCategory()
    }

    private fun setOnClicks() {

    }

    private fun initObservers() {
        viewModel.categoryItemsL.observe(viewLifecycleOwner) {
            contentAdapter.setDataSource(it)
        }
    }

    private fun fillToolbar() {
        val titleRes = viewModel.contentType?.titleResId ?: return

        binding?.toolbar?.titleTv?.text = getString(titleRes)
        binding?.toolbar?.backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        binding?.itemsRv?.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding?.itemsRv?.adapter = contentAdapter
        contentAdapter.doOnItemClick = { content ->
            when (content.type) {
                ContentType.SITE -> {

                }

                ContentType.GAME -> {
                    Utils.showToast(requireContext(), getString(R.string.content_game_selected, content.name))
                }

                ContentType.TV_CHANNEL -> {

                }

                ContentType.MUSIC -> {

                }

                else -> {  }
            }
        }

        contentAdapter.doOnItemDoubleClick = { content ->
            viewModel.toFavourite(content)
        }
    }

    private fun showOrHideRemoute(show: Boolean) {
        binding?.let { b ->
            b.remouteView.channelDownBtn.isVisible = show
            b.remouteView.channelUpBtn.isVisible = show
            b.remouteView.volumeDownBtn.isVisible = show
            b.remouteView.volumeUpBtn.isVisible = show
            b.remouteView.muteBtn.isVisible = show
            b.remouteView.playPauseBtn.isVisible = show
            b.remouteView.powerBtn.isVisible = show
            b.remouteView.voiceBtn.isVisible = show

            b.remouteView.bcgView.isVisible = show
        }
    }

    private fun initRemoute() {
        showOrHideRemoute(show = isRemouteActive)

        binding?.remouteView?.bcgView?.setOnClickListener {
            isRemouteActive = false
        }

        binding?.remouteView?.remouteBtn?.setOnClickListener {
            isRemouteActive = !isRemouteActive
        }

        binding?.remouteView?.playPauseBtn?.setOnClickListener {
            binding?.remouteView?.playPauseBtn?.setImageResource(
                if (isPaused) R.drawable.ic_pause else R.drawable.ic_play
            )
            isPaused = !isPaused
        }
    }

    companion object {
        private const val TAG = "CategoryFragment"

        fun open(navController: NavController, user: User?, contentType: ContentType?) {
            CategoryViewModel.userInit = user
            CategoryViewModel.contentTypeInit = contentType

            navController.navigate(R.id.categoryFragment)
        }
    }
}
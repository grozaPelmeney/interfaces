package com.example.interfaces.main

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.interfaces.R
import com.example.interfaces.Session
import com.example.interfaces.Utils
import com.example.interfaces.databinding.FragmentUserContentBinding
import com.example.interfaces.models.ContentType
import com.example.interfaces.models.User
import com.example.interfaces.remoute.RemouteFragment
import com.example.interfaces.remoute.RemouteViewModel
import com.example.interfaces.selectUser.SelectUserFragment
import com.example.interfaces.settings.SettingsFragment
import com.example.interfaces.settings.SettingsViewModel
import com.example.interfaces.user.UserFragment
import com.example.interfaces.user.UserViewModel
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText

class UserContentFragment : Fragment() {
    private var binding: FragmentUserContentBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(UserContentViewModel::class.java) }
    private val contentAdapter by lazy { UserContentAdapter(findNavController(), Session.currentUser) }

    private var isRemouteActive = false
        set(value) {
            field = value
            showOrHideRemoute(show = value)
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initAdapter()
        initView()

        viewModel.loadUserData()
        viewModel.loadContent()
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

    private fun showOrHideLoading(show: Boolean) {
        binding?.loadingBar?.isVisible = show
        binding?.mainRv?.isVisible = !show

        if (!show) {
            binding?.swipeRefreshL?.isRefreshing = false
        }
    }

    private fun initView() {
        showOrHideLoading(show = true)

        binding?.swipeRefreshL?.setOnRefreshListener {
            viewModel.loadContent()
        }

        fillToolbar()
        setOnClicks()
        initRemoute()
    }

    private fun fillDrawer() {
        val slider = binding?.slider ?: return
        val user = viewModel.userL.value ?: return

        val menuItems =
            listOf(
                PrimaryDrawerItem().apply {
                    nameText = user.name
                    isSelectable = false
                    identifier = 0
                },
                DividerDrawerItem(),
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu1
                    iconRes = R.drawable.ic_menu_main
                    identifier = 1
                },
                DividerDrawerItem(),
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu2
                    iconRes = R.drawable.ic_menu_remote
                    identifier = 2
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu3
                    iconRes = R.drawable.ic_menu_settings
                    identifier = 3
                },
                DividerDrawerItem(),
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu4
                    iconRes = R.drawable.ic_menu_logout
                    identifier = 4
                },
            )

        slider.itemAdapter.set(menuItems)

        slider.onDrawerItemClickListener = { v, drawerItem, position ->
            when (drawerItem.identifier.toInt()) {
                0 -> {
                    UserFragment.open(findNavController(), user, isNewUser = false)
                }
                1 -> {
                    /* current */
                }
                2 -> {
                    RemouteFragment.open(findNavController(), user)
                }
                3 -> {
                    SettingsFragment.open(findNavController(), user)
                }
                4 -> {
                    SelectUserFragment.open(findNavController())
                }
                else -> {  }
            }
            /*return*/ false
        }
    }

    private fun setOnClicks() {
        //Переопределил поведение кнопки "назад", чтобы, когда меню открыто, нажатие "назад" закрывало меню, а не возвращало на пред.экран
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding?.slider?.let { slider ->
                    if (slider.drawerLayout?.isDrawerOpen(slider) == true) {
                        slider.drawerLayout?.close()
                    }
                    else {
                        //На главной, нажатие назад возвращает к выбору юзера
                        findNavController().popBackStack(R.id.selectUserFragment, false)
                    }
                }
            }
        })
    }

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.user_content_toolbar)
        binding?.toolbar?.menuBtn?.setOnClickListener {
            binding?.slider?.drawerLayout?.open()
        }
    }

    private fun getAdapterCells(): List<UserContentAdapter.Cell>? {
        val user = viewModel.userL.value ?: return null
        val contents = viewModel.contentsL.value ?: return null

        val cells = mutableListOf<UserContentAdapter.Cell>()

        val favouriteContent = user.likes
        if (favouriteContent.isNotEmpty()) {
            cells.add(
                UserContentAdapter.Cell(
                    id = 0,
                    type = ContentType.FAVOURITES,
                    title = getString(ContentType.FAVOURITES.titleResId),
                    content = favouriteContent
                )
            )
        }

        ContentType.values().forEachIndexed { i, type ->
            val content = contents.filter { it.type == type }
            if (content.isNotEmpty()) {
                cells.add(
                    UserContentAdapter.Cell(
                        id = i + 1,
                        type = type,
                        title = getString(type.titleResId),
                        content = content
                    )
                )
            }
        }

        return cells
    }

    private fun initObservers() {
        viewModel.userL.observe(viewLifecycleOwner) {
            fillDrawer()

            getAdapterCells()?.let { cells ->
                contentAdapter.setDataSource(cells)
            }
        }

        viewModel.contentsL.observe(viewLifecycleOwner) {
            showOrHideLoading(show = false)
            getAdapterCells()?.let { cells ->
                contentAdapter.setDataSource(cells)
            }
        }
    }

    private fun initAdapter() {
        binding?.mainRv?.adapter = contentAdapter

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

    companion object {
        private const val TAG = "UserContentFragment"

        fun open(navController: NavController) {
            navController.navigate(R.id.userContentFragment)
        }
    }
}
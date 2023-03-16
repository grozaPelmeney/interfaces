package com.example.interfaces.settings

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
import com.example.interfaces.databinding.FragmentSettingsBinding
import com.example.interfaces.main.UserContentFragment
import com.example.interfaces.models.User
import com.example.interfaces.remoute.RemouteFragment
import com.example.interfaces.remoute.RemouteViewModel
import com.example.interfaces.selectUser.SelectUserFragment
import com.example.interfaces.user.UserFragment
import com.example.interfaces.user.UserViewModel
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(SettingsViewModel::class.java) }
    private val settingsAdapter by lazy { SettingAdapter() }

    private var isRemouteActive = false
        set(value) {
            field = value
            showOrHideRemoute(show = value)
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillDrawer()
        fillToolbar()
        initObservers()
        initAdapter()
        initRemoute()
        setOnClicks()

        viewModel.loadSettings()
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
                        findNavController().popBackStack()
                    }
                }
            }
        })
    }

    private fun initObservers() {
        viewModel.settingsL.observe(viewLifecycleOwner) {
            settingsAdapter.setDataSource(it)
        }
    }

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.remoute_toolbar)
        binding?.toolbar?.menuBtn?.setOnClickListener {
            binding?.slider?.drawerLayout?.open()
        }
    }

    private fun initAdapter() {
        binding?.settingsRv?.adapter = settingsAdapter
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

    private fun fillDrawer() {
        val slider = binding?.slider ?: return
        val user = viewModel.user ?: return

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
                    UserContentFragment.open(findNavController())
                }
                2 -> {
                    RemouteFragment.open(findNavController(), user)
                }
                3 -> {
                    /* current */
                }
                4 -> {
                    SelectUserFragment.open(findNavController())
                }
                else -> {  }
            }
            /*return*/ false
        }
    }

    companion object {
        private const val TAG = "SettingsFragment"

        fun open(navController: NavController, user: User?) {
            SettingsViewModel.userInit = user
            navController.navigate(R.id.settingsFragment)
        }
    }
}
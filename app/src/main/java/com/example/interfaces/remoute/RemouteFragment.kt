package com.example.interfaces.remoute

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.interfaces.R
import com.example.interfaces.databinding.FragmentRemouteBinding
import com.example.interfaces.main.UserContentFragment
import com.example.interfaces.models.User
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

class RemouteFragment : Fragment() {
    private var binding: FragmentRemouteBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(RemouteViewModel::class.java) }

    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRemouteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillDrawer()
        fillToolbar()
        initRemoute()
        setOnClicks()
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

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.remoute_toolbar)
        binding?.toolbar?.menuBtn?.setOnClickListener {
            binding?.slider?.drawerLayout?.open()
        }
    }

    private fun initRemoute() {
        binding?.playPauseBtn?.setOnClickListener {
            binding?.playPauseBtn?.setImageResource(
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
                    /* current */
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

    companion object {
        private const val TAG = "RemouteFragment"

        fun open(navController: NavController, user: User?) {
            RemouteViewModel.userInit = user
            navController.navigate(R.id.remouteFragment)
        }
    }
}
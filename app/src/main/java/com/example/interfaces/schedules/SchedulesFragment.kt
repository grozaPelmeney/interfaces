package com.example.interfaces.schedules

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.interfaces.R
import com.example.interfaces.databinding.FragmentSchedulesBinding
import com.example.interfaces.main.UserContentFragment
import com.example.interfaces.models.User
import com.example.interfaces.remoute.RemouteFragment
import com.example.interfaces.selectUser.SelectUserFragment
import com.example.interfaces.settings.SettingAdapter
import com.example.interfaces.settings.SettingsFragment
import com.example.interfaces.settings.SettingsViewModel
import com.example.interfaces.user.UserFragment
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class SchedulesFragment : Fragment() {
    private var binding: FragmentSchedulesBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(SchedulesViewModel::class.java) }
    
    private val scheduleOnAdapter by lazy { SettingAdapter(adapterForSettings = false) }
    private val scheduleOffAdapter by lazy { SettingAdapter(adapterForSettings = false) }

    private var test = 1

    private var isRemouteActive = false
        set(value) {
            field = value
            showOrHideRemoute(show = value)
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSchedulesBinding.inflate(inflater, container, false)
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

        viewModel.loadSchedules()
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
        viewModel.scheduleOnL.observe(viewLifecycleOwner) {
            scheduleOnAdapter.setDataSource(it)
        }
        
        viewModel.scheduleOffL.observe(viewLifecycleOwner) {
            scheduleOffAdapter.setDataSource(it)
        }
    }

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.schedules_toolbar)
        binding?.toolbar?.menuBtn?.setOnClickListener {
            binding?.slider?.drawerLayout?.open()
        }
    }

    private fun initAdapter() {
        binding?.turnOnScheduleRv?.adapter = scheduleOnAdapter
        binding?.turnOffScheduleRv?.adapter = scheduleOffAdapter
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
                    iconRes = R.drawable.ic_calendar
                    identifier = 3
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu4
                    iconRes = R.drawable.ic_menu_settings
                    identifier = 4
                },
                DividerDrawerItem(),
                PrimaryDrawerItem().apply {
                    nameRes = R.string.menu5
                    iconRes = R.drawable.ic_menu_logout
                    identifier = 5
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
                    SettingsFragment.open(findNavController(), user)
                }
                5 -> {
                    SelectUserFragment.open(findNavController())
                }
                else -> {  }
            }
            /*return*/ false
        }

        startHardOperation()
    }

    override fun onStop() {
        super.onStop()
     //   stopHardOperation()
    }

    var doHardOperation = true
    val array = arrayListOf<Int>()

    private fun startHardOperation() {
        repeat(Random.nextInt(from = 100, until = 1000)) {
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 0 until 10000) {
                    if (!doHardOperation) break

                    array.add(Random.nextInt())

                    if (i % 100 == 0)  {
                        delay(Random.nextLong(until = 50))
                    }
                }
            }
        }
    }

    private fun stopHardOperation() {
        doHardOperation = false
        array.clear()
    }

    companion object {
        private const val TAG = "SchedulesFragment"

        fun open(navController: NavController, user: User?) {
            SchedulesViewModel.userInit = user
            navController.navigate(R.id.scheduleFragment)
        }
    }
}
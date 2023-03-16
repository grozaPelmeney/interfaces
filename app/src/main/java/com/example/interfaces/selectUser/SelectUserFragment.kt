package com.example.interfaces.selectUser

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
import com.example.interfaces.databinding.FragmentSelectUserBinding
import com.example.interfaces.main.UserContentFragment
import com.example.interfaces.user.UserFragment

class SelectUserFragment : Fragment() {
    private var binding: FragmentSelectUserBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(SelectUserViewModel::class.java) }
    private val userAdapter by lazy { SelectUserAdapter() }

    private var isRemouteActive = false
        set(value) {
            showOrHideRemoute(show = value)
            field = value
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initAdapter()
        fillToolbar()
        initRemoute()
        setOnClicks()

        viewModel.loadUsers()
    }

    private fun setOnClicks() {
        //Переопределил поведение кнопки "назад", чтобы, если текущего пользователя нет, "назад" выходило из приложения
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (Session.currentUser != null) {
                    findNavController().popBackStack()
                }
                else {
                    activity?.finish()
                }
            }
        })
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

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.select_user_toolbar)

        binding?.toolbar?.addBtn?.setOnClickListener {
            UserFragment.open(findNavController(), user = null, isNewUser = true)
        }
    }

    private fun initObservers() {
        viewModel.usersL.observe(viewLifecycleOwner) {
            userAdapter.setDataSource(it)
        }
    }

    private fun initAdapter() {
        binding?.usersRv?.adapter = userAdapter
        userAdapter.doOnItemClick = { user ->
            Session.updateCurrentUser(user)
            UserContentFragment.open(findNavController())
        }
    }

    companion object {
        private const val TAG = "SelectUserFragment"

        fun open(navController: NavController) {
            navController.navigate(R.id.selectUserFragment)
        }
    }
}
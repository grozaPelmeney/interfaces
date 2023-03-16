package com.example.interfaces.user

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.interfaces.R
import com.example.interfaces.Utils
import com.example.interfaces.databinding.FragmentUserBinding
import com.example.interfaces.main.UserContentFragment
import com.example.interfaces.models.User
import com.example.interfaces.remoute.RemouteFragment
import com.example.interfaces.selectUser.SelectUserFragment
import com.example.interfaces.settings.SettingsFragment
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText


class UserFragment : Fragment() {
    private var binding: FragmentUserBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding?.deleteBtn?.isVisible = !viewModel.isNewUser

        initRemoute()
        fillToolbar()
        setOnClicks()

        binding?.fieldTv?.setText(viewModel.user?.name)
    }

    private fun showDialogForDeleteUser() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_user_confirm_text))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deleteUser()
                findNavController().navigate(R.id.selectUserFragment)
                dialog?.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                /* do nothing */
                dialog?.dismiss()
            }
            .show()
    }

    private fun setOnClicks() {
        binding?.deleteBtn?.setOnClickListener {
            showDialogForDeleteUser()
        }

        binding?.saveBtn?.setOnClickListener {
            val username = binding?.fieldTv?.text?.toString()

            if (viewModel.validateUsername(username)) {
                Utils.showToast(requireContext(), getString(R.string.save_data_success))
                hideKeyboard()
                if (viewModel.isNewUser) {
                    viewModel.addUser(username!!)
                    UserContentFragment.open(findNavController())
                }
                else {
                    viewModel.updateUser()
                    UserContentFragment.open(findNavController())
                }
            }
            else {
                Utils.showToast(requireContext(), getString(R.string.new_username_error))
            }
        }
    }

    private fun hideKeyboard() {
        val imm = (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = getText(R.string.user_toolbar)

        binding?.toolbar?.backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private var isRemouteActive = false
        set(value) {
            field = value
            showOrHideRemoute(show = value)
        }
    private var isPaused = false

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
        private const val TAG = "UserFragment"

        fun open(navController: NavController, user: User?, isNewUser: Boolean) {
            UserViewModel.userInit = user
            UserViewModel.isNewUserInit = isNewUser
            navController.navigate(R.id.userFragment)
        }
    }
}
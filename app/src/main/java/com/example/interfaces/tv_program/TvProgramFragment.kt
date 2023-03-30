package com.example.interfaces.tv_program

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.interfaces.R
import com.example.interfaces.databinding.FragmentTvProgramBinding
import com.example.interfaces.models.Content

class TvProgramFragment : Fragment() {
    private var binding: FragmentTvProgramBinding? = null
    private val viewModel by lazy { ViewModelProvider(this).get(TvProgramViewModel::class.java) }

    private var isRemouteActive = false
        set(value) {
            field = value
            showOrHideRemoute(show = value)
        }
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTvProgramBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRemoute()
        initObservers()
        fillToolbar()

        showOrHideLoading(show = true)
        viewModel.getSchedule()
    }

    private fun showOrHideLoading(show: Boolean) {
        binding?.loadingBar?.isVisible = show
        binding?.tvScheduleTv?.isVisible = !show
    }

    private fun fillToolbar() {
        binding?.toolbar?.titleTv?.text = viewModel.tvChannel?.name
        binding?.toolbar?.backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.scheduleL.observe(viewLifecycleOwner) {
            showOrHideLoading(show = false)
            binding?.tvScheduleTv?.text = it
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
        private const val TAG = "TvScheduleFragment"

        fun open(navController: NavController, channel: Content?) {
            TvProgramViewModel.tvChannelInit = channel
            navController.navigate(R.id.tvScheduleFragment)
        }
    }
}
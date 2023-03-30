package com.example.interfaces.settings

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.interfaces.Session
import com.example.interfaces.databinding.*
import com.example.interfaces.models.Setting
import com.example.interfaces.models.SettingType
import java.util.Calendar
import kotlin.math.min

class SettingAdapter(private val adapterForSettings: Boolean = true): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var settings = listOf<Setting>()

    fun setDataSource(settings: List<Setting>) {
        this.settings = settings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            SettingType.RANGE.value -> {
                val binding = SettingRangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RangeSettingViewHolder(binding)
            }

            SettingType.SELECT.value -> {
                val binding = SettingSelectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SelectSettingViewHolder(binding)
            }

            SettingType.CHECK_WITH_TIME.value -> {
                val binding = SettingCheckWithTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CheckWithTimeSettingViewHolder(binding)
            }

            else -> {
                Log.e(TAG, "onCreateViewHolder - wrong viewType")
                val binding = SettingSelectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SelectSettingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val setting = settings[position]
        val type = when (setting) {
            is Setting.RangeSetting -> {
                setting.type.value
            }
            is Setting.SelectSetting -> {
                setting.type.value
            }
            is Setting.CheckWithTimeSetting -> {
                setting.type.value
            }
            else -> { -1 }
        }

        return type
    }

    override fun getItemCount() =
        settings.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            holder is RangeSettingViewHolder -> {
                holder.bind()
            }
            holder is SelectSettingViewHolder -> {
                holder.bind()
            }
            holder is CheckWithTimeSettingViewHolder -> {
                holder.bind()
            }
            else -> {  }
        }
    }

    inner class RangeSettingViewHolder(private val binding: SettingRangeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind() {
            val setting = settings[absoluteAdapterPosition] as? Setting.RangeSetting

            binding.errorL.isVisible = setting == null

            if (setting != null) {
                binding.titleTv.text = context.getText(setting.nameRes)
                binding.fromValueTv.text = setting.from.toString()
                binding.toValueTv.text = setting.to.toString()
                binding.progressTv.text = setting.value.toString()

                binding.seekBar.progress = setting.value
                binding.seekBar.max = setting.to
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.seekBar.min = setting.from
                }

                binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                            if (progress < setting.from) {
                                seekBar?.progress = setting.from
                            }
                        }

                        setting.value = progress

                        binding.progressTv.text = progress.toString()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        /* do nothing */
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (adapterForSettings) {
                            Session.updateSetting(setting)
                        }
                    }
                })
            }
        }
    }

    inner class SelectSettingViewHolder(private val binding: SettingSelectItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        private fun showPopup(menuView: View, setting: Setting.SelectSetting) {
            val popup = PopupMenu(context, menuView)

            setting.values.forEachIndexed { i, item ->
                popup.menu.add(Menu.NONE, item.id, i, context.getString(item.nameRes))
            }

            popup.setOnMenuItemClickListener { menuItem ->
                setting.values.firstOrNull { it.id == menuItem.itemId }?.let { selectedItem ->
                    binding.fieldTv.text = context.getText(selectedItem.nameRes)
                    setting.selectedValueId = selectedItem.id

                    if (adapterForSettings) {
                        Session.updateSetting(setting)
                    }
                }

                return@setOnMenuItemClickListener true
            }

            popup.gravity = Gravity.END

            popup.show()
        }

        fun bind() {
            val setting = settings[absoluteAdapterPosition] as? Setting.SelectSetting

            binding.errorL.isVisible = setting == null

            if (setting != null) {
                binding.titleTv.text = context.getText(setting.nameRes)

                val selectedValueNameRes = setting.values.firstOrNull { it.id == setting.selectedValueId }?.nameRes
                if (selectedValueNameRes != null) {
                    binding.fieldTv.text = context.getText(selectedValueNameRes)
                }

                binding.field.setOnClickListener {
                    showPopup(
                        menuView = binding.fieldIv,
                        setting = setting,
                    )
                }
            }
        }
    }

    inner class CheckWithTimeSettingViewHolder(private val binding: SettingCheckWithTimeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        private fun showTimepicker(doOnTimeSelected: ((hour: Int, minutes: Int) -> Unit)) {
            val calendar = Calendar.getInstance()

            val dialog = TimePickerDialog(
                context,
                { timePicker, hourOfDay, minute ->
                    doOnTimeSelected.invoke(hourOfDay, minute)
                },
               /* hourOfDay */ calendar.get(Calendar.HOUR_OF_DAY),
               /* minute */ calendar.get(Calendar.MINUTE),
               /* is24HourView */ true
            )
            dialog.show()
        }

        fun bind() {
            val setting = settings[absoluteAdapterPosition] as? Setting.CheckWithTimeSetting

            binding.errorL.isVisible = setting == null

            if (setting != null) {
                binding.titleTv.text = context.getText(setting.nameRes)

                binding.field.isEnabled = setting.isSelected
                binding.field.alpha = if (setting.isSelected) 1f else 0.5f

                binding.switchV.setOnCheckedChangeListener { compoundButton, value ->
                    setting.isSelected = value
                    binding.field.isEnabled = setting.isSelected
                    binding.field.alpha = if (setting.isSelected) 1f else 0.5f

                    if (adapterForSettings) {
                        Session.updateSetting(setting)
                    }
                }

                binding.field.setOnClickListener {
                    showTimepicker(
                        doOnTimeSelected = { hour, minute ->
                            setting.value = "${hour}:${minute}"
                            binding.fieldTv.text = setting.value

                            if (adapterForSettings) {
                                Session.updateSetting(setting)
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "SettingAdapter"
    }
}
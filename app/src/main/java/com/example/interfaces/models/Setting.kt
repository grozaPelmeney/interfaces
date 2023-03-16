package com.example.interfaces.models

class SelectSettingItem(val id: Int, val nameRes: Int)

sealed class Setting() {
    class RangeSetting(
        val id: Int,
        val type: SettingType = SettingType.RANGE,
        val nameRes: Int,
        val from: Int,
        val to: Int,
        var value: Int,
    ) : Setting()

    class SelectSetting(
        val id: Int,
        val type: SettingType = SettingType.SELECT,
        val nameRes: Int,
        var selectedValueId: Int,
        val values: List<SelectSettingItem>
    ) : Setting()

    class CheckWithTimeSetting(
        val id: Int,
        val type: SettingType = SettingType.CHECK_WITH_TIME,
        val nameRes: Int,
        var isSelected: Boolean,
        var value: String
    ) : Setting()
}
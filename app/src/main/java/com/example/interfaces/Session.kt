package com.example.interfaces

import com.example.TestDataGenerator
import com.example.interfaces.models.Content
import com.example.interfaces.models.ContentType
import com.example.interfaces.models.Setting
import com.example.interfaces.models.User

object Session {
    private val userList = TestDataGenerator.getUsers().toMutableList()
    private val settingList = TestDataGenerator.getSettings().toMutableList()

    var currentUser: User? = null
        private set

    fun updateCurrentUser(user: User) {
        currentUser = user
    }

    fun addUser(user: User) {
        userList.add(user)
        currentUser = user
    }

    fun removeUser(user: User) {
        userList.removeAll { it.id == user.id }
        currentUser = null
    }

    fun getUsers(): List<User> {
        return userList
    }


    fun getContent(type: ContentType? = null): List<Content> {
        return TestDataGenerator.getContent(type)
    }

    fun toFavourite(content: Content) {
        val user = currentUser ?: return

        val isFavourite = user.likes.map { it.id }.contains(content.id)

        val newLikes =
            if (isFavourite) {
                user.likes.filter { it.id != content.id }
            }
            else {
                val buf = user.likes.toMutableList()
                buf.add(content)
                buf
            }

        user.likes = newLikes
    }


    fun updateSetting(setting: Setting) {
        val oldSettingIndex =
            when (setting) {
                is Setting.RangeSetting -> {
                    settingList.indexOfFirst { oldSetting ->
                        when (oldSetting) {
                            is Setting.RangeSetting -> { oldSetting.id == setting.id }
                            is Setting.SelectSetting -> { oldSetting.id == setting.id }
                            is Setting.CheckWithTimeSetting -> { oldSetting.id == setting.id }
                        }
                    }
                }

                is Setting.SelectSetting -> {
                    settingList.indexOfFirst { oldSetting ->
                        when (oldSetting) {
                            is Setting.RangeSetting -> { oldSetting.id == setting.id }
                            is Setting.SelectSetting -> { oldSetting.id == setting.id }
                            is Setting.CheckWithTimeSetting -> { oldSetting.id == setting.id }
                        }
                    }
                }

                is Setting.CheckWithTimeSetting -> {
                    settingList.indexOfFirst { oldSetting ->
                        when (oldSetting) {
                            is Setting.RangeSetting -> { oldSetting.id == setting.id }
                            is Setting.SelectSetting -> { oldSetting.id == setting.id }
                            is Setting.CheckWithTimeSetting -> { oldSetting.id == setting.id }
                        }
                    }
                }
            }

        settingList.removeAt(oldSettingIndex)
        settingList.add(oldSettingIndex, setting)
    }

    fun getSettings(): List<Setting> {
        return settingList
    }
}
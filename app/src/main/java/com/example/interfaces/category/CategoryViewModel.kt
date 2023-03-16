package com.example.interfaces.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interfaces.Session
import com.example.interfaces.models.Content
import com.example.interfaces.models.ContentType
import com.example.interfaces.models.User

class CategoryViewModel : ViewModel() {
    val user by lazy {
        if (userInit != null) {
            val buf = userInit
            userInit = null
            return@lazy buf
        }
        return@lazy null
    }
    val contentType by lazy {
        if (contentTypeInit != null) {
            val buf = contentTypeInit
            contentTypeInit = null
            return@lazy buf
        }
        return@lazy null
    }

    val categoryItemsL = MutableLiveData<List<Content>>()

    fun loadCategory() {
        val items =
            if (contentType == ContentType.FAVOURITES) {
                user?.likes ?: listOf()
            }
            else {
                Session.getContent(contentType)
            }

        categoryItemsL.postValue(items)
    }

    fun toFavourite(content: Content) {
        Session.toFavourite(content)
    }

    companion object {
        var userInit: User? = null
        var contentTypeInit: ContentType? = null
    }
}
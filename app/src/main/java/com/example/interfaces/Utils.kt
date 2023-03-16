package com.example.interfaces

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.Disposable
import coil.request.ImageRequest

object Utils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun loadImage(imageView: ImageView,
                  pictureUrl: String?): Disposable
    {
        val context = imageView.context

        val imageLoader: ImageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context))
            }
            .memoryCachePolicy(policy = CachePolicy.ENABLED)
            .crossfade(true)
            .crossfade(500)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .allowHardware(true)
            .build()

        if (pictureUrl.isNullOrEmpty()) {
            val request = ImageRequest.Builder(context)
                .data(R.drawable.ic_placeholder)
                .target(imageView)
                .build()
            return imageLoader.enqueue(request)
        }

        val request = ImageRequest.Builder(context)
            .data(pictureUrl)
            .target(imageView)
            .listener(
                onError = { request, throwable ->
                    Log.e(javaClass.simpleName, "loadImage error: $throwable")
                }
            )
            .build()

        return imageLoader.enqueue(request)
    }
}
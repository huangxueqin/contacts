package com.xueqin.contacts.loader

import android.graphics.Bitmap

interface ImageDownloader {

    fun download(url: String): Bitmap?

}

val NullDownloader = object : ImageDownloader {
    override fun download(url: String): Bitmap? {
        return null
    }
}

package com.xueqin.contacts.loader

import java.io.InputStream

interface UrlLoader {

    fun canHandle(url: String): Boolean

    fun open(url: String): InputStream?

}

val httpUrlLoader = object : UrlLoader {
    override fun canHandle(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    override fun open(url: String): InputStream? {
        // not implemented
        return null;
    }
}

val fileUrlLoader = object : UrlLoader {

    override fun canHandle(url: String): Boolean {
        return url.startsWith("/") || url.startsWith("file://")
    }

    override fun open(url: String): InputStream? {
        // not implemented
        return null;
    }

}


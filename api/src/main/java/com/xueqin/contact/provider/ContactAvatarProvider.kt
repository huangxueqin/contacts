package com.xueqin.contact.provider

import android.content.Context
import androidx.annotation.Keep

import java.io.InputStream

@Keep
interface ContactAvatarProvider {

    fun getContactAvatar(context: Context, url: String): InputStream?

}

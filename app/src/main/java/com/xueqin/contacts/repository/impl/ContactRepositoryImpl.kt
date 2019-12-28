package com.xueqin.contacts.repository.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.Log
import com.xueqin.contacts.model.ContactInfo
import com.xueqin.contacts.repository.ContactRepository
import com.xueqin.contacts.util.ContactUtils
import com.xueqin.contacts.util.IOUtils
import java.io.IOException
import java.lang.Exception

private const val CONTACT_PATH = "contacts.json"
private const val AVATAR_FOLDER = "avatars"

class ContactRepositoryImpl(context: Context) : ContactRepository {

    private val Tag = "ContactRepositoryImpl"

    private val mContext = context.applicationContext

    override fun queryAllContacts(): List<ContactInfo> =
        IOUtils.readTextFromAsset(mContext, CONTACT_PATH).let {
            ContactUtils.parseContactList(it)
        }

    override fun loadAvatar(contactInfo: ContactInfo): Bitmap? {
        try {
            val avatarName = getActualAvatarName(contactInfo)
            Log.d(Tag, "avatar path is ${AVATAR_FOLDER}/${avatarName}")
            val inputStream = mContext.assets.open("${AVATAR_FOLDER}/${avatarName}")
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e(Tag, "error happened in load avatar")
        return null;
    }

    /**
     * TODO: reference array without check may produces NPE in real project, but ok here
     */
    private fun getActualAvatarName(contactInfo: ContactInfo) =
        contactInfo.avatarFileName.split(".").first().let {
            getAvatarFileNameByDpi(it)
        }

    private fun getAvatarFileNameByDpi(name: String) =
        when (mContext.resources.displayMetrics.densityDpi) {
            in -1 until DisplayMetrics.DENSITY_HIGH ->
                "${name}.png"
            in DisplayMetrics.DENSITY_HIGH until DisplayMetrics.DENSITY_XHIGH ->
                "${name}@2x.png"
            else ->
                "${name}@3x.png"
        }
}

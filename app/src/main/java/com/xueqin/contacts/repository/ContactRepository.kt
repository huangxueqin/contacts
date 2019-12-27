package com.xueqin.contacts.repository

import android.graphics.Bitmap
import com.xueqin.contacts.model.ContactInfo

interface ContactRepository {

    fun queryAllContacts(): List<ContactInfo>

    fun loadAvatar(contactInfo: ContactInfo): Bitmap?

}
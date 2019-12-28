package com.xueqin.contact.provider

import android.content.Context

import androidx.annotation.Keep
import com.xueqin.contact.model.ContactInfo


@Keep
interface ContactDataProvider {

    /**
     * load contact list
     * @return null if there is no contact info
     */
    fun queryContacts(context: Context): List<ContactInfo>?

}

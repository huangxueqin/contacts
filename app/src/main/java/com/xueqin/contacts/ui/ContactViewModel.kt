package com.xueqin.contacts.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.xueqin.contact.model.ContactInfo
import com.xueqin.contact.provider.ContactDataProvider
import com.xueqin.contacts.provider.ContactProviderManager
import kotlin.concurrent.thread

class ContactViewModel(app: Application) : AndroidViewModel(app) {

    private val provider by lazy {
        ContactProviderManager.instance
            .getProvider<ContactDataProvider>(ContactDataProvider::class.java.name)
    }

    val contactList: MutableLiveData<List<ContactInfo>> = MutableLiveData()

    init {
        thread {
            provider?.let {
                contactList.postValue(it.queryContacts(app.applicationContext))
            }
        }
    }

}
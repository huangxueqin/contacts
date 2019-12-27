package com.xueqin.contacts.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.xueqin.contacts.model.ContactInfo
import com.xueqin.contacts.repository.defaultRepository
import kotlin.concurrent.thread

class ContactViewModel(app: Application) : AndroidViewModel(app) {

    private val repository by lazy {
        defaultRepository(app.applicationContext)
    }

    val contactList: MutableLiveData<List<ContactInfo>> = MutableLiveData()

    init {
        thread {
            contactList.postValue(repository.queryAllContacts())
        }
    }

}
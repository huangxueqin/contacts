@file:JvmName("ContactRepositoryFactory")
package com.xueqin.contacts.repository

import android.content.Context
import com.xueqin.contacts.repository.impl.ContactRepositoryImpl

private var _defaultRepository: ContactRepository? = null

@Synchronized
fun defaultRepository(context: Context): ContactRepository {
    if (_defaultRepository == null) {
        _defaultRepository = ContactRepositoryImpl(context)
    }
    return _defaultRepository!!
}

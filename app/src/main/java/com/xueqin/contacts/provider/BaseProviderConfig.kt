package com.xueqin.contacts.provider

import com.xueqin.contact.provider.ContactDataProvider

internal val baseProviderInfoMap = mapOf<String, String>(
    ContactDataProvider::class.java.name to "com.xueqin.contacts.data.provider.ContactDataProviderImpl"
)
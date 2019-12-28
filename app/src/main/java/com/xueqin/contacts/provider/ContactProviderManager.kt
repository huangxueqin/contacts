package com.xueqin.contacts.provider

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

class ContactProviderManager private constructor() {

    private val Tag = "ContactProviderManager"

    // singleton
    companion object {
        @JvmStatic
        val instance by lazy {
            ContactProviderManager()
        }
    }

    private val providerInfoMap = ConcurrentHashMap<String, String>(baseProviderInfoMap)
    private val providerMap = ConcurrentHashMap<String, Any>()

    @Synchronized
    fun setProvider(name: String, instance: Any) {
        if (name.isEmpty()) {
            Log.w(Tag, "can not set a provider with blank name")
            return;
        }
        providerMap[name] = instance
    }

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun <T> getProvider(name: String): T? {
        initProvider(name)
        return providerMap[name]?.let { it as T }
    }

    private fun initProvider(name: String) {
        if (providerMap.contains(name)) {
            // already cached
            return;
        }
        val clazzName = providerInfoMap[name] ?: return
        try {
            val clazz = Class.forName(clazzName)
            val instance = clazz.newInstance()
            if (instance != null) {
                providerMap[name] = instance
            }
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

}

package com.xueqin.contacts.data

import com.xueqin.contacts.data.util.IOUtils

import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo

class IOUnitTest {

    @Test
    fun readTextFromStream_correctly() {
        try {
            val `is` = javaClass.classLoader!!.getResourceAsStream("testio.txt")
            assertThat(IOUtils.readTextFromStream(`is`), equalTo("io_test"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}

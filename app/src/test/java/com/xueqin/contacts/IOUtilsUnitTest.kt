package com.xueqin.contacts

import com.xueqin.contacts.data.util.IOUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class IOUtilsUnitTest {

    @Test
    fun readText_correctly() {
        javaClass.classLoader?.getResourceAsStream("testio.txt")?.let {
            val text = com.xueqin.contacts.data.util.IOUtils.readTextFromStream(it)
            assertEquals("read \"io_test\" from asset", "io_test", text)
        }
    }
}
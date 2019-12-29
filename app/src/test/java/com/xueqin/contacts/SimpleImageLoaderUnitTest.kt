package com.xueqin.contacts

import com.xueqin.contacts.loader.fileUrlLoader
import com.xueqin.contacts.loader.httpUrlLoader
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SimpleImageLoaderUnitTest {

    @Test
    fun url_handleCorrectly() {
        assertTrue(httpUrlLoader.canHandle("https://dddddd"))
        assertTrue(fileUrlLoader.canHandle("file://ddddddd"))
        assertTrue(fileUrlLoader.canHandle("/ddddddd"))
    }

}
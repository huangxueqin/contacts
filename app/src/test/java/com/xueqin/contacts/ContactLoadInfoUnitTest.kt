package com.xueqin.contacts

import com.xueqin.contacts.helper.createTestContactJSON
import com.xueqin.contacts.util.ContactUtils
import com.xueqin.contacts.util.IOUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ContactLoadInfoUnitTest {

    @Test
    fun contactInfo_parseSuccess() {
        val contactJo = createTestContactJSON()
        val contactInfo = ContactUtils.parseContactInfo(contactJo)
        assertNotNull(contactInfo)
        assertEquals("first name parsed", contactJo.getString("first_name"), contactInfo?.firstName)
        assertEquals("last name parsed", contactJo.getString("last_name"), contactInfo?.lastName)
        assertEquals("title parsed", contactJo.getString("title"), contactInfo?.title)
        assertEquals("avatar filename parsed", contactJo.getString("avatar_filename"), contactInfo?.avatarFileName)
        assertEquals("introduction parsed" ,contactJo.getString("introduction"), contactInfo?.introduction)
    }

    @Test
    fun contactInfoWithoutFirstName_parseFail() {
        val contactJo = createTestContactJSON();
        contactJo.put("first_name", "")
        assertNull(ContactUtils.parseContactInfo(contactJo))
    }

    @Test
    fun contactList_parseEmpty() {
        val text = "[]"
        assertThat(ContactUtils.parseContactList(text).size, equalTo(0))
    }

    @Test
    fun contactList_parseWrongFormat() {
        val text = "???"
        assertThat(ContactUtils.parseContactList(text).size, equalTo(0))
    }

    @Test
    fun contactList_parseSuccess() {
        /*
         * test.json contains 5 correct contact info record
         */
        javaClass.classLoader?.getResourceAsStream("test.json")?.let {
            val json = IOUtils.readTextFromStream(it)
            val contacts = ContactUtils.parseContactList(json)
            assertThat(contacts.size, equalTo(5))
            assertTrue(contacts.filter { it.firstName == "Carlos" }.isNotEmpty())
            assertTrue(contacts.filter { it.firstName == "Carlos" }[0].title == "Nurse")
        }
    }

    @Test
    fun contactList_parseFail() {
        assertThat(ContactUtils.parseContactList("???").size, equalTo(0))
    }

}
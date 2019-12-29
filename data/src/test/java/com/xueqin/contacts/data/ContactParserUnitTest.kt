package com.xueqin.contacts.data

import com.xueqin.contacts.data.helper.getTestContactJson
import com.xueqin.contacts.data.util.ContactParser
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ContactParserUnitTest {

    @Test
    fun contactInfo_parseSuccess() {
        val contactJo = getTestContactJson()
        val contactInfo = ContactParser.parseContactInfo(contactJo)
        assertNotNull(contactInfo)
        assertEquals(
            "first name parsed",
            contactJo.getString("first_name"),
            contactInfo?.firstName
        )
        assertEquals(
            "last name parsed",
            contactJo.getString("last_name"),
            contactInfo?.lastName
        )
        assertEquals(
            "title parsed",
            contactJo.getString("title"),
            contactInfo?.title
        )
        assertEquals(
            "avatar filename parsed",
            contactJo.getString("avatar_filename"),
            contactInfo?.avatarUrl
        )
        assertEquals(
            "introduction parsed" ,
            contactJo.getString("introduction"),
            contactInfo?.introduction
        )
    }

    @Test
    fun contactInfoWithoutFirstName_parseFail() {
        val contactJo = getTestContactJson()
        contactJo.put("first_name", "")
        assertNull(ContactParser.parseContactInfo(contactJo))
    }

    @Test
    fun contactList_parseEmpty() {
        val text = "[]"
        assertThat(ContactParser.parseContactList(text).size, equalTo(0))
    }

    @Test
    fun contactList_parseWrongFormat() {
        val text = "???"
        assertThat(ContactParser.parseContactList(text).size, equalTo(0))
    }

    @Test
    fun contactList_parseSuccess() {
        /*
         * test.json contains 5 correct contact info record
         */
        javaClass.classLoader?.getResourceAsStream("test.json")?.let {
            val json = com.xueqin.contacts.data.util.IOUtils.readTextFromStream(it)
            val contacts = ContactParser.parseContactList(json)
            assertThat(contacts.size, equalTo(5))
            assertTrue(contacts.filter { it.firstName == "Carlos" }.isNotEmpty())
            assertTrue(contacts.filter { it.firstName == "Carlos" }[0].title == "Nurse")
        }
    }

    @Test
    fun contactList_parseFail() {
        assertThat(ContactParser.parseContactList("???").size, equalTo(0))
    }

}
package com.xueqin.contacts

import com.xueqin.contacts.provider.ContactProviderManager
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ContactProviderUnitTest {

    private interface TestProvider {
        fun name(): String
    }

    private interface TestProvider2 {
        fun name2(): String
    }

    private lateinit var manager: ContactProviderManager
    private lateinit var testProviderImpl: TestProvider

    @Before
    fun init() {
        manager = ContactProviderManager.instance

        testProviderImpl = mock(TestProvider::class.java)
        `when`(testProviderImpl.name()).thenReturn("TestProvider")
        manager.setProvider(TestProvider::class.java.name, testProviderImpl)
    }

    @Test
    fun getProvider_correctly() {
        val provider = manager.getProvider<TestProvider>(TestProvider::class.java.name)
        assertEquals(provider, testProviderImpl)
        assertThat(provider?.name(), equalTo("TestProvider"))
    }

    @Test
    fun setProvider_correctly() {
        val impl2 = mock(TestProvider2::class.java)
        `when`(impl2.name2()).thenReturn("TestProvider2")
        manager.setProvider(TestProvider2::class.java.name, impl2)
        val provider = manager.getProvider<TestProvider2>(TestProvider2::class.java.name)
        assertEquals(provider, impl2)
        assertEquals(provider?.name2(), "TestProvider2")
    }

    @Test
    fun setProvider_overrideCorrectly() {
        val anotherImpl = mock(TestProvider::class.java)
        `when`(anotherImpl.name()).thenReturn("Another TestProvider")
        manager.setProvider(TestProvider::class.java.name, anotherImpl)
        val provider = manager.getProvider<TestProvider>(TestProvider::class.java.name)
        assertEquals(provider, anotherImpl)
        assertEquals(provider?.name(), "Another TestProvider")
    }

}
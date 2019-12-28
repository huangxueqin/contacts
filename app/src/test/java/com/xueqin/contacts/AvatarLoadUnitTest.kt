package com.xueqin.contacts

import com.xueqin.contacts.helper.testNameList
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AvatarLoadUnitTest {

    @Test
    fun avatarFileName_splitCorrectly() {
        val namesWithExt = testNameList.map { "${it}.png" }
        for (i in testNameList.indices) {
            namesWithExt[i].split(".").let {
                MatcherAssert.assertThat(it.size, CoreMatchers.equalTo(2))
                MatcherAssert.assertThat(it[0], CoreMatchers.equalTo(testNameList[i]))
                MatcherAssert.assertThat(it[1], CoreMatchers.equalTo("png"))
            }
        }
    }



}
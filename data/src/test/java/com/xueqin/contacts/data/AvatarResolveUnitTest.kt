package com.xueqin.contacts.data

import android.util.DisplayMetrics
import com.xueqin.contacts.data.helper.TEST_NAME_LIST

import com.xueqin.contacts.data.util.AssetAvatarResolver

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.mockito.Mockito.mock

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AvatarResolveUnitTest {

    @Test
    fun filename_splitCorrectly() {
        for (i in TEST_NAME_LIST.indices) {
            val nameWithoutExt = TEST_NAME_LIST[i]
            val splits = "$nameWithoutExt.png".split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            assertThat(splits.size, equalTo(2))
            assertThat(splits[0], equalTo(nameWithoutExt))
            assertThat(splits[1], equalTo("png"))
        }
    }

    @Test
    fun filename_dpiValidation() {
        val dm = mock(DisplayMetrics::class.java)
        // low density
        dm.densityDpi = DisplayMetrics.DENSITY_140
        assertThat(
            AssetAvatarResolver.getAvatarFilenameForScreenDpi(dm, "hahaha"),
            equalTo("hahaha")
        )
        // medium density
        dm.densityDpi = DisplayMetrics.DENSITY_280
        assertThat(
            AssetAvatarResolver.getAvatarFilenameForScreenDpi(dm, "hahaha"),
            equalTo("hahaha@2x")
        )
        // high density
        dm.densityDpi = DisplayMetrics.DENSITY_XXXHIGH
        assertThat(
            AssetAvatarResolver.getAvatarFilenameForScreenDpi(dm, "hahaha"),
            equalTo("hahaha@3x")
        )
    }

    @Test
    fun filename_concatCorrectly() {
        val dm = mock(DisplayMetrics::class.java)
        dm.densityDpi = DisplayMetrics.DENSITY_XXHIGH
        assertThat(
            AssetAvatarResolver.getAvatarPathInAsset(dm, "hahaha"),
            equalTo("avatars/hahaha@3x.png")
        )
    }
}

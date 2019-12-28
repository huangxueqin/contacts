package com.xueqin.contacts.helper

import org.json.JSONObject

fun createTestContactJSON() =
    JSONObject().apply {
        put("first_name", "Kevin")
        put("last_name", "Patterson")
        put("title", "Writer")
        put("introduction", "Kristin Patterson.png")
        put("avatar_filename", "Phasellus eget pellentesque augue. Morbi quis est non urna posuere")
    }

val testNameList = listOf<String>("Allan Munger", "Ashley Mc Carthy", "xueqin")

package com.xueqin.contacts.data.helper

import org.json.JSONObject

val TEST_NAME_LIST = listOf("Allan Munger", "Ashley Mc Carthy", "xueqin")

fun getTestContactJson() = JSONObject().apply {
    put("first_name", "Kevin")
    put("last_name", "Patterson")
    put("title", "Writer")
    put("introduction", "Kristin Patterson.png")
    put(
        "avatar_filename",
        "Phasellus eget pellentesque augue. Morbi quis est non urna posuere"
    )
}

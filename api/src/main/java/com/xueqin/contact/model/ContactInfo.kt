package com.xueqin.contact.model

/**
 * contact info model
 * @param avatarUrl maybe a online url or some custom schema, like asset://
 */
data class ContactInfo(val firstName: String,
                       val lastName: String,
                       val title: String,
                       val introduction: String,
                       var avatarUrl: String?)

package com.multi.producthunt.utils

import com.multi.producthunt.MR
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

class Validator {

    fun isValidLoginData(username: String, password: String): StringDesc {
        return when {
            username.isNullOrBlank() -> StringDesc.Resource(MR.strings.username_is_empty)
            password.isNullOrBlank() -> StringDesc.Resource(MR.strings.password_is_empty)
//            !password.contains(Regex("[A-Z]")) -> StringDesc.Resource(MR.strings.password_must_one_capital_letter)
//            !password.contains(Regex("[0-9]")) -> StringDesc.Resource(MR.strings.password_must_one_digit)
//            !password.contains(Regex("[^a-zA-Z0-9 ]")) -> StringDesc.Resource(MR.strings.password_must_special_character)
            else -> StringDesc.Raw("")
        }
    }
}
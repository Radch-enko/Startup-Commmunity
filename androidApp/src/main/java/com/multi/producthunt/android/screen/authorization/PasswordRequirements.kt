package com.multi.producthunt.android.screen.authorization

import com.multi.producthunt.MR
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

enum class PasswordRequirements(
    val label: StringDesc
) {
    CAPITAL_LETTER(StringDesc.Resource(MR.strings.password_requirement_capital)),
    NUMBER(StringDesc.Resource(MR.strings.password_requirement_digit)),
    EIGHT_CHARACTERS(StringDesc.Resource(MR.strings.password_requirement_characters))
}
package com.multi.producthunt.utils

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.StringDesc

fun getMyPluralDesc(resourceId: PluralsResource, quantity: Int): StringDesc {
    return StringDesc.PluralFormatted(resourceId, quantity, quantity)
}
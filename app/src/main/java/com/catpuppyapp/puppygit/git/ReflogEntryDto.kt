package com.catpuppyapp.puppygit.git

import com.github.git24j.core.Oid

data class ReflogEntryDto(
    var username:String="",
    var email:String="",
    var date:String="",
    var idNew: Oid?=null,
    var idOld:Oid?=null,
    var msg:String=""
) {
    fun getItemKey():String {
        return ""+hashCode()
    }
}

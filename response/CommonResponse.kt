package com.project.genassist_ecommerce.response

import com.project.genassist_ecommerce.model.Entries

data class CommonResponse(
    var error:Boolean,
    var message:String,
    var data: ArrayList<Entries>

)

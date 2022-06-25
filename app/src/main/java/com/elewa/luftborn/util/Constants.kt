package com.elewa.luftborn.util

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Constants {
    var DOWNLOADFLAG = false
    const val DATABASE_NAME = "luftborn_db"


    fun View.setSafeOnClickListener(onClick : (View) -> Unit) {
        RxView.clicks(this).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
            onClick(this)
        }
    }

    fun getDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
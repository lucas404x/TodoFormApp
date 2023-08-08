package com.noti0ns.todoformapp

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        private lateinit var instance: MyApp
        fun getInstance() = instance
    }
}
package com.mickstarify.nswdriverknowledgetest

import android.app.Application
import com.mickstarify.nswdriverknowledgetest.di.component.ApplicationComponent
import com.mickstarify.nswdriverknowledgetest.di.component.DaggerApplicationComponent
import com.mickstarify.nswdriverknowledgetest.di.module.ApplicationModule

class NSWDriverKnowledgeTestApplication : Application() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(this)
            ).build()
    }

}

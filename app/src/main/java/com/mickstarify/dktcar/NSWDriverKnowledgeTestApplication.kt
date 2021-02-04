package com.mickstarify.dktcar

import android.app.Application
import com.mickstarify.dktcar.di.component.ApplicationComponent
import com.mickstarify.dktcar.di.component.DaggerApplicationComponent
import com.mickstarify.dktcar.di.module.ApplicationModule

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

package com.mickstarify.nswdriverknowledgetest.di.component

import android.content.Context
import com.mickstarify.nswdriverknowledgetest.EntryScreen.EntryActivityModel
import com.mickstarify.nswdriverknowledgetest.QuestionListScreen.QuestionListActivityModel
import com.mickstarify.nswdriverknowledgetest.QuestionScreen.QuestionActivityModel
import com.mickstarify.nswdriverknowledgetest.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    val context: Context
    fun inject(entryActivityModel: EntryActivityModel)
    fun inject(questionActivityModel: QuestionActivityModel)
    fun inject(questionListActivityModel: QuestionListActivityModel)
}
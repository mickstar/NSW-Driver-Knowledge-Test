package com.mickstarify.dktcar.di.component

import android.content.Context
import com.mickstarify.dktcar.EntryScreen.EntryActivityModel
import com.mickstarify.dktcar.QuestionListScreen.QuestionListActivityModel
import com.mickstarify.dktcar.QuestionScreen.QuestionActivityModel
import com.mickstarify.dktcar.di.module.ApplicationModule
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
package com.mickstarify.nswdriverknowledgetest.di.module

import android.content.Context
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabase
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabaseMode
import com.mickstarify.nswdriverknowledgetest.NSWDriverKnowledgeTestApplication
import com.mickstarify.nswdriverknowledgetest.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val app: NSWDriverKnowledgeTestApplication) {
    @Provides
    fun provideContext(): Context {
        return app
    }

    private var riderQuestionDatabase: QuestionDatabase? = null
    private var carQuestionDatabase: QuestionDatabase? = null

    @Provides
    fun provideQuestionDatabase(context: Context, preferenceManager: PreferenceManager): QuestionDatabase {
        return when (preferenceManager.getQuestionMode()){
            QuestionDatabaseMode.RIDER -> provideRiderQuestionDatabase(context)
            QuestionDatabaseMode.CAR -> provideCarQuestionDatabase(context)
        }
    }

    @Singleton
    @Provides
    fun providePreferenceManager(context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @Singleton
    fun provideCarQuestionDatabase(context: Context): QuestionDatabase{
        if (carQuestionDatabase == null){
            carQuestionDatabase = QuestionDatabase(context, QuestionDatabaseMode.CAR)
        }
        return carQuestionDatabase!!
    }

    fun provideRiderQuestionDatabase(context: Context): QuestionDatabase{
        if (riderQuestionDatabase == null){
            riderQuestionDatabase = QuestionDatabase(context, QuestionDatabaseMode.RIDER)
        }
        return riderQuestionDatabase!!
    }
}
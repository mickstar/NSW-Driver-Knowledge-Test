package com.mickstarify.dktcar.di.module

import android.content.Context
import com.mickstarify.dktcar.Database.QuestionDatabase
import com.mickstarify.dktcar.Database.QuestionDatabaseMode
import com.mickstarify.dktcar.NSWDriverKnowledgeTestApplication
import com.mickstarify.dktcar.PreferenceManager
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
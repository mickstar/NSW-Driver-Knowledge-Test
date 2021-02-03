package com.mickstarify.nswdriverknowledgetest.Database.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabaseMode
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Database(entities = arrayOf(QuestionStatus::class), version = 1)
abstract class QuestionStatusRoomDatabase : RoomDatabase() {
    abstract fun questionStatusDao(): QuestionStatusDao
}

@Singleton
class QuestionStatusDatabase @Inject constructor(val context: Context) {
    val db = Room.databaseBuilder(
        context.applicationContext,
        QuestionStatusRoomDatabase::class.java, "questionStatus"
    ).build()

    fun getQuestionStatus(id: String, mode: QuestionDatabaseMode): Single<Int> {
        val modeAsInt = when(mode){
            QuestionDatabaseMode.CAR -> 0
            QuestionDatabaseMode.RIDER -> 1
        }
        return db.questionStatusDao().getQuestionStatus(id, modeAsInt).map { it.answerStatus }.defaultIfEmpty(QuestionStatus.NOT_ANSWERED)
    }

    fun getAllQuestionStatuses(mode: QuestionDatabaseMode): Single<List<QuestionStatus>> {
        val modeAsInt = when(mode){
            QuestionDatabaseMode.CAR -> 0
            QuestionDatabaseMode.RIDER -> 1
        }
        return Single.fromCallable {
            db.questionStatusDao().getAllQuestionStatuses(modeAsInt)
        }
    }

    fun setQuestionStatus(id: String, mode: QuestionDatabaseMode, status: Int): Completable{
        val modeAsInt = when(mode){
            QuestionDatabaseMode.CAR -> 0
            QuestionDatabaseMode.RIDER -> 1
        }
//        return db.questionStatusDao().setQuestionStatus(id, modeAsInt ,status)
        val questionStatus = QuestionStatus(id,  modeAsInt, status)
        return db.questionStatusDao().insertQuestionStatus(questionStatus)
    }
}

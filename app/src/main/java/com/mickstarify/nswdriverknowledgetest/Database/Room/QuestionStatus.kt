package com.mickstarify.nswdriverknowledgetest.Database.Room

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single


@Entity(primaryKeys = arrayOf("questionId", "mode"))
data class QuestionStatus(
    @ColumnInfo(name = "questionId") val questionId: String,
    @ColumnInfo(name = "mode") val mode: Int,
    @ColumnInfo(name = "answerStatus") val answerStatus: Int = 0 // 0 or 1 or 2
) {

    companion object {
        val NOT_ANSWERED = 0
        val ANSWERED_CORRECT = 1
        val ANSWERED_INCORRECT = 2
    }
}

@Dao
interface QuestionStatusDao {

    @Transaction
    @Query("UPDATE QuestionStatus SET answerStatus=:status WHERE questionId=:id AND mode=:mode")
    fun setQuestionStatus(id: String, mode: Int, status: Int): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestionStatus(queetionStatus: QuestionStatus): Completable

    @Query("SELECT * FROM QuestionStatus WHERE questionId=:id AND mode=:mode")
    fun getQuestionStatus(id: String, mode: Int): Maybe<QuestionStatus>

    @Query("SELECT * FROM QuestionStatus WHERE mode=:mode")
    fun getAllQuestionStatuses(mode: Int): List<QuestionStatus>
}
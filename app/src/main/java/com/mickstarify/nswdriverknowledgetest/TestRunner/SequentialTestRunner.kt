package com.mickstarify.nswdriverknowledgetest.TestRunner

import android.util.Log
import com.mickstarify.nswdriverknowledgetest.Database.Question
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabase
import java.lang.Exception

class SequentialTestRunner(val questionDatabase: QuestionDatabase, val initialQuestionId: Int=1) :
    TestRunner {
    var currentQuestionIndex = 0 // will be set to 1 when initiated.

    init {
        if (initialQuestionId != 1) {
            currentQuestionIndex = initialQuestionId - 1
        }
        Log.d("dktcar","opening test at question $initialQuestionId")
    }

    override fun getNextQuestion(): Question {
        currentQuestionIndex++
        return getQuestion()
    }

    override fun getTestStatus(): TestStatus {
        if (currentQuestionIndex >= questionDatabase.questions.size){
            return TestStatus.SUCCESSFULLY_COMPLETED
        }
        return TestStatus.INPROGRESS
    }

    override fun getQuestion(): Question{
        return questionDatabase.questions[currentQuestionIndex-1]
    }

    override fun setUserAnswerCorrectly(isCorrect: Boolean) {
        Log.d("nswdriverknowledgetest", "User answered currectly=$isCorrect")
    }

    override fun getQuestionProgressString(): String {
        return "${currentQuestionIndex}/${questionDatabase.questions.size}"
    }

}
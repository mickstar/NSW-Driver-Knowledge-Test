package com.mickstarify.dktcar.TestRunner

import com.mickstarify.dktcar.Database.Question
import com.mickstarify.dktcar.Database.QuestionDatabase
import java.util.*


/* This method handles the conduct of a test. It decides what question to pick next,
* as well as when the user has failed.*/

val MAX_QUESTIONS = 45
val N_GENERAL_KNOWLEDGE = 15
val N_ROAD_SAFETY = 30

val MAX_ERRORS_ALLOWED_GENERAL_KNOWLEDGE = 3
val MAX_ERRORS_ALLOWED_ROAD_SAFETY = 1
val MAX_ERRORS_ALLOWED_ICAC = 0

class FormalTest(val questionDatabase: QuestionDatabase) : TestRunner {
    var currentQuestionIndex = -1;

    var questionsAsked = LinkedList<Int>()

    var n_inccorect_icac = 0
    var n_incorrect_general_knowledge = 0
    var n_incorrect_road_safety = 0
    var n_incorrect_traffic_signs = 0

    var currentQuestion: Question? = null

    var failedQuestions = LinkedList<Question>()

    override fun getNextQuestion(): Question {
        currentQuestionIndex += 1

        var question = getRandomQuestion()
        var max_i = 0
        while (questionsAsked.contains(question.id)) {
            question = getRandomQuestion()
            max_i += 1
            if (max_i > 400) {
                throw Exception("error no available questions left.") // should never happen.
            }
        }
        questionsAsked.add(question.id)
        currentQuestion = question
        return question
    }

    private fun getRandomQuestion(): Question {
        return when (currentQuestionIndex) {
            0 -> {
                questionDatabase.getRandomICAC()
            }
            in 1..15 -> {
                questionDatabase.getRandomGeneralKnowledge()
            }
            in 15..35 -> {
                questionDatabase.getRandomRoadSafety()
            }
            in 35..45 -> {
                questionDatabase.getRandomTrafficSign()
            }
            46 -> {
                questionDatabase.getRandomICAC()
            }
            else -> {
                throw (Exception("Invalid Question Number"))
            }
        }
    }

    private fun hasFailed(): Boolean{
        if (n_inccorect_icac > MAX_ERRORS_ALLOWED_ICAC){
            return true
        }
        if (n_incorrect_general_knowledge > MAX_ERRORS_ALLOWED_GENERAL_KNOWLEDGE){
            return true
        }
        if (n_incorrect_road_safety > MAX_ERRORS_ALLOWED_ROAD_SAFETY){
            return true
        }
        return false
    }

    override fun getTestStatus(): TestStatus {
        val failed = hasFailed()

        if (currentQuestionIndex >= 46){
            return when(failed){
                true -> TestStatus.FAILED
                false -> TestStatus.SUCCESSFULLY_COMPLETED
            }
        } else {
            return when(failed){
                true -> TestStatus.FAILED
                false -> TestStatus.INPROGRESS
            }
        }
    }

    override fun setUserAnswerCorrectly(isCorrect: Boolean) {
        if (!isCorrect){
            failedQuestions.add(currentQuestion!!)

            when(currentQuestionIndex){
                0,46 -> n_inccorect_icac ++
                in 1..15 -> n_incorrect_general_knowledge ++
                in 16..45 -> n_incorrect_road_safety ++
            }
        }

        // todo add database hook.
    }

    override fun getQuestionProgressString(): String {
        if (currentQuestionIndex == 0 || currentQuestionIndex == 46){
            return "ICAC"
        }

        val errors = n_incorrect_general_knowledge + n_incorrect_road_safety
        val score = currentQuestionIndex - errors
        return "$score/$currentQuestionIndex"
    }

    override fun getQuestion(): Question {
        return currentQuestion!!
    }
}
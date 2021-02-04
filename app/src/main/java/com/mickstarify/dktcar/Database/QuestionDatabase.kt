package com.mickstarify.dktcar.Database

import android.content.Context
import android.util.Log
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mickstarify.dktcar.Database.Room.QuestionStatus
import com.mickstarify.dktcar.Database.Room.QuestionStatusDatabase
import io.reactivex.rxjava3.core.Completable
import java.util.*
import kotlin.collections.HashMap

enum class QuestionDatabaseMode {
    CAR,
    RIDER
}

class QuestionDatabase(
    val context: Context,
    val mode: QuestionDatabaseMode
) {
    lateinit var questions: List<Question>

    lateinit var questionsByCategories: MutableMap<String, MutableList<Question>>

    val questionStatusDatabase = QuestionStatusDatabase(context)

    var categories = LinkedList<String>()

    var questionStatuses = HashMap<String, Int>()


    var CAR_ICAC_MAX_INDEX = 3
    var CAR_GENERAL_KNOWLEDGE_MAX_INDEX = 85
    var CAR_ROADSAFETY_MAX_INDEX = 313
    var CAR_TRAFFIC_SIGNS_MAX_INDEX = 364

    var RIDER_ICAC_MAX_INDEX = 3
    var RIDER_GENERAL_KNOWLEDGE_MAX_INDEX = 71
    var RIDER_ROADSAFETY_MAX_INDEX = 268
    var RIDER_TRAFFIC_SIGNS_MAX_INDEX = 319

    fun getRandomICAC(): Question {
        var MAX_ICAC = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_ICAC_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_ICAC_MAX_INDEX
        }
        return questions.subList(0, MAX_ICAC).random()
    }

    fun getRandomGeneralKnowledge(): Question {
        var MIN_GENERAL_KNOWLEDGE = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_ICAC_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_ICAC_MAX_INDEX
        }
        var MAX_GENERAL_KNOWLEDGE = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_GENERAL_KNOWLEDGE_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_GENERAL_KNOWLEDGE_MAX_INDEX
        }

        return questions.subList(MIN_GENERAL_KNOWLEDGE, MAX_GENERAL_KNOWLEDGE).random()
    }

    fun getRandomRoadSafety(): Question {
        var MAX_ROAD_SAFETY = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_ROADSAFETY_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_ROADSAFETY_MAX_INDEX
        }
        var MIN_ROAD_SAFETY = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_GENERAL_KNOWLEDGE_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_GENERAL_KNOWLEDGE_MAX_INDEX
        }

        return questions.subList(MIN_ROAD_SAFETY, MAX_ROAD_SAFETY).random()
    }

    fun getRandomTrafficSign(): Question {
        var MAX_TRAFFIC_SIGN = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_TRAFFIC_SIGNS_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_TRAFFIC_SIGNS_MAX_INDEX
        }
        var MIN_TRAFFIC_SIGN = when (mode) {
            QuestionDatabaseMode.CAR -> CAR_ROADSAFETY_MAX_INDEX
            QuestionDatabaseMode.RIDER -> RIDER_ROADSAFETY_MAX_INDEX
        }

        return questions.subList(MIN_TRAFFIC_SIGN, MAX_TRAFFIC_SIGN).random()
    }

    private fun loadDatabase(jsonQuestionsIS: InputStream) {
        // method needs to load the json file into memory.
        val reader = InputStreamReader(jsonQuestionsIS)
        val typeToken = object : TypeToken<List<Question>>() {}.type
        questions = Gson().fromJson(reader, typeToken)

        questionsByCategories = HashMap<String, MutableList<Question>>()
        for (question in questions) {
            if (!questionsByCategories.containsKey(question.category)) {
                categories.add(question.category)
                questionsByCategories[question.category] = LinkedList<Question>()
            }
            questionsByCategories[question.category]!!.add(question)
        }
    }

    fun loadQuestionStatuses(): Completable {
        val questionStatuses = questionStatusDatabase.getAllQuestionStatuses(mode).doAfterSuccess {
            for (qStatus: QuestionStatus in it) {
                questionStatuses[qStatus.questionId] = qStatus.answerStatus
            }
        }

        return Completable.fromSingle(questionStatuses)
    }

    fun getCategories(): List<String> {
        return questionsByCategories.keys.sorted()
    }

    fun getQuestionsByCategory(category: String): List<Question> {
        return questionsByCategories[category]
            ?: throw IllegalArgumentException("Category $category doesn't exist.")
    }

    init {
        Log.d(
            "dktcar", "initiated questiondatabase (${
                if (mode == QuestionDatabaseMode.CAR) {
                    "car"
                } else {
                    "rider"
                }
            })"
        )
        when (mode) {
            QuestionDatabaseMode.CAR -> {
                loadDatabase(context.assets.open("dkt-car.json"))
            }
            QuestionDatabaseMode.RIDER -> {
                loadDatabase(context.assets.open("dkt-rider.json"))
            }
        }
    }
}
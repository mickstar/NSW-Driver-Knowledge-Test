package com.mickstarify.dktcar.QuestionListScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.mickstarify.dktcar.Database.QuestionDatabase
import com.mickstarify.dktcar.NSWDriverKnowledgeTestApplication
import com.mickstarify.dktcar.PreferenceManager
import com.mickstarify.dktcar.QuestionScreen.QuestionActivityView
import java.util.*
import javax.inject.Inject

class QuestionListActivityModel(val context: Context) : Model {
    @Inject
    lateinit var questionDatabase: QuestionDatabase

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun getQuestions(): List<QuestionCategoryObject> {
        val list = LinkedList<QuestionCategoryObject>()

        for (category in questionDatabase.categories){
            list.add(QuestionCategoryObject(Category(category)))
            for (question in questionDatabase.questionsByCategories[category]!!){
                list.add(QuestionCategoryObject(question))
            }
        }

        return list
    }

    override fun openQuestionWithId(questionId: Int) {
        val intent = Intent(context, QuestionActivityView::class.java)
        intent.putExtra(QuestionActivityView.PARAM_MODE, QuestionActivityView.MODE_SEQUENTIAL_QUESTIONS)
        intent.putExtra(QuestionActivityView.QUESTION_ID, questionId)
        context.startActivity(intent)
    }

    init {
        ((context as Activity).application as NSWDriverKnowledgeTestApplication).component.inject(
            this
        )
    }

}
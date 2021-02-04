package com.mickstarify.dktcar.EntryScreen

import android.content.Context
import android.content.Intent
import com.mickstarify.dktcar.Database.QuestionDatabaseMode
import com.mickstarify.dktcar.PreferenceManager
import com.mickstarify.dktcar.QuestionListScreen.QuestionListActivityView
import com.mickstarify.dktcar.QuestionScreen.QuestionActivityView
import java.lang.IllegalArgumentException

class EntryActivityModel(
    val context: Context,
    val presenter: Presenter
) : Model {
    val preferenceManager = PreferenceManager(context)

    override fun setQuestionMode(selectedMode: String) {
        val mode = when(selectedMode) {
            "CAR" -> QuestionDatabaseMode.CAR
            "RIDER" -> QuestionDatabaseMode.RIDER
            else -> throw IllegalArgumentException("INVALID QUESTION MODE $selectedMode")
        }

        preferenceManager.setQuestionMode(mode)
    }

    override fun initiateStartTest() {
        val intent = Intent(context, QuestionActivityView::class.java)
        intent.putExtra(QuestionActivityView.PARAM_MODE, QuestionActivityView.MODE_FORMAL_TEST)
        context.startActivity(intent)
    }

    override fun initiateBrowseQuestions() {
        val intent = Intent(context, QuestionListActivityView::class.java)
        context.startActivity(intent)
    }

    override fun getQuestionMode(): QuestionDatabaseMode {
        return preferenceManager.getQuestionMode()
    }

}
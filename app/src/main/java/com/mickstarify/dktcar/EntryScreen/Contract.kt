package com.mickstarify.dktcar.EntryScreen

import com.mickstarify.dktcar.Database.QuestionDatabaseMode

interface View {
    fun initUI()
    fun setQuestionModeToggle(questionMode: QuestionDatabaseMode)
}

interface Presenter {
    fun startTestButtonPressed()
    fun browseQuestionsButtonPressed()
    fun questionModeToggled(selectedMode: String)

}

interface Model {
    fun setQuestionMode(selectedMode: String)
    fun getQuestionMode(): QuestionDatabaseMode
    fun initiateStartTest()
    fun initiateBrowseQuestions()
}
package com.mickstarify.nswdriverknowledgetest.EntryScreen

import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabaseMode

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
package com.mickstarify.nswdriverknowledgetest.QuestionListScreen

import androidx.recyclerview.widget.RecyclerView
import com.mickstarify.nswdriverknowledgetest.Database.Question

interface View {
    fun initUI()
    fun populateList(questionListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?)
    fun pressedQuestion(questionTitle: String)
}

interface Presenter {
}

interface Model {
    fun getQuestions(): List<QuestionCategoryObject>
    fun openQuestionWithId(questionId: Int)
}